// 실제 구현 위치 예: src/main/java/com/example/scm/service/PurchaseOrderService.java (my/detail/cancel/adminList)
// 목표: 내 발주 목록 / 상세 / 취소 + 관리자 목록 분기 조회를 채우세요. PRD 2.5.5 참고.

// ============ 내 발주 목록 ============
@Transactional(readOnly = true)
public Page<PurchaseOrderResponse> myList(Long currentUserId, Pageable pageable) {
    // TODO 01: 본인 작성 발주만 조회. URL 에 writerId 받지 않고 인증 사용자 기반.
    return purchaseOrderRepository.findBy____(currentUserId, pageable)
        .map(PurchaseOrderResponse::from);
}

// ============ 발주 상세 (본인 또는 ADMIN) ============
@Transactional(readOnly = true)
public PurchaseOrderResponse getDetail(Long currentUserId, Long purchaseOrderId) {
    User user = userRepository.findById(currentUserId)
        .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

    PurchaseOrder po = purchaseOrderRepository.findById(purchaseOrderId)
        .orElseThrow(() -> new BusinessException(ErrorCode.PURCHASE_ORDER_NOT_FOUND));

    // TODO 02: 권한 분기 — 작성자이거나 ADMIN 이어야 함.
    boolean isOwner = po.getWriterId().equals(user.getId());
    boolean isAdmin = (user.getRole() == UserRole.____);
    if (!isOwner && !isAdmin) {
        throw new BusinessException(ErrorCode.ACCESS_DENIED);
    }

    return PurchaseOrderResponse.from(po);
}

// ============ 발주 취소 (본인) ============
@Transactional
public void cancel(Long currentUserId, Long purchaseOrderId) {
    PurchaseOrder po = purchaseOrderRepository.findById(purchaseOrderId)
        .orElseThrow(() -> new BusinessException(ErrorCode.PURCHASE_ORDER_NOT_FOUND));

    try {
        // TODO 03: 도메인 메서드 안에서 작성자 + DRAFT/REQUESTED 검증.
        po.____(currentUserId);
    } catch (AccessDeniedException e) {
        throw new BusinessException(ErrorCode.ACCESS_DENIED, e.getMessage());
    } catch (IllegalStateException e) {
        throw new BusinessException(ErrorCode.INVALID_STATUS, e.getMessage());
    }
}

// ============ 관리자 발주 목록 (FR-PO-009) ============
@Transactional(readOnly = true)
public Page<PurchaseOrderResponse> adminList(
        PurchaseOrderStatus status,
        Long partnerId,
        Pageable pageable
) {
    // TODO 04: status / partnerId 조합 4가지를 분기.
    Page<PurchaseOrder> page;
    if (status != null && partnerId != null) {
        page = purchaseOrderRepository.findByStatusAndPartner_Id(status, partnerId, pageable);
    } else if (status != null) {
        page = purchaseOrderRepository.findByStatus(status, pageable);
    } else if (partnerId != null) {
        page = purchaseOrderRepository.findByPartner_Id(partnerId, pageable);
    } else {
        page = purchaseOrderRepository.____(pageable);
    }
    return page.map(PurchaseOrderResponse::from);
}

// 학습 질문:
// Q1. URL 에 writerId 를 받지 않고 currentUserId 로 조회하는 이유는?
//     A:
// Q2. 조건 분기를 4갈래 if-else 로 두는 게 부담스러우면 어떤 대안이 있는가?
//     A:
// Q3. 본인 + ADMIN 분기 권한 검사 코드를 헬퍼로 추출한다면 어떤 시그니처가 좋을까?
//     A:
