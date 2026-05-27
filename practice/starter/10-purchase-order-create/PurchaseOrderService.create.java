// 실제 구현 위치 예: src/main/java/com/example/scm/service/PurchaseOrderService.java (create)
// 목표: 발주서(헤더 + 라인)를 같은 트랜잭션으로 저장하세요.
//       TRD 3.7.5, 3.8.3, 3.10.1 참고.

// TODO 01: 헤더 + 라인 정합성을 위해 어떤 어노테이션?
@____
public PurchaseOrderResponse create(Long currentUserId, PurchaseOrderCreateRequest request) {

    // TODO 02: 작성자(사용자) 존재 확인.
    User writer = userRepository.findById(currentUserId)
        .orElseThrow(() -> new BusinessException(ErrorCode.____));

    // TODO 03: 거래처 조회.
    Partner partner = partnerRepository.findById(request.getPartnerId())
        .orElseThrow(() -> new BusinessException(ErrorCode.____));

    // TODO 04: 거래처가 발주 가능한 유형인지 확인 (SUPPLIER 또는 BOTH).
    if (partner.getPartnerType() != PartnerType.SUPPLIER
            && partner.getPartnerType() != PartnerType.____) {
        throw new BusinessException(ErrorCode.PARTNER_TYPE_MISMATCH);
    }
    if (partner.getStatus() != PartnerStatus.ACTIVE) {
        throw new BusinessException(ErrorCode.INVALID_STATUS);
    }

    // TODO 05: 라인이 비어 있으면?
    if (request.getLines() == null || request.getLines().isEmpty()) {
        throw new BusinessException(ErrorCode.____);
    }

    // TODO 06: 날짜 범위 검증 (orderDate <= dueDate).
    if (request.getDueDate() != null
            && request.getOrderDate().isAfter(request.getDueDate())) {
        throw new BusinessException(ErrorCode.____);
    }

    // 발주번호 채번 (예: PO-20260526-0001)
    String orderNumber = orderNumberGenerator.next("PO", LocalDate.now());

    PurchaseOrder po = PurchaseOrder.create(
        orderNumber,
        partner,
        writer.getId(),
        request.getOrderDate(),
        request.getDueDate()
    );

    // TODO 07: 라인 검증 + 추가.
    for (PurchaseOrderCreateRequest.LineDto lineDto : request.getLines()) {
        Item item = itemRepository.findById(lineDto.getItemId())
            .orElseThrow(() -> new BusinessException(ErrorCode.____));

        // TODO 08: 단종 품목 거부.
        if (item.getStatus() == ItemStatus.____) {
            throw new BusinessException(ErrorCode.ITEM_DISCONTINUED);
        }

        // TODO 09: 수량 검증.
        if (lineDto.getQuantity() == null || lineDto.getQuantity() ____ 0) {
            throw new BusinessException(ErrorCode.INVALID_INPUT);
        }

        po.addLine(item, lineDto.getQuantity(), lineDto.getUnitPrice());
    }

    // TODO 10: 헤더 + 라인 저장. cascade = ALL 이면 헤더만 save 해도 됩니다.
    PurchaseOrder saved = purchaseOrderRepository.____(po);

    return PurchaseOrderResponse.from(saved);
}

// 학습 질문:
// Q1. 라인 검증을 헤더 save 전에 끝내는 이유는?
//     A:
// Q2. 거래처 유형 검증을 화면이 아닌 Service 에서 해야 하는 이유는?
//     A:
// Q3. 발주번호 채번이 동시성 이슈가 있다면 어떻게 막을까?
//     A:
