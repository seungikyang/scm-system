// 실제 구현 위치 예: src/main/java/com/example/scm/service/SalesOrderService.java (ship/complete/cancel)
// 목표: 수주 출고/완료/취소 상태 전이를 채우세요. TRD 3.8.5, 3.10.4 참고.

// ============ 출고 (CONFIRMED → SHIPPED) ============
@Transactional
public SalesOrderResponse ship(Long currentUserId, Long salesOrderId) {
    User manager = userRepository.findById(currentUserId)
        .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

    // TODO 01: MANAGER 또는 ADMIN 권한 확인.
    if (manager.getRole() != UserRole.MANAGER && manager.getRole() != UserRole.____) {
        throw new BusinessException(ErrorCode.ACCESS_DENIED);
    }

    SalesOrder so = salesOrderRepository.findById(salesOrderId)
        .orElseThrow(() -> new BusinessException(ErrorCode.SALES_ORDER_NOT_FOUND));

    try {
        // TODO 02: 도메인 메서드 ship() 호출 (managerId 함께 기록).
        so.____(manager.getId());
    } catch (IllegalStateException e) {
        throw new BusinessException(ErrorCode.INVALID_STATUS, e.getMessage());
    }

    // 확장: 라인별 재고 -quantity 차감 (Inventory 도메인 추가 시)
    // TODO 03: 출고 시 재고는 어디서 차감해야 할까?
    // A:

    return SalesOrderResponse.from(so);
}

// ============ 완료 (SHIPPED → COMPLETED) ============
@Transactional
public SalesOrderResponse complete(Long currentUserId, Long salesOrderId) {
    User manager = userRepository.findById(currentUserId)
        .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    if (manager.getRole() != UserRole.MANAGER && manager.getRole() != UserRole.ADMIN) {
        throw new BusinessException(ErrorCode.ACCESS_DENIED);
    }

    SalesOrder so = salesOrderRepository.findById(salesOrderId)
        .orElseThrow(() -> new BusinessException(ErrorCode.SALES_ORDER_NOT_FOUND));

    try {
        so.complete();
    } catch (IllegalStateException e) {
        throw new BusinessException(ErrorCode.____, e.getMessage());
    }

    return SalesOrderResponse.from(so);
}

// ============ 취소 ============
@Transactional
public SalesOrderResponse cancel(Long currentUserId, Long salesOrderId, String reason) {
    User user = userRepository.findById(currentUserId)
        .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

    SalesOrder so = salesOrderRepository.findById(salesOrderId)
        .orElseThrow(() -> new BusinessException(ErrorCode.SALES_ORDER_NOT_FOUND));

    // TODO 04: 매니저/관리자 또는 작성자 본인이 취소 가능하다고 가정. 누구 권한?
    boolean isManager  = (user.getRole() == UserRole.MANAGER || user.getRole() == UserRole.ADMIN);
    boolean isWriter   = so.getWriterId().equals(user.getId());
    if (!isManager && !isWriter) {
        throw new BusinessException(ErrorCode.____);
    }

    // TODO 05: 취소 사유 blank 검증.
    if (reason == null || reason.isBlank()) {
        throw new BusinessException(ErrorCode.____, "취소 사유는 필수입니다.");
    }

    try {
        so.cancel(reason);
    } catch (IllegalStateException e) {
        throw new BusinessException(ErrorCode.INVALID_STATUS, e.getMessage());
    }

    return SalesOrderResponse.from(so);
}

// 학습 질문:
// Q1. SHIPPED 상태가 아닌 수주를 complete() 시도하면 어디서 막히는가?
//     A:
// Q2. CANCELED 또는 COMPLETED 가 종료 상태인 이유는?
//     A:
// Q3. 권한 표(매니저/관리자/작성자) 를 도식으로 그릴 수 있는가?
//     A:
