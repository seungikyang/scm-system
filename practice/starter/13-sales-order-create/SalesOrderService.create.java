// 실제 구현 위치 예: src/main/java/com/example/scm/service/SalesOrderService.java (create + confirm)
// 목표: 수주서 작성 / 확정 흐름을 채우세요. TRD 3.8.5 참고.

// ============ 수주서 작성 (DRAFT) ============
// TODO 01: 헤더 + 라인 트랜잭션 보장 어노테이션.
@____
public SalesOrderResponse create(Long currentUserId, SalesOrderCreateRequest request) {

    User writer = userRepository.findById(currentUserId)
        .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

    Partner partner = partnerRepository.findById(request.getPartnerId())
        .orElseThrow(() -> new BusinessException(ErrorCode.PARTNER_NOT_FOUND));

    // TODO 02: 거래처가 수주 가능한 유형인지 확인 (CUSTOMER 또는 BOTH).
    if (partner.getPartnerType() != PartnerType.____
            && partner.getPartnerType() != PartnerType.BOTH) {
        throw new BusinessException(ErrorCode.PARTNER_TYPE_MISMATCH);
    }

    // TODO 03: 라인 검증.
    if (request.getLines() == null || request.getLines().isEmpty()) {
        throw new BusinessException(ErrorCode.____);
    }

    String orderNumber = orderNumberGenerator.next("SO", LocalDate.now());

    SalesOrder so = SalesOrder.create(
        orderNumber,
        partner,
        writer.getId(),
        request.getOrderDate(),
        request.getShipDate()
    );

    for (SalesOrderCreateRequest.LineDto lineDto : request.getLines()) {
        Item item = itemRepository.findById(lineDto.getItemId())
            .orElseThrow(() -> new BusinessException(ErrorCode.ITEM_NOT_FOUND));

        // TODO 04: DRAFT 단계에서는 단종 품목을 허용할지 막을지 도메인 정책을 적어 보세요.
        // A:
        if (item.getStatus() == ItemStatus.DISCONTINUED) {
            throw new BusinessException(ErrorCode.ITEM_DISCONTINUED);
        }

        so.addLine(item, lineDto.getQuantity(), lineDto.getUnitPrice());
    }

    return SalesOrderResponse.from(salesOrderRepository.save(so));
}

// ============ 수주 확정 (DRAFT → CONFIRMED) ============
@Transactional
public SalesOrderResponse confirm(Long currentUserId, Long salesOrderId) {
    SalesOrder so = salesOrderRepository.findById(salesOrderId)
        .orElseThrow(() -> new BusinessException(ErrorCode.SALES_ORDER_NOT_FOUND));

    // TODO 05: 도메인 메서드 confirm() 안에서 작성자/상태 검증이 모두 일어나는 구조.
    try {
        so.____(currentUserId);
    } catch (IllegalStateException e) {
        throw new BusinessException(ErrorCode.INVALID_STATUS, e.getMessage());
    } catch (AccessDeniedException e) {
        throw new BusinessException(ErrorCode.____, e.getMessage());
    }

    return SalesOrderResponse.from(so);
}

// 학습 질문:
// Q1. 작성(POST)과 확정(PATCH /confirm) 을 분리한 이유는?
//     A:
// Q2. 거래처 유형이 SUPPLIER 인 거래처로 수주를 작성하면 어떻게 막아야 하는가?
//     A:
// Q3. 도메인 메서드 confirm() 안의 검증 vs Service 의 검증, 어디까지를 어디서 책임지나?
//     A:
