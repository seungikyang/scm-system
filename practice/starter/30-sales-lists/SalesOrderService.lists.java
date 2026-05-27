// 실제 구현 위치 예: src/main/java/com/example/scm/service/SalesOrderService.java (my/pending/detail)
// 목표: 수주 my / pending / detail 권한 검사를 채우세요. PRD 2.5.7, FR-SO-003/004/005 참고.

// ============ 내 수주서 목록 ============
@Transactional(readOnly = true)
public Page<SalesOrderResponse> myList(Long currentUserId, Pageable pageable) {
    return salesOrderRepository.findByWriterId(currentUserId, pageable)
        .map(SalesOrderResponse::from);
}

// ============ 처리 대기 수주서 (매니저/관리자) ============
@Transactional(readOnly = true)
public Page<SalesOrderResponse> pendingList(Pageable pageable) {
    // TODO 01: "처리 대기" 는 어떤 상태인가?
    return salesOrderRepository.findByStatus(SalesOrderStatus.____, pageable)
        .map(SalesOrderResponse::from);
}

// ============ 수주서 상세 (권한 분기) ============
@Transactional(readOnly = true)
public SalesOrderResponse getDetail(Long currentUserId, Long salesOrderId) {
    User user = userRepository.findById(currentUserId)
        .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

    SalesOrder so = salesOrderRepository.findById(salesOrderId)
        .orElseThrow(() -> new BusinessException(ErrorCode.SALES_ORDER_NOT_FOUND));

    // TODO 02: 권한 분기 — 작성자, 처리자, ADMIN 만.
    boolean isWriter  = so.getWriterId().equals(user.getId());
    boolean isManager = so.getManagerId() != null && so.getManagerId().equals(user.getId());
    boolean isAdmin   = (user.getRole() == UserRole.____);
    if (!isWriter && !isManager && !isAdmin) {
        throw new BusinessException(ErrorCode.____);
    }

    return SalesOrderResponse.from(so);
}

// ============ (선택) 관리자 전체 목록 ============
@Transactional(readOnly = true)
public Page<SalesOrderResponse> adminList(SalesOrderStatus status, Long partnerId, Pageable pageable) {
    // TODO 03: status / partnerId 조합 분기.
    Page<SalesOrder> page;
    if (status != null && partnerId != null) {
        page = salesOrderRepository.findByStatusAndPartner_Id(status, partnerId, pageable);
    } else if (status != null) {
        page = salesOrderRepository.findByStatus(status, pageable);
    } else if (partnerId != null) {
        page = salesOrderRepository.findByPartner_Id(partnerId, pageable);
    } else {
        page = salesOrderRepository.findAll(pageable);
    }
    return page.map(SalesOrderResponse::from);
}

// 학습 질문:
// Q1. managerId 가 아직 비어 있는 (CONFIRMED) 수주는 누구의 pending 으로 보일까?
//     A:
// Q2. 매니저 권한 위임(managerId 변경) 시 기존 위임자의 pending 목록은 어떻게 되는가?
//     A:
// Q3. 권한 검사 코드를 매번 작성하면 중복이 많아진다. 어디로 추출할까?
//     A:
