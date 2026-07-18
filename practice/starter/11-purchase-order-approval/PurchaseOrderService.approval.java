// 실제 구현 위치 예: src/main/java/com/example/scm/service/PurchaseOrderService.java (approval)
// 목표: 발주 승인/반려/입고 상태 전이를 트랜잭션으로 처리하세요.
//       TRD 3.8.3, 3.10.2, 3.10.3 참고.

// ============ 발주 요청 (DRAFT → REQUESTED) ============
@Transactional
public void submit(Long currentUserId, Long purchaseOrderId) {
    PurchaseOrder po = purchaseOrderRepository.findById(purchaseOrderId)
        .orElseThrow(() -> new BusinessException(ErrorCode.____));

    // TODO 01: 작성자 본인 여부 검증.
    if (!po.getWriterId().equals(currentUserId)) {
        throw new BusinessException(ErrorCode.____);
    }

    // TODO 02: 도메인 메서드 호출 (내부에서 상태 전이 검증).
    po.____();
}

// ============ 발주 승인 (REQUESTED → APPROVED) ============
@Transactional
public PurchaseOrderResponse approve(Long currentUserId, Long purchaseOrderId) {
    // TODO 03: ADMIN 또는 MANAGER 권한 확인.
    User approver = userRepository.findById(currentUserId)
        .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    if (approver.getRole() != UserRole.____ && approver.getRole() != UserRole.____) {
        throw new BusinessException(ErrorCode.ACCESS_DENIED);
    }

    PurchaseOrder po = purchaseOrderRepository.findById(purchaseOrderId)
        .orElseThrow(() -> new BusinessException(ErrorCode.PURCHASE_ORDER_NOT_FOUND));

    // TODO 04: 상태 전이 시도. 잘못된 상태면 INVALID_STATUS 로 매핑.
    try {
        po.approve(approver.getId());
    } catch (IllegalStateException e) {
        throw new BusinessException(ErrorCode.____, e.getMessage());
    }

    return PurchaseOrderResponse.from(po);
}

// ============ 발주 반려 (REQUESTED → REJECTED) ============
@Transactional
public PurchaseOrderResponse reject(Long currentUserId, Long purchaseOrderId, String reason) {
    User approver = userRepository.findById(currentUserId)
        .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    if (approver.getRole() != UserRole.____ && approver.getRole() != UserRole.____) {
        throw new BusinessException(ErrorCode.ACCESS_DENIED);
    }

    PurchaseOrder po = purchaseOrderRepository.findById(purchaseOrderId)
        .orElseThrow(() -> new BusinessException(ErrorCode.PURCHASE_ORDER_NOT_FOUND));

    // TODO 05: 반려 사유 blank 검증 (DTO @NotBlank 와의 이중 방어).
    if (reason == null || reason.____) {
        throw new BusinessException(ErrorCode.INVALID_INPUT, "반려 사유는 필수입니다.");
    }

    try {
        po.reject(approver.getId(), reason);
    } catch (IllegalStateException e) {
        throw new BusinessException(ErrorCode.INVALID_STATUS, e.getMessage());
    }

    return PurchaseOrderResponse.from(po);
}

// ============ 입고 처리 (APPROVED → RECEIVED) ============
@Transactional
public PurchaseOrderResponse receive(Long currentUserId, Long purchaseOrderId) {
    User operator = userRepository.findById(currentUserId)
        .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    if (operator.getRole() != UserRole.____ && operator.getRole() != UserRole.____) {
        throw new BusinessException(ErrorCode.ACCESS_DENIED);
    }

    PurchaseOrder po = purchaseOrderRepository.findById(purchaseOrderId)
        .orElseThrow(() -> new BusinessException(ErrorCode.PURCHASE_ORDER_NOT_FOUND));

    try {
        // TODO 06: 도메인 메서드 호출.
        po.____();
    } catch (IllegalStateException e) {
        throw new BusinessException(ErrorCode.INVALID_STATUS, e.getMessage());
    }

    // TODO 07: 상태 전이와 같은 트랜잭션에서 라인별 재고를 증가시키세요.
    // 같은 품목의 최초 재고 생성 경합을 막기 위해 품목 ID를 정렬한 뒤 행 잠금을 잡습니다.
    List<Long> itemIds = po.getLines().stream()
        .map(line -> line.getItem().getId())
        .distinct()
        .sorted()
        .toList();
    itemRepository.____(itemIds);

    for (PurchaseOrderLine line : po.getLines()) {
        Long itemId = line.getItem().getId();
        Stock stock = stockRepository.findByItemId(itemId)
            .orElseGet(() -> stockRepository.save(new Stock(itemId, 0)));
        stock.____(line.getQuantity());
    }

    return PurchaseOrderResponse.from(po);
}

// 학습 질문:
// Q1. 같은 발주를 두 관리자가 동시에 승인하면 어떻게 막을까? (낙관적 락 / 비관적 락)
//     A:
// Q2. 도메인 IllegalStateException 을 BusinessException(INVALID_STATUS) 로 매핑하는 이유는?
//     A:
// Q3. 권한 검사를 Controller(@PreAuthorize)와 Service 양쪽에서 하는 이점은?
//     A:
// Q4. 상태를 RECEIVED로 바꾼 뒤 재고 증가 중 하나가 실패하면 왜 전체가 롤백되어야 하는가?
//     A:
