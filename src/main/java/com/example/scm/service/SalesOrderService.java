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
    // 이 함수에는 일부러 @Transactional을 붙이지 않았습니다. 번호가 겹쳐 저장이 실패하면 그 저장 묶음은
    // 더 쓸 수 없는 상태가 되므로, 실제 저장은 매 시도마다 새 묶음을 여는 SalesOrderPersistenceService에 맡깁니다.
    public SalesOrderResponse create(SalesOrderCreateRequest request, LoginUser user) {
        Authz.requireLogin(user);
        // Controller의 @Valid가 이미 검사했더라도 여기서 다시 검사합니다.
        // 테스트나 다른 Service처럼 Controller를 거치지 않고 이 함수를 부르는 경우에도 잘못된 주문이 저장되지 않게 하는 마지막 방어선입니다.
        if (request == null) throw new BusinessException(ErrorCode.INVALID_INPUT);
        // 물건 줄이 없는 경우는 일반 입력 오류보다 원인이 분명하므로 EMPTY_ORDER_LINES로 따로 알려 줍니다.
        if (request.getLines() == null || request.getLines().isEmpty())
            throw new BusinessException(ErrorCode.EMPTY_ORDER_LINES);
        // validator는 필수 입력, 수량, 글자 수 같은 양식 규칙을 검사하는 도구입니다.
        if (!validator.validate(request).isEmpty()) throw new BusinessException(ErrorCode.INVALID_INPUT);
        Partner partner = partners.findById(request.getPartnerId())
                .orElseThrow(() -> new BusinessException(ErrorCode.PARTNER_NOT_FOUND));
        // 수주는 "고객에게 파는" 주문이므로 공급사(SUPPLIER) 전용 거래처로는 만들 수 없습니다.
        // canBuy는 CUSTOMER 또는 BOTH 유형일 때만 참입니다.
        if (!partner.canBuy()) throw new BusinessException(ErrorCode.PARTNER_TYPE_MISMATCH);
        // 거래를 멈춘(비활성) 거래처와는 새 주문을 시작하지 않습니다. 과거 주문 조회는 따로 허용합니다.
        if (!partner.isActive()) throw new BusinessException(ErrorCode.INVALID_STATUS);
        // 출고 예정일은 비워 둘 수 있지만, 적었다면 주문일보다 앞설 수 없습니다. 주문하기 전에 보낼 수는 없기 때문입니다.
        if (request.getShipDate() != null && request.getOrderDate().isAfter(request.getShipDate()))
            throw new BusinessException(ErrorCode.INVALID_DATE_RANGE);
        // 각 주문 줄의 물건을 찾고 사용할 수 있는지 검사한 뒤 수량과 가격을 정리합니다.
        List<ResolvedLine> lines = request.getLines().stream().map(line -> {
            Item item = items.findById(line.getItemId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.ITEM_NOT_FOUND));
            // 단종된 물건은 더 이상 팔지 않으므로 새 주문 줄에 넣을 수 없습니다.
            if (!item.isActive()) throw new BusinessException(ErrorCode.ITEM_DISCONTINUED);
            // 단가를 비워 두면 품목에 등록된 표준 단가를 씁니다. 할인 등으로 값을 적었다면 그 값을 존중합니다.
            BigDecimal price = line.getUnitPrice() == null ? item.getUnitPrice() : line.getUnitPrice();
            validateMoney(price);
            // 단가 하나는 정상이어도 수량을 곱하면 저장 칸(정수 13자리)을 넘을 수 있어 줄 금액도 따로 검사합니다.
            validateMoney(price.multiply(BigDecimal.valueOf(line.getQuantity())));
            return new ResolvedLine(item, line.getQuantity(), price);
        }).toList();
        // reduce는 여러 값을 하나로 합치는 연산입니다. 여기서는 각 줄의 금액을 모두 더합니다.
        validateMoney(lines.stream().map(l -> l.price().multiply(BigDecimal.valueOf(l.quantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        // 저장할 번호가 겹치면 잠깐 무작위로 기다린 뒤 새 주문 객체와 새 저장 묶음으로 다시 시도합니다.
        // 시도 횟수와 대기 규칙은 발주와 같은 OrderNumberRetry를 따릅니다.
        for (int attempt = 0; attempt < OrderNumberRetry.MAX_ATTEMPTS; attempt++) {
            // 번호의 날짜는 사용자가 적은 주문일이 아니라 실제로 등록한 오늘 날짜입니다.
            // 그래서 주문일을 며칠 전으로 적어도 번호는 오늘 순번에 이어 붙고, 번호만 보면 언제 시스템에 입력됐는지 알 수 있습니다.
            String number = numbers.generate(LocalDate.now());
            // 실패한 시도에서 쓰던 주문 객체는 이미 저장 실패 흔적이 남았을 수 있어, 매번 새로 만듭니다.
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
        // 여러 번 다시 시도해도 번호를 얻지 못하면 끝없이 기다리게 두지 않고, 사용자에게 재시도를 안내합니다.
        throw new BusinessException(ErrorCode.INTERNAL_ERROR, "수주번호 채번에 실패했습니다. 다시 시도하세요.");
    }

    @Transactional
    public SalesOrderResponse confirm(Long id, LoginUser user) {
        // 확정은 작성자 본인만 합니다. 관리 역할이 있어도 다른 사람의 주문을 대신 확정할 수 없습니다.
        // 확정은 "이 내용으로 고객에게 팔겠다"는 작성자의 약속이므로, 내용을 잘 아는 작성자가 직접 해야 합니다.
        Authz.requireLogin(user);
        SalesOrder order = locked(id);
        if (!order.getWriterId().equals(user.id())) throw new BusinessException(ErrorCode.ACCESS_DENIED);
        // 확정은 재고를 건드리지 않으므로 출고와 달리 품목 잠금(lockItems)이 필요 없습니다.
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
        // 재고 줄을 잠그기 전에 품목부터 정해진 순서로 잠급니다. 입고 처리도 같은 순서를 쓰므로
        // 출고와 입고가 동시에 같은 품목을 건드려도 서로를 영원히 기다리는(교착) 일이 생기지 않습니다.
        lockItems(order);
        // 재고 기록이 아예 없는 품목은 "한 번도 들어온 적 없음", 즉 재고 0개와 같으므로 재고 부족으로 알립니다.
        // decrease는 남은 수량보다 많이 빼려 하면 오류를 내고, 그 오류로 이번 출고 전체가 되돌아갑니다.
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
            // 출고할 때 재고를 뺐다면 그 재고 기록은 반드시 있어야 합니다. 없다면 사용자 실수가 아니라
            // 데이터가 어긋난 상황이므로 재고 부족이 아닌 INTERNAL_ERROR로 알리고 취소 전체를 되돌립니다.
            for (SalesOrderLine line : order.getLines()) stocks.findByItemIdForUpdate(line.getItemId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.INTERNAL_ERROR)).increase(line.getQuantity());
        }
        return response(order, true);
    }
    @Transactional(readOnly = true)
    public SalesOrderResponse detail(Long id, LoginUser user) {
        Authz.requireLogin(user);
        // 조회는 아무것도 바꾸지 않으므로 잠금 없이 findById로 읽습니다. 잠그면 다른 사람의 출고·취소를 괜히 기다리게 합니다.
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
        // "처리 대기"는 확정(CONFIRMED)되어 창고에서 출고를 기다리는 주문입니다.
        // 출고는 관리자·매니저 업무이므로 이 목록도 같은 역할만 볼 수 있습니다.
        Authz.requireRole(user, UserRole.ADMIN, UserRole.MANAGER);
        // 정렬 기준을 허용 목록으로 검사합니다. 없는 필드 이름이 그대로 DB 조회에 들어가 서버 오류가 나는 것을 막기 위해서입니다.
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
        // 새 수주 작성 화면의 고객사 선택 목록입니다. 검색용(customerSearchOptions)과 달리
        // 지금 실제로 팔 수 있는(구매 가능 + 활성) 거래처만 보여 줘야 저장 단계에서 거절당하는 일이 없습니다.
        return partners.findAll().stream().filter(Partner::canBuy).filter(Partner::isActive).map(PartnerOption::from).toList();
    }
    // 대시보드에 "출고를 기다리는 수주 n건"을 보여 줄 때 씁니다. 목록 전체를 읽지 않고 개수만 셉니다.
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
        // 주문에는 거래처·작성자의 번호만 저장되어 있습니다. 나중에 거래처나 사용자가 지워져도
        // 과거 주문 화면이 오류로 멈추지 않도록, 찾지 못하면 "삭제된 거래처"처럼 대신할 이름을 넣습니다.
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
    // 검사를 마친 주문 줄 하나를 잠시 담는 상자입니다. 번호 충돌로 다시 시도할 때
    // 품목을 다시 찾거나 가격을 다시 계산하지 않고 이 값을 그대로 재사용하기 위해 따로 모아 둡니다.
    private record ResolvedLine(Item item, Integer quantity, BigDecimal price) {}
}
