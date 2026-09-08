package com.example.scm.service;

import com.example.scm.common.auth.Authz;
import com.example.scm.common.auth.LoginUser;
import com.example.scm.common.exception.BusinessException;
import com.example.scm.common.exception.ErrorCode;
import com.example.scm.domain.Item;
import com.example.scm.domain.Partner;
import com.example.scm.domain.PurchaseOrder;
import com.example.scm.domain.PurchaseOrderLine;
import com.example.scm.domain.Stock;
import com.example.scm.domain.User;
import com.example.scm.domain.enums.ItemStatus;
import com.example.scm.domain.enums.PurchaseOrderStatus;
import com.example.scm.domain.enums.UserRole;
import com.example.scm.dto.purchaseorder.ItemOption;
import com.example.scm.dto.purchaseorder.PartnerOption;
import com.example.scm.dto.purchaseorder.PurchaseOrderCreateRequest;
import com.example.scm.dto.purchaseorder.PurchaseOrderCreateResponse;
import com.example.scm.dto.purchaseorder.PurchaseOrderDetailResponse;
import com.example.scm.dto.purchaseorder.PurchaseOrderDetailView;
import com.example.scm.dto.purchaseorder.PurchaseOrderStatusResponse;
import com.example.scm.dto.purchaseorder.PurchaseOrderSummaryResponse;
import com.example.scm.repository.ItemRepository;
import com.example.scm.repository.PartnerRepository;
import com.example.scm.repository.PurchaseOrderRepository;
import com.example.scm.repository.StockRepository;
import com.example.scm.repository.UserRepository;
import com.example.scm.repository.spec.PurchaseOrderSpecs;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 발주 업무 흐름 전체를 조정하는 핵심 서비스.
 *
 * <p>컨트롤러에서 받은 요청을 검증하고, 발주번호를 만들고, 헤더와 라인을 저장한다.
 * 이후 결재 요청 → 승인/반려 → 입고 또는 취소라는 상태 전이를 권한과 함께 검사한다.
 * 입고 시에는 발주 상태 변경과 재고 증가를 하나의 트랜잭션으로 묶어 둘 중 하나만
 * 반영되는 일을 막는다.</p>
 *
 * <p>큰 파일을 읽을 때는 {@code 작성 -> 상태 전이 -> 조회 -> DTO 변환} 구역 순서로
 * 보면 된다. 상태 변경 자체의 규칙은 {@link PurchaseOrder}에, 여러 저장소를 함께
 * 사용하는 작업 순서는 이 서비스에 둔다.</p>
 *
 * <p>학습 순서: 모듈 10 작성 → 11 결재·입고 → 29 조회·취소 순으로 구역을 배치했다.
 * 모듈 04의 발주 엔티티와 07의 Repository 기초를 끝낸 뒤 읽는다.</p>
 */
@Service
@RequiredArgsConstructor
public class PurchaseOrderService {

    private static final int ORDER_NUMBER_RETRY = 5;
    private static final int REJECT_REASON_MAX_LENGTH = 500;
    private static final BigDecimal MAX_MONEY = new BigDecimal("9999999999999.99");

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final StockRepository stockRepository;
    private final PartnerRepository partnerRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final OrderNumberGenerator orderNumberGenerator;
    private final PurchaseOrderPersistenceService purchaseOrderPersistenceService;

    // ====================================================================
    // 모듈 10: 헤더와 라인 함께 저장, 금액 서버 계산, 발주번호 생성
    // ====================================================================

    public PurchaseOrderCreateResponse create(PurchaseOrderCreateRequest request, LoginUser loginUser) {
        Authz.requireLogin(loginUser);

        // 1) 라인 존재 검증
        List<PurchaseOrderCreateRequest.LineRequest> lineReqs = request.getLines();
        if (lineReqs == null || lineReqs.isEmpty()) {
            throw new BusinessException(ErrorCode.EMPTY_ORDER_LINES);
        }
        // HTTP 검증을 거치지 않는 직접 호출에서도 null 행을 저장 전에 거부한다.
        if (lineReqs.stream().anyMatch(line -> line == null)) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "발주 라인은 비어 있을 수 없습니다.");
        }

        // 2) 공급사 검증: 존재 / SUPPLIER|BOTH / ACTIVE
        Partner partner = partnerRepository.findById(request.getPartnerId())
                .orElseThrow(() -> new BusinessException(ErrorCode.PARTNER_NOT_FOUND));
        if (!partner.canSupply()) {
            throw new BusinessException(ErrorCode.PARTNER_TYPE_MISMATCH);
        }
        if (!partner.isActive()) {
            throw new BusinessException(ErrorCode.INVALID_STATUS,
                    "비활성 상태의 거래처로는 발주할 수 없습니다.");
        }

        // 3) 날짜 검증: orderDate ≤ dueDate (dueDate 있을 때)
        if (request.getDueDate() != null && request.getOrderDate().isAfter(request.getDueDate())) {
            throw new BusinessException(ErrorCode.INVALID_DATE_RANGE);
        }

        // 4) 발주 헤더 생성 (채번 + UNIQUE 충돌 재시도)
        PurchaseOrder po = saveWithOrderNumber(request, loginUser, lineReqs);
        return PurchaseOrderCreateResponse.from(po);
    }

    private PurchaseOrder saveWithOrderNumber(PurchaseOrderCreateRequest request, LoginUser loginUser,
                                              List<PurchaseOrderCreateRequest.LineRequest> lineReqs) {
        // 라인 검증과 단가 결정은 발주번호 재시도와 무관하므로 한 번만 수행한다.
        List<ResolvedLine> resolvedLines = lineReqs.stream().map(this::resolveLine).toList();
        BigDecimal totalAmount = resolvedLines.stream()
                .map(this::calculateLineAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add); // 클라이언트 값을 받지 않고 총액을 서버 계산
        validateMoney(totalAmount, "총금액");

        // 동시에 같은 번호를 만든 요청이 있으면 DB UNIQUE 제약이 한 요청을 막는다.
        // 그 경우에만 새 번호를 받아 제한된 횟수만큼 다시 저장한다.
        for (int attempt = 0; attempt < ORDER_NUMBER_RETRY; attempt++) {
            String orderNumber = orderNumberGenerator.generate(LocalDate.now());
            PurchaseOrder po = PurchaseOrder.builder()
                    .orderNumber(orderNumber)
                    .partnerId(request.getPartnerId())
                    .writerId(loginUser.id())
                    .orderDate(request.getOrderDate())
                    .dueDate(request.getDueDate())
                    .totalAmount(totalAmount)
                    .build();
            // 매 시도마다 새 라인 인스턴스 부착(이전 시도 엔티티 재사용 금지)
            for (ResolvedLine rl : resolvedLines) {
                po.addLine(PurchaseOrderLine.builder()
                        .itemId(rl.itemId())
                        .quantity(rl.quantity())
                        .unitPrice(rl.unitPrice())
                        .build());
            }

            try {
                return purchaseOrderPersistenceService.saveAndFlush(po);
            } catch (DataIntegrityViolationException e) {
                // 실제 발주번호 충돌만 재시도한다. 다른 제약 위반은 원인을 숨기지 않는다.
                if (!purchaseOrderRepository.existsByOrderNumber(orderNumber)) {
                    throw e;
                }
            }
        }
        throw new BusinessException(ErrorCode.INTERNAL_ERROR,
                "발주번호 채번에 실패했습니다. 잠시 후 다시 시도해 주세요.");
    }

    /** 라인을 검증하고 적용 단가를 정한다. 라인 금액은 엔티티 빌더가 서버에서 계산한다. */
    private ResolvedLine resolveLine(PurchaseOrderCreateRequest.LineRequest lr) {
        if (lr.getItemId() == null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "품목은 필수입니다.");
        }
        if (lr.getQuantity() == null || lr.getQuantity() <= 0) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "수량은 0보다 커야 합니다.");
        }
        Item item = itemRepository.findById(lr.getItemId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ITEM_NOT_FOUND));
        if (!item.isActive()) {
            throw new BusinessException(ErrorCode.ITEM_DISCONTINUED);
        }

        BigDecimal unitPrice = lr.getUnitPrice();
        if (unitPrice == null) {
            unitPrice = item.getUnitPrice(); // 요청에서 생략하면 품목의 표준단가를 사용한다.
        } else if (unitPrice.signum() < 0) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "단가는 0 이상이어야 합니다.");
        }
        validateMoney(unitPrice, "단가");
        return new ResolvedLine(item.getId(), lr.getQuantity(), unitPrice);
    }

    private BigDecimal calculateLineAmount(ResolvedLine line) {
        BigDecimal lineAmount = line.unitPrice().multiply(BigDecimal.valueOf(line.quantity()));
        validateMoney(lineAmount, "라인 금액");
        return lineAmount;
    }

    private void validateMoney(BigDecimal value, String fieldName) {
        if (value.scale() > 2 || value.compareTo(MAX_MONEY) > 0) {
            throw new BusinessException(ErrorCode.INVALID_INPUT,
                    fieldName + "은 정수 13자리, 소수 2자리 이하여야 합니다.");
        }
    }

    /** 검증·해소가 끝난 라인 데이터 (채번 재시도 간 재사용). */
    private record ResolvedLine(Long itemId, Integer quantity, BigDecimal unitPrice) {
    }

    // ====================================================================
    // 모듈 11: 작성자의 결재 요청 (DRAFT → REQUESTED)
    // ====================================================================

    /** 임시 저장 발주를 작성자 본인이 결재 요청 상태로 바꾼다. */
    @Transactional
    public PurchaseOrderStatusResponse submit(Long poId, LoginUser loginUser) {
        Authz.requireLogin(loginUser);
        PurchaseOrder po = getEntity(poId);
        requireWriter(po, loginUser);
        po.submit();
        return PurchaseOrderStatusResponse.of(po, "결재 요청되었습니다.");
    }

    // ====================================================================
    // 모듈 11: ADMIN/MANAGER의 승인, 반려, 입고
    // ====================================================================

    /** T3: approve (REQUESTED → APPROVED), ADMIN/MANAGER. 동시 승인은 @Version 으로 방지. */
    @Transactional
    public PurchaseOrderStatusResponse approve(Long poId, LoginUser loginUser) {
        Authz.requireRole(loginUser, UserRole.ADMIN, UserRole.MANAGER);
        PurchaseOrder po = getEntity(poId);
        po.approve(loginUser.id());
        return PurchaseOrderStatusResponse.of(po, "발주가 승인되었습니다.");
    }

    /** 결재 요청을 반려한다. ADMIN/MANAGER만 가능하며 사유와 처리자 ID를 기록한다. */
    @Transactional
    public PurchaseOrderStatusResponse reject(Long poId, String rejectReason, LoginUser loginUser) {
        Authz.requireRole(loginUser, UserRole.ADMIN, UserRole.MANAGER);
        if (rejectReason == null || rejectReason.isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "반려 사유는 필수입니다.");
        }
        String normalizedReason = rejectReason.trim();
        if (normalizedReason.length() > REJECT_REASON_MAX_LENGTH) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "반려 사유는 500자 이하여야 합니다.");
        }
        PurchaseOrder po = getEntity(poId);
        po.reject(loginUser.id(), normalizedReason);
        return PurchaseOrderStatusResponse.of(po, "발주가 반려되었습니다.");
    }

    /**
     * 승인된 발주를 입고 완료로 바꾼다. ADMIN/MANAGER만 실행할 수 있다.
     * 상태 변경과 라인별 재고 증가는 같은 트랜잭션이므로 일부가 실패하면 모두 롤백된다.
     */
    @Transactional
    public PurchaseOrderStatusResponse receive(Long poId, LoginUser loginUser) {
        Authz.requireRole(loginUser, UserRole.ADMIN, UserRole.MANAGER);
        PurchaseOrder po = purchaseOrderRepository.findByIdWithLines(poId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PURCHASE_ORDER_NOT_FOUND));

        po.receive(); // 상태 검증(APPROVED) 포함

        // 같은 품목의 재고 행을 서로 다른 발주에서 동시에 최초 생성하면 UNIQUE 충돌이 난다.
        // 품목 행을 ID 순서로 잠가 최초 생성과 증가를 품목별로 직렬화한다.
        List<Long> itemIds = po.getLines().stream()
                .map(PurchaseOrderLine::getItemId)
                .distinct()
                .sorted()
                .toList();
        itemRepository.findAllByIdForUpdate(itemIds);

        for (PurchaseOrderLine line : po.getLines()) {
            Stock stock = stockRepository.findByItemId(line.getItemId())
                    .orElseGet(() -> stockRepository.save(new Stock(line.getItemId(), 0)));
            stock.increase(line.getQuantity());
        }
        return PurchaseOrderStatusResponse.of(po, "입고 처리되었습니다.");
    }

    // ====================================================================
    // 모듈 29: 내 목록, 관리자 목록, 상세, 작성자 취소
    // ====================================================================

    /** 내 발주서 목록 (writerId, 선택 status). */
    @Transactional(readOnly = true)
    public Page<PurchaseOrderSummaryResponse> getMyOrders(LoginUser loginUser,
                                                          PurchaseOrderStatus status,
                                                          Pageable pageable) {
        Authz.requireLogin(loginUser);
        Page<PurchaseOrder> page = (status != null)
                ? purchaseOrderRepository.findByWriterIdAndStatus(loginUser.id(), status, pageable)
                : purchaseOrderRepository.findByWriterId(loginUser.id(), pageable);
        return toSummaryPage(page);
    }

    /** 관리자 발주 목록 (전체, 선택 status/partnerId). ADMIN/MANAGER. */
    @Transactional(readOnly = true)
    public Page<PurchaseOrderSummaryResponse> getAdminOrders(LoginUser loginUser,
                                                             PurchaseOrderStatus status,
                                                             Long partnerId,
                                                             Pageable pageable) {
        Authz.requireRole(loginUser, UserRole.ADMIN, UserRole.MANAGER);
        Page<PurchaseOrder> page = purchaseOrderRepository.findAll(
                PurchaseOrderSpecs.adminSearch(status, partnerId), pageable);
        return toSummaryPage(page);
    }

    /** 발주 상세를 조회한다. 작성자 본인 또는 ADMIN/MANAGER만 접근할 수 있다. */
    @Transactional(readOnly = true)
    public PurchaseOrderDetailResponse getDetail(Long poId, LoginUser loginUser) {
        Authz.requireLogin(loginUser);
        PurchaseOrder po = purchaseOrderRepository.findByIdWithLines(poId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PURCHASE_ORDER_NOT_FOUND));
        requireDetailAccess(po, loginUser);
        return toDetailResponse(po);
    }

    /** 발주 상세 + 버튼 플래그(Web 상세 화면). */
    @Transactional(readOnly = true)
    public PurchaseOrderDetailView getDetailView(Long poId, LoginUser loginUser) {
        PurchaseOrderDetailResponse detail = getDetail(poId, loginUser);
        boolean isWriter = detail.getWriterId() != null && detail.getWriterId().equals(loginUser.id());
        boolean isAdminOrManager = loginUser.role() == UserRole.ADMIN
                || loginUser.role() == UserRole.MANAGER;
        PurchaseOrderStatus status = detail.getStatus();

        boolean canSubmit = isWriter && status == PurchaseOrderStatus.DRAFT;
        boolean canCancel = isWriter && (status == PurchaseOrderStatus.DRAFT
                || status == PurchaseOrderStatus.REQUESTED
                || status == PurchaseOrderStatus.APPROVED);
        boolean canApproveReject = isAdminOrManager && status == PurchaseOrderStatus.REQUESTED;
        boolean canReceive = isAdminOrManager && status == PurchaseOrderStatus.APPROVED;

        return PurchaseOrderDetailView.builder()
                .order(detail)
                .canSubmit(canSubmit)
                .canCancel(canCancel)
                .canApproveReject(canApproveReject)
                .canReceive(canReceive)
                .build();
    }

    /** 진행 중인 발주를 작성자 본인이 취소한다. 종료 상태는 PurchaseOrder가 거부한다. */
    @Transactional
    public PurchaseOrderStatusResponse cancel(Long poId, LoginUser loginUser) {
        Authz.requireLogin(loginUser);
        PurchaseOrder po = getEntity(poId);
        requireWriter(po, loginUser);
        po.cancel();
        return PurchaseOrderStatusResponse.of(po, "발주가 취소되었습니다.");
    }

    // ====================================================================
    // 드롭다운 옵션 (Web 폼/필터)
    // ====================================================================

    /** 유형이 SUPPLIER/BOTH이면서 ACTIVE인 거래처를 선택 옵션으로 반환한다. */
    @Transactional(readOnly = true)
    public List<PartnerOption> getSupplierOptions() {
        return partnerRepository.findAll().stream()
                .filter(Partner::canSupply)
                .filter(Partner::isActive)
                .map(PartnerOption::from)
                .toList();
    }

    /** 품목 옵션 (ACTIVE). 작성 폼 라인 셀렉트. */
    @Transactional(readOnly = true)
    public List<ItemOption> getItemOptions() {
        return itemRepository.findByStatusOrderByItemCodeAsc(ItemStatus.ACTIVE).stream()
                .map(ItemOption::from)
                .toList();
    }

    // ====================================================================
    // 대시보드 집계
    // ====================================================================

    /** 발주 대기(REQUESTED) 전체 수. */
    @Transactional(readOnly = true)
    public long countPending() {
        return purchaseOrderRepository.countByStatus(PurchaseOrderStatus.REQUESTED);
    }

    /** 현재 사용자가 작성한 발주 수. */
    @Transactional(readOnly = true)
    public long countMyOrders(Long writerId) {
        return purchaseOrderRepository.countByWriterId(writerId);
    }

    // ====================================================================
    // 내부 헬퍼
    // ====================================================================

    private PurchaseOrder getEntity(Long poId) {
        return purchaseOrderRepository.findById(poId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PURCHASE_ORDER_NOT_FOUND));
    }

    private void requireWriter(PurchaseOrder po, LoginUser loginUser) {
        if (!po.isWriter(loginUser.id())) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }
    }

    private void requireDetailAccess(PurchaseOrder po, LoginUser loginUser) {
        boolean isWriter = po.isWriter(loginUser.id());
        boolean isAdminOrManager = loginUser.role() == UserRole.ADMIN
                || loginUser.role() == UserRole.MANAGER;
        if (!isWriter && !isAdminOrManager) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }
    }

    /** 목록 → Summary 변환 (partnerName 일괄 resolve). */
    private Page<PurchaseOrderSummaryResponse> toSummaryPage(Page<PurchaseOrder> page) {
        // 거래처를 발주 건마다 조회하지 않고 필요한 ID를 모아 한 번에 조회한다.
        Set<Long> partnerIds = page.getContent().stream()
                .map(PurchaseOrder::getPartnerId)
                .collect(Collectors.toCollection(HashSet::new));
        Map<Long, String> partnerNames = partnerRepository.findAllById(partnerIds).stream()
                .collect(Collectors.toMap(Partner::getId, Partner::getName));
        return page.map(po ->
                PurchaseOrderSummaryResponse.from(po, partnerNames.get(po.getPartnerId())));
    }

    /** 상세 → DetailResponse 변환 (partner/user/item 표시값 resolve). */
    private PurchaseOrderDetailResponse toDetailResponse(PurchaseOrder po) {
        String partnerName = partnerRepository.findById(po.getPartnerId())
                .map(Partner::getName).orElse(null);
        String writerName = userRepository.findById(po.getWriterId())
                .map(User::getName).orElse(null);
        String approverName = (po.getApproverId() != null)
                ? userRepository.findById(po.getApproverId()).map(User::getName).orElse(null)
                : null;

        // 라인별 품목명도 같은 방식으로 일괄 조회해 반복 쿼리를 피한다.
        List<PurchaseOrderLine> lines = po.getLines();
        Set<Long> itemIds = lines.stream()
                .map(PurchaseOrderLine::getItemId)
                .collect(Collectors.toCollection(HashSet::new));
        Map<Long, Item> itemMap = itemRepository.findAllById(itemIds).stream()
                .collect(Collectors.toMap(Item::getId, Function.identity()));

        List<PurchaseOrderDetailResponse.LineResponse> lineResponses = lines.stream()
                .map(line -> {
                    Item item = itemMap.get(line.getItemId());
                    return PurchaseOrderDetailResponse.LineResponse.builder()
                            .lineId(line.getId())
                            .itemId(line.getItemId())
                            .itemCode(item != null ? item.getItemCode() : null)
                            .itemName(item != null ? item.getName() : null)
                            .quantity(line.getQuantity())
                            .unitPrice(line.getUnitPrice())
                            .lineAmount(line.getLineAmount())
                            .build();
                })
                .toList();

        return PurchaseOrderDetailResponse.of(po, partnerName, writerName, approverName, lineResponses);
    }
}
