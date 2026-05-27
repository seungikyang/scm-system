// 실제 구현 위치 예: src/main/java/com/example/scm/repository/ItemRepository.java
// 목표: Item 검색/페이징 Repository 메서드를 채우세요. TRD 3.7.4, 3.8.2 참고.

public interface ItemRepository extends JpaRepository<Item, Long> {

    // TODO 01: 품목코드 중복 검사. 어떤 메서드 이름 규칙을 따를까요?
    boolean ____ByItemCode(String itemCode);

    // TODO 02: 품목코드로 단건 조회.
    Optional<Item> findBy____(String itemCode);

    // TODO 03: 특정 카테고리에 속한 품목을 페이지로 조회.
    Page<Item> findByCategory_Id(Long categoryId, ____ pageable);

    // TODO 04: 카테고리 삭제 정책: 소속 품목 수를 셀 메서드.
    long countBy____(Long categoryId);

    // TODO 05: 이름 또는 품목코드에 keyword 가 포함된 품목 페이징.
    Page<Item> findByNameContainingOrItemCodeContaining(
            String nameKeyword,
            String codeKeyword,
            ____ pageable
    );

    // TODO 06: 상태 필터링 (ACTIVE / DISCONTINUED) 페이징.
    Page<Item> findByStatus(ItemStatus status, Pageable pageable);

    // 동적 조건이 더 늘어나면 어디로 옮길까요?
    // A: ____  (예: Querydsl / Specification / @Query)
}

// 학습 질문:
// Q1. existsByXxx 와 findByXxx.isPresent() 의 차이는?
//     A:
// Q2. 메서드 이름 쿼리가 4단어를 넘어가면 어떤 대안을 쓰는가?
//     A:
// Q3. 단순 페이징 시그니처에 필요한 파라미터는?
//     A:
