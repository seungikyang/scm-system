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
 * 발주(PurchaseOrder) 비즈니스 로직. 상태머신 T1~T8 + 권한 + 검증 + 채번 + 입고 재고 증가.
 * (02_architect_datamodel §5~7, 02_architect_contracts §1/§4/§5, analyst 7.1 확정 우선)
 */
@Service
@RequiredArgsConstructor
public class PurchaseOrderService {

    private static final int ORDER_NUMBER_RETRY = 5;

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final StockRepository stockRepository;
    private final PartnerRepository partnerRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final OrderNumberGenerator orderNumberGenerator;

    // ====================================================================
    // 작성 (T1, REQ-PO-001) — 헤더+라인 cascade 동시 저장, 서버 계산, 채번
    // ====================================================================

    @Transactional
    public PurchaseOrderCreateResponse create(PurchaseOrderCreateRequest request, LoginUser loginUser) {
        Authz.requireLogin(loginUser);

        // 1) 라인 존재 검증
        List<PurchaseOrderCreateRequest.LineRequest> lineReqs = request.getLines();
        if (lineReqs == null || lineReqs.isEmpty()) {
            throw new BusinessException(ErrorCode.EMPTY_ORDER_LINES);
        }

        // 2) 공급사 검증: 존재 / SUPPLIER|BOTH / ACTIVE
        Partner partner = partnerRepository.findById(request.getPartnerId())
                .orElseThrow(() -> new BusinessException(ErrorCode.PARTNER_NOT_FOUND));
        if (!partner.canSupply()) {
            throw new BusinessException(ErrorCode.PARTNER_TYPE_MISMATCH);
        }
        if (!partner.isActive()) {
            throw new BusinessException(ErrorCode.INVALID_STATUS,
                    "비활성 상태의 거래처로는 발주할 수 없습니다."); // OQ-12
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
        // 라인 검증 + 단가 결정(OQ-13) — 채번/재시도와 무관하므로 1회만 수행.
        List<ResolvedLine> resolvedLines = lineReqs.stream().map(this::resolveLine).toList();
        BigDecimal totalAmount = resolvedLines.stream()
                .map(rl -> rl.unitPrice().multiply(BigDecimal.valueOf(rl.quantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add); // totalAmount 서버 계산 (OQ-1)

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
                return purchaseOrderRepository.saveAndFlush(po); // flush 로 UNIQUE 충돌 즉시 감지
            } catch (DataIntegrityViolationException e) {
                // 채번 충돌 가능성 → 재시도 (다음 루프에서 재채번)
            }
        }
        throw new BusinessException(ErrorCode.INTERNAL_ERROR,
                "발주번호 채번에 실패했습니다. 잠시 후 다시 시도해 주세요.");
    }

    /** 라인 검증 + 단가 결정(OQ-13). lineAmount 는 엔티티 빌더가 서버 계산(OQ-1). */
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
            unitPrice = item.getUnitPrice(); // OQ-13: 누락 시 품목 표준단가
        } else if (unitPrice.signum() < 0) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "단가는 0 이상이어야 합니다.");
        }
        return new ResolvedLine(item.getId(), lr.getQuantity(), unitPrice);
    }

    /** 검증·해소가 끝난 라인 데이터 (채번 재시도 간 재사용). */
    private record ResolvedLine(Long itemId, Integer quantity, BigDecimal unitPrice) {
    }

    // ====================================================================
    // 상태 전이 (작성자 본인: submit/cancel)
    // ====================================================================

    /** T2: submit (DRAFT → REQUESTED), 작성자 본인. */
    @Transactional
    public PurchaseOrderStatusResponse submit(Long poId, LoginUser loginUser) {
        Authz.requireLogin(loginUser);
        PurchaseOrder po = getEntity(poId);
        requireWriter(po, loginUser);
        po.submit();
        return PurchaseOrderStatusResponse.of(po, "결재 요청되었습니다.");
    }

    /** T6~T8: cancel ({DRAFT,REQUESTED,APPROVED} → CANCELED), 작성자 본인. */
    @Transactional
    public PurchaseOrderStatusResponse cancel(Long poId, LoginUser loginUser) {
        Authz.requireLogin(loginUser);
        PurchaseOrder po = getEntity(poId);
        requireWriter(po, loginUser);
        po.cancel();
        return PurchaseOrderStatusResponse.of(po, "발주가 취소되었습니다.");
    }

    // ====================================================================
    // 상태 전이 (ADMIN+MANAGER: approve/reject/receive)
    // ====================================================================

    /** T3: approve (REQUESTED → APPROVED), ADMIN/MANAGER. 동시 승인은 @Version 으로 방지. */
    @Transactional
    public PurchaseOrderStatusResponse approve(Long poId, LoginUser loginUser) {
        Authz.requireRole(loginUser, UserRole.ADMIN, UserRole.MANAGER);
        PurchaseOrder po = getEntity(poId);
        po.approve(loginUser.id());
        return PurchaseOrderStatusResponse.of(po, "발주가 승인되었습니다.");
    }

    /** T4: reject (REQUESTED → REJECTED), ADMIN/MANAGER. rejectReason 필수, approverId 기록(OQ-11). */
    @Transactional
    public PurchaseOrderStatusResponse reject(Long poId, String rejectReason, LoginUser loginUser) {
        Authz.requireRole(loginUser, UserRole.ADMIN, UserRole.MANAGER);
        if (rejectReason == null || rejectReason.isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "반려 사유는 필수입니다.");
        }
        PurchaseOrder po = getEntity(poId);
        po.reject(loginUser.id(), rejectReason);
        return PurchaseOrderStatusResponse.of(po, "발주가 반려되었습니다.");
    }

    /**
     * T5: receive (APPROVED → RECEIVED), ADMIN/MANAGER.
     * 상태전이 + 라인별 Stock 증가가 동일 트랜잭션(원자성, OQ-4). 일부 실패 시 전체 롤백.
     */
    @Transactional
    public PurchaseOrderStatusResponse receive(Long poId, LoginUser loginUser) {
        Authz.requireRole(loginUser, UserRole.ADMIN, UserRole.MANAGER);
        PurchaseOrder po = purchaseOrderRepository.findByIdWithLines(poId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PURCHASE_ORDER_NOT_FOUND));

        po.receive(); // 상태 검증(APPROVED) 포함

        for (PurchaseOrderLine line : po.getLines()) {
            Stock stock = stockRepository.findByItemId(line.getItemId())
                    .orElseGet(() -> stockRepository.save(new Stock(line.getItemId(), 0)));
            stock.increase(line.getQuantity()); // 재고 증가 (OQ-4)
        }
        return PurchaseOrderStatusResponse.of(po, "입고 처리되었습니다.");
    }

    // ====================================================================
    // 조회
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

    /** 발주 상세 (작성자 본인 또는 ADMIN/MANAGER, OQ-6). */
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

    // ====================================================================
    // 드롭다운 옵션 (Web 폼/필터)
    // ====================================================================

    /** 공급사 옵션 (SUPPLIER|BOTH & ACTIVE). 작성 폼 / 관리자 거래처 필터. */
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
