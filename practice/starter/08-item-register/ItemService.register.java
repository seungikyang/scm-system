// 실제 구현 위치 예: src/main/java/com/example/scm/service/ItemService.java
// 목표: 품목 등록 Service 를 채우세요. TRD 3.7.4, 3.8.2 참고.

// TODO 01: 이 메서드가 트랜잭션 안에서 실행되도록 선언하세요.
@____
public ItemResponse register(ItemCreateRequest request) {

    String itemCode = request.getItemCode().trim();

    // TODO 02: 품목코드 중복 검사. 어떤 ErrorCode 를 던질까요?
    if (itemRepository.existsByItemCode(itemCode)) {
        throw new BusinessException(ErrorCode.____);
    }

    // TODO 03: 카테고리가 존재하는지 확인.
    Category category = categoryRepository.findById(request.getCategoryId())
        .orElseThrow(() -> new BusinessException(ErrorCode.____));

    // TODO 04: 단가가 0 이상인지 검증. (BigDecimal 비교)
    if (request.getUnitPrice().compareTo(BigDecimal.ZERO) ____ 0) {
        throw new BusinessException(ErrorCode.INVALID_INPUT);
    }

    // TODO 05: 정적 팩토리로 엔티티 생성.
    Item item = Item.____(
        category,
        itemCode,
        request.getName(),
        request.getUnit(),
        request.getUnitPrice(),
        request.getSafetyStock()
    );

    // TODO 06: 저장.
    Item saved = itemRepository.____(item);

    // TODO 07: Entity 를 그대로 반환하지 말고 응답 DTO 로 변환하세요. 이유는?
    return ItemResponse.____(saved);
}

// 학습 질문:
// Q1. 이 메서드에서 @Transactional 이 빠지면 어떤 데이터 정합성 문제가 생기는가?
//     A:
// Q2. 중복 검사를 Service 에서 했는데도 DB unique 제약을 두는 이유는?
//     A:
// Q3. ItemCreateRequest 에서 @Valid 가 빠지면 어떤 일이 일어나는가?
//     A:
