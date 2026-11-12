// Repository 메서드 쿼리 (TRD 3.7.4, 3.8.2) · 4지 선다
// 브라우저에서 풀기: ../../sw-quiz/index.html?bank=workbook&module=07
// 각 문제에서 1~4번 중 하나를 고릅니다. 정답·해설 보기: practice/answers.md

// 선택형 [W035] existsByItemCode와 findByItemCode의 차이는?
//    1. 앞은 존재 여부, 뒤는 품목 조회를 표현한다
//    2. 둘 다 모든 주문을 삭제한다
//    3. exists는 페이지 목록을 반환한다
//    4. find는 HTTP 경로를 등록한다
//    정답·해설: practice/workbook-questions.md#w035
//    정답: 1번 · Spring Data는 메서드 이름의 접두사와 속성 조건으로 쿼리를 구성합니다.

// 선택형 [W036] findByNameContainingOrItemCodeContaining의 조건 의미는?
//    1. 이름과 코드가 모두 숫자여야 한다
//    2. 두 조건을 항상 AND로 결합한다
//    3. 품목을 코드 순서로 자동 삭제한다
//    4. 이름 또는 코드가 주어진 문자열을 포함한다
//    정답·해설: practice/workbook-questions.md#w036
//    정답: 4번 · Or는 OR 결합이고 Containing은 부분 문자열 검색을 표현합니다.

// 선택형 [W037] Pageable을 받는 Repository의 자연스러운 반환 타입은?
//    1. HttpSession
//    2. PasswordEncoder
//    3. void만 가능
//    4. Page<Item>
//    정답·해설: practice/workbook-questions.md#w037
//    정답: 4번 · Page는 데이터와 총 개수·페이지 수 같은 메타 정보를 제공합니다.

// 선택형 [W038] 카테고리 ID로 품목을 제한하는 Repository 조건은?
//    1. category.id를 기준으로 조회 조건을 만든다
//    2. 품목 이름을 사용자 비밀번호와 비교한다
//    3. 항상 전체 품목을 반환한다
//    4. 카테고리 ID를 HTTP 포트로 쓴다
//    정답·해설: practice/workbook-questions.md#w038
//    정답: 1번 · findByCategoryId 같은 속성 경로 조건 또는 Specification을 사용합니다.

// 선택형 [W136] 복잡해진 메서드 이름 쿼리를 정리하는 기준은?
//    1. 메서드 이름을 무한히 늘린다
//    2. Controller에 SQL 문자열을 모두 붙인다
//    3. 가독성·조합 조건·조인 요구에 따라 Query나 Specification을 선택한다
//    4. 조회 조건을 문서에서 삭제한다
//    정답·해설: practice/workbook-questions.md#w136
//    정답: 3번 · 명시 쿼리와 동적 조건 추상화의 목적에 맞게 선택합니다.

// 선택형 [W229] ItemRepository의 직접 구현 클래스가 없어도 호출 가능한 이유는?
//    1. 브라우저가 Java 인터페이스를 실행한다
//    2. Spring Data가 Repository 프록시 구현을 제공한다
//    3. JPA가 HTML을 컴파일한다
//    4. 인터페이스는 항상 구현 없이 JVM에서 실행된다
//    정답·해설: practice/workbook-questions.md#w229
//    정답: 2번 · 컨테이너가 등록한 런타임 프록시가 쿼리 메서드를 처리합니다.

// 선택형 [W252] 카테고리와 keyword를 함께 제한하는 품목 검색의 결합은?
//    1. 두 조건을 무조건 OR로 결합
//    2. 카테고리 조건 AND keyword 조건
//    3. 카테고리 조건을 항상 무시
//    4. keyword를 비밀번호로 저장
//    정답·해설: practice/workbook-questions.md#w252
//    정답: 2번 · 동일 카테고리 안에서 이름·코드 조건을 검색하려면 범위 조건과 검색 조건을 AND로 묶습니다.

// 선택형 [W273] Specification 검색 조건과 페이징을 함께 전달하는 호출은?
//    1. findAll(passwordEncoder, session)
//    2. deleteAll(pageable)
//    3. findById(cssColor)
//    4. findAll(specification, pageable)
//    정답·해설: practice/workbook-questions.md#w273
//    정답: 4번 · JpaSpecificationExecutor의 조건과 Pageable을 함께 사용해 필터된 페이지를 조회합니다.
