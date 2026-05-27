// 실제 구현 위치 예: src/main/java/com/example/scm/service/ItemService.java (search 부분)
// 목표: 품목 검색 + 페이징을 채우세요. TRD 3.7.4, PRD 2.5.4 참고.

// TODO 01: 조회 전용 트랜잭션 옵션은?
@Transactional(readOnly = ____)
public Page<ItemResponse> search(String keyword, Long categoryId, Pageable pageable) {

    String trimmed = (keyword == null) ? "" : keyword.trim();

    Page<Item> page;

    if (trimmed.isBlank() && categoryId == null) {
        // 조건 없음 → 전체
        page = itemRepository.findAll(pageable);

    } else if (!trimmed.isBlank() && categoryId == null) {
        // TODO 02: 이름/품목코드 contains 검색.
        page = itemRepository.findByNameContainingOrItemCodeContaining(
                trimmed, trimmed, ____
        );

    } else if (trimmed.isBlank() && categoryId != null) {
        // TODO 03: 카테고리만 조건.
        page = itemRepository.findByCategory_Id(categoryId, ____);

    } else {
        // 둘 다 있는 경우: @Query 또는 Specification 활용 (학습용 빈칸)
        // TODO 04: 어떤 메서드 시그니처로 보낼까요? 한 줄로 적어 보세요.
        // A:
        page = itemRepository.findAll(pageable);
    }

    // TODO 05: Entity 페이지를 응답 DTO 페이지로 변환.
    return page.____(ItemResponse::from);
}

// 학습 질문:
// Q1. readOnly = true 가 가져다 주는 이점은?
//     A:
// Q2. 정렬 컬럼을 클라이언트가 마음대로 지정하게 두면 어떤 위험이 있는가?
//     A:
// Q3. Page.map vs new PageImpl 의 차이는?
//     A:
