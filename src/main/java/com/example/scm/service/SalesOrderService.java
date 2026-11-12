package com.example.scm.service;
import com.example.scm.common.validation.PageableValidator;

import com.example.scm.common.auth.Authz;
import com.example.scm.common.auth.LoginUser;
import com.example.scm.common.exception.BusinessException;
import com.example.scm.common.exception.ErrorCode;
import com.example.scm.domain.*;
import com.example.scm.domain.enums.PartnerType;
import com.example.scm.domain.enums.SalesOrderStatus;
import com.example.scm.domain.enums.UserRole;
import com.example.scm.dto.salesorder.*;
import com.example.scm.dto.purchaseorder.PartnerOption;
import com.example.scm.repository.*;
import com.example.scm.repository.spec.SalesOrderSpecs;
import jakarta.validation.Validator;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 고객이 우리 회사 물건을 사는 주문인 수주를 처리합니다.
 * 작성 → 확정 → 출고 → 완료 순서로 진행하며, 출고하면 창고 수량을 줄입니다.
 * 주문 객체는 진행 단계를 검사하고, 이 서비스는 권한 확인과 재고 변경을 함께 처리합니다.
 */
@Service
@RequiredArgsConstructor
public class SalesOrderService {
    // 금액은 소수 둘째 자리까지 저장합니다. 줄별 금액과 전체 합계도 이 한도를 넘지 않아야 합니다.
    private static final BigDecimal MAX_MONEY = new BigDecimal("9999999999999.99");
    private final SalesOrderRepository orders;
    private final PartnerRepository partners;
    private final ItemRepository items;
    private final UserRepository users;
    private final StockRepository stocks;
    private final SalesOrderNumberGenerator numbers;
    private final SalesOrderPersistenceService persistence;
    private final Validator validator;

    // 수주 작성 순서: 로그인 확인 → 고객사·날짜·물건 검사 → 금액 계산 → 번호를 붙여 저장.
    public SalesOrderResponse create(SalesOrderCreateRequest request, LoginUser user) {
        Authz.requireLogin(user);
        if (request == null) throw new BusinessException(ErrorCode.INVALID_INPUT);
        if (request.getLines() == null || request.getLines().isEmpty())
            throw new BusinessException(ErrorCode.EMPTY_ORDER_LINES);
        // validator는 필수 입력, 수량, 글자 수 같은 양식 규칙을 검사하는 도구입니다.
        if (!validator.validate(request).isEmpty()) throw new BusinessException(ErrorCode.INVALID_INPUT);
        Partner partner = partners.findById(request.getPartnerId())
                .orElseThrow(() -> new BusinessException(ErrorCode.PARTNER_NOT_FOUND));
        if (!partner.canBuy()) throw new BusinessException(ErrorCode.PARTNER_TYPE_MISMATCH);
        if (!partner.isActive()) throw new BusinessException(ErrorCode.INVALID_STATUS);
        if (request.getShipDate() != null && request.getOrderDate().isAfter(request.getShipDate()))
            throw new BusinessException(ErrorCode.INVALID_DATE_RANGE);
        // 각 주문 줄의 물건을 찾고 사용할 수 있는지 검사한 뒤 수량과 가격을 정리합니다.
        List<ResolvedLine> lines = request.getLines().stream().map(line -> {
            Item item = items.findById(line.getItemId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.ITEM_NOT_FOUND));
            if (!item.isActive()) throw new BusinessException(ErrorCode.ITEM_DISCONTINUED);
            BigDecimal price = line.getUnitPrice() == null ? item.getUnitPrice() : line.getUnitPrice();
            validateMoney(price);
            validateMoney(price.multiply(BigDecimal.valueOf(line.getQuantity())));
            return new ResolvedLine(item, line.getQuantity(), price);
        }).toList();
        // reduce는 여러 값을 하나로 합치는 연산입니다. 여기서는 각 줄의 금액을 모두 더합니다.
        validateMoney(lines.stream().map(l -> l.price().multiply(BigDecimal.valueOf(l.quantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        // 저장할 번호가 겹치면 잠깐 무작위로 기다린 뒤 새 주문 객체와 새 저장 묶음으로 다시 시도합니다.
        // 시도 횟수와 대기 규칙은 발주와 같은 OrderNumberRetry를 따릅니다.
        for (int attempt = 0; attempt < OrderNumberRetry.MAX_ATTEMPTS; attempt++) {
            String number = numbers.generate(LocalDate.now());
            SalesOrder order = new SalesOrder(number, partner.getId(), user.id(), request.getOrderDate(), request.getShipDate());
            for (ResolvedLine line : lines) order.addLine(SalesOrderLine.builder()
                    .itemId(line.item().getId()).quantity(line.quantity()).unitPrice(line.price()).build());
            try {
                SalesOrder saved = persistence.saveAndFlush(order);
                return new SalesOrderResponse(saved.getId(), number, partner.getId(), partner.getName(), user.id(), user.name(),
                        null, saved.getOrderDate(), saved.getShipDate(), saved.getTotalAmount(), saved.getStatus(), null,
                        saved.getCreatedAt(), null, null, null, lines.stream().map(l -> new SalesOrderResponse.Line(
                        l.item().getId(), l.item().getItemCode(), l.item().getName(), l.quantity(), l.price(),
                        l.price().multiply(BigDecimal.valueOf(l.quantity())))).toList());
            } catch (DataIntegrityViolationException e) {
                // 번호 중복일 때만 다시 시도합니다. 다른 저장 오류는 호출한 곳에 그대로 알립니다.
                if (!orders.existsByOrderNumber(number)) throw e;
                OrderNumberRetry.backoff(attempt);
            }
        }
        throw new BusinessException(ErrorCode.INTERNAL_ERROR, "수주번호 채번에 실패했습니다. 다시 시도하세요.");
    }

    @Transactional
    public SalesOrderResponse confirm(Long id, LoginUser user) {
        // 확정은 작성자 본인만 합니다. 관리 역할이 있어도 다른 사람의 주문을 대신 확정할 수 없습니다.
        Authz.requireLogin(user);
        SalesOrder order = locked(id);
        if (!order.getWriterId().equals(user.id())) throw new BusinessException(ErrorCode.ACCESS_DENIED);
        order.confirm(); return response(order, true);
    }
    // 출고 상태 변경과 재고 빼기는 한 저장 묶음입니다. 한 품목이라도 부족하면 모두 되돌립니다.
    @Transactional
    public SalesOrderResponse ship(Long id, LoginUser user) {
        // @Transactional은 상태 변경과 재고 변경을 묶습니다. 실패 신호가 나면 이번 묶음을 되돌립니다.
        Authz.requireRole(user, UserRole.ADMIN, UserRole.MANAGER);
        SalesOrder order = locked(id);
        // 먼저 확정된 주문인지 검사합니다. 재고 변경 실패 시 이 상태 변경도 함께 되돌아갑니다.
        order.ship(user.id());
        lockItems(order);
        for (SalesOrderLine line : order.getLines()) stocks.findByItemIdForUpdate(line.getItemId())
                .orElseThrow(() -> new BusinessException(ErrorCode.INSUFFICIENT_STOCK)).decrease(line.getQuantity());
        return response(order, true);
    }
    @Transactional
    public SalesOrderResponse complete(Long id, LoginUser user) {
        // 출고에서 이미 재고를 뺐으므로 완료에서는 재고를 다시 빼지 않고 완료 상태와 처리자·시각을 기록합니다.
        Authz.requireRole(user, UserRole.ADMIN, UserRole.MANAGER);
        SalesOrder order = locked(id); order.complete(user.id()); return response(order, true);
    }
    @Transactional
    public SalesOrderResponse cancel(Long id, String reason, LoginUser user) {
        Authz.requireLogin(user);
        SalesOrder order = locked(id); requireAccess(order, user);
        // 물건을 이미 보낸 SHIPPED 주문만 취소할 때 창고 수량을 되돌려야 합니다.
        boolean restoreStock = order.getStatus() == SalesOrderStatus.SHIPPED;
        // 출고 후 취소는 창고 재고를 늘리므로 실물 반품을 확인하는 ADMIN/MANAGER만 할 수 있다.
        // 일반 작성자는 출고 전(DRAFT/CONFIRMED)까지만 직접 취소한다.
        if (restoreStock && !user.isAdmin() && !user.isManager())
            throw new BusinessException(ErrorCode.ACCESS_DENIED, "출고된 수주는 관리자 또는 매니저만 취소할 수 있습니다.");
        order.cancel(reason);
        // 출고 후 취소가 허용되므로, 주문 취소와 출고 수량 복원이 함께 성공해야 합니다.
        if (restoreStock) {
            lockItems(order);
            for (SalesOrderLine line : order.getLines()) stocks.findByItemIdForUpdate(line.getItemId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.INTERNAL_ERROR)).increase(line.getQuantity());
        }
        return response(order, true);
    }
    @Transactional(readOnly = true)
    public SalesOrderResponse detail(Long id, LoginUser user) {
        Authz.requireLogin(user);
        SalesOrder order = orders.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.SALES_ORDER_NOT_FOUND));
        requireAccess(order, user); return response(order, true);
    }
    @Transactional(readOnly = true)
    public Page<SalesOrderResponse> myOrders(LoginUser user, SalesOrderStatus status, Pageable pageable) {
        Authz.requireLogin(user);
        PageableValidator.validate(pageable, PageableValidator.SALES_ORDER);
        return summaryPage(orders.findMyOrders(user.id(), status, pageable));
    }
    @Transactional(readOnly = true)
    public Page<SalesOrderResponse> pending(LoginUser user, Pageable pageable) {
        Authz.requireRole(user, UserRole.ADMIN, UserRole.MANAGER);
        PageableValidator.validate(pageable, PageableValidator.SALES_ORDER);
        return summaryPage(orders.findByStatus(SalesOrderStatus.CONFIRMED, pageable));
    }
    @Transactional(readOnly = true)
    public Page<SalesOrderResponse> adminOrders(LoginUser user, SalesOrderStatus status, Long partnerId,
                                              LocalDate from, LocalDate to, Pageable pageable) {
        Authz.requireRole(user, UserRole.ADMIN, UserRole.MANAGER);
        PageableValidator.validate(pageable, PageableValidator.SALES_ORDER);
        if (from != null && to != null && from.isAfter(to))
            throw new BusinessException(ErrorCode.INVALID_DATE_RANGE);
        // 화면 요청과 API 요청 모두 같은 권한 검사와 검색 규칙을 사용합니다.
        return summaryPage(orders.findAll(SalesOrderSpecs.adminSearch(status, partnerId, from, to),
                pageable));
    }
    @Transactional(readOnly = true)
    public List<PartnerOption> customerSearchOptions(LoginUser user) {
        Authz.requireRole(user, UserRole.ADMIN, UserRole.MANAGER);
        // 비활성화되거나 공급사로 유형을 바꾼 거래처의 과거 수주도 검색할 수 있어야 한다.
        return partners.findSalesSearchPartners(List.of(PartnerType.CUSTOMER, PartnerType.BOTH))
                .stream().map(PartnerOption::from).toList();
    }
    @Transactional(readOnly = true)
    public List<PartnerOption> customerOptions() {
        return partners.findAll().stream().filter(Partner::canBuy).filter(Partner::isActive).map(PartnerOption::from).toList();
    }
    @Transactional(readOnly = true)
    public long countPending() { return orders.countByStatus(SalesOrderStatus.CONFIRMED); }

    private SalesOrder locked(Long id) {
        // 같은 주문을 두 사람이 동시에 처리하지 않도록, 이 저장 묶음이 끝날 때까지 장부를 잠급니다.
        return orders.findForUpdate(id).orElseThrow(() -> new BusinessException(ErrorCode.SALES_ORDER_NOT_FOUND));
    }
    private void lockItems(SalesOrder order) {
        // 입고와 같은 품목 잠금을 ID 순서로 사용해 출고/취소/입고 간 재고 경합을 막는다.
        // 예: 3번과 1번 품목이 있어도 모두 1번 → 3번으로 잠가 서로 반대 순서로 기다리는 일을 줄입니다.
        items.findAllByIdForUpdate(order.getLines().stream().map(SalesOrderLine::getItemId).distinct().sorted().toList());
    }
    private void requireAccess(SalesOrder order, LoginUser user) {
        // 일반 사용자는 본인 주문만, 관리자와 매니저는 다른 사람의 주문도 조회·취소할 수 있습니다.
        if (!order.getWriterId().equals(user.id()) && !user.isAdmin() && !user.isManager())
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
    }
    private void validateMoney(BigDecimal value) {
        // signum은 음수 여부, scale은 소수 자릿수, compareTo는 최대 금액과의 크기를 검사합니다.
        if (value.signum() < 0 || value.scale() > 2 || value.compareTo(MAX_MONEY) > 0)
            throw new BusinessException(ErrorCode.INVALID_INPUT, "금액은 정수 13자리, 소수 2자리 이하여야 합니다.");
    }
    private Page<SalesOrderResponse> summaryPage(Page<SalesOrder> page) {
        if (page.isEmpty()) return page.map(o -> response(o, false));
        // 같은 회사·작성자를 주문마다 다시 찾지 않도록, 이번 페이지에 필요한 이름을 한 번에 읽습니다.
        Map<Long, String> partnerNames = partners.findAllById(page.stream().map(SalesOrder::getPartnerId).distinct().toList())
                .stream().collect(Collectors.toMap(Partner::getId, Partner::getName));
        Map<Long, String> writerNames = users.findAllById(page.stream().map(SalesOrder::getWriterId).distinct().toList())
                .stream().collect(Collectors.toMap(User::getId, User::getName));
        return page.map(o -> new SalesOrderResponse(o.getId(), o.getOrderNumber(), o.getPartnerId(),
                partnerNames.getOrDefault(o.getPartnerId(), "삭제된 거래처"), o.getWriterId(),
                writerNames.getOrDefault(o.getWriterId(), "알 수 없음"), o.getManagerId(), o.getOrderDate(),
                o.getShipDate(), o.getTotalAmount(), o.getStatus(), o.getCancelReason(), o.getCreatedAt(),
                o.getConfirmedAt(), o.getShippedAt(), o.getCompletedAt(), List.of()));
    }
    private SalesOrderResponse response(SalesOrder o, boolean includeLines) {
        String partnerName = partners.findById(o.getPartnerId()).map(Partner::getName).orElse("삭제된 거래처");
        String writerName = users.findById(o.getWriterId()).map(User::getName).orElse("알 수 없음");
        // 상세 조회에는 물건별 내역을 담고, 요약 조회에는 빈 목록을 넣습니다.
        List<SalesOrderResponse.Line> lines = includeLines ? o.getLines().stream().map(l -> {
            Item item = items.findById(l.getItemId()).orElseThrow(() -> new BusinessException(ErrorCode.ITEM_NOT_FOUND));
            return new SalesOrderResponse.Line(l.getItemId(), item.getItemCode(), item.getName(), l.getQuantity(), l.getUnitPrice(), l.getLineAmount());
        }).toList() : List.of();
        return new SalesOrderResponse(o.getId(), o.getOrderNumber(), o.getPartnerId(), partnerName, o.getWriterId(), writerName,
                o.getManagerId(), o.getOrderDate(), o.getShipDate(), o.getTotalAmount(), o.getStatus(), o.getCancelReason(),
                o.getCreatedAt(), o.getConfirmedAt(), o.getShippedAt(), o.getCompletedAt(), lines);
    }
    private record ResolvedLine(Item item, Integer quantity, BigDecimal price) {}
}
