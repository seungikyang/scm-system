// 실제 구현 위치 예: src/main/java/com/example/scm/service/ItemService.java (detail/update/discontinue)
// 목표: 품목 상세/수정/단종 처리를 채우세요. PRD 2.5.4 참고.

// ============ 상세 ============
@Transactional(readOnly = true)
public ItemResponse getDetail(Long itemId) {
    Item item = itemRepository.findById(itemId)
        .orElseThrow(() -> new BusinessException(ErrorCode.____));
    // TODO 01: LAZY 로 묶인 Category 가 DTO 변환 시점에 접근됩니다.
    //          open-in-view 가 꺼져 있으면 어떻게 해야 할까요?
    //          (정답 후보: Service 트랜잭션 안에서 DTO 변환 / fetch join)
    return ItemResponse.from(item);
}

// ============ 수정 ============
@Transactional
public ItemResponse update(Long itemId, ItemUpdateRequest request) {
    Item item = itemRepository.findById(itemId)
        .orElseThrow(() -> new BusinessException(ErrorCode.ITEM_NOT_FOUND));

    // TODO 02: 카테고리 변경 요청이 들어왔을 때.
    if (request.getCategoryId() != null) {
        Category newCategory = categoryRepository.findById(request.getCategoryId())
            .orElseThrow(() -> new BusinessException(ErrorCode.____));
        item.changeCategory(newCategory);
    }

    // TODO 03: 도메인 메서드 호출 (setter 사용 금지).
    item.____(
        request.getName(),
        request.getUnit(),
        request.getUnitPrice(),
        request.getSafetyStock()
    );
    return ItemResponse.from(item);
}

// ============ 단종 처리 ============
@Transactional
public ItemResponse discontinue(Long itemId) {
    Item item = itemRepository.findById(itemId)
        .orElseThrow(() -> new BusinessException(ErrorCode.ITEM_NOT_FOUND));
    try {
        item.discontinue();
    } catch (IllegalStateException e) {
        // TODO 04: 이미 단종된 품목이면 어떻게 매핑할까?
        throw new BusinessException(ErrorCode.____, e.getMessage());
    }
    return ItemResponse.from(item);
}

// 학습 질문:
// Q1. setter 를 직접 호출하지 않고 도메인 메서드(updateProfile, changeCategory)를 만든 이유는?
//     A:
// Q2. DISCONTINUED 품목을 다시 ACTIVE 로 돌리는 정책을 둘지 결정해 보세요. (yes/no + 이유)
//     A:
// Q3. open-in-view 의 장단점은?
//     A:
