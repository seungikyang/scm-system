// Repository — User / Partner / Category / PurchaseOrder / Notice / SalesOrder (TRD 3.5) · 4지 선다
// 브라우저에서 풀기: ../../sw-quiz/index.html?bank=workbook&module=31
// 각 문제에서 1~4번 중 하나를 고릅니다. 정답·해설 보기: practice/answers.md

// 선택형 [W029] 중요 공지를 먼저, 같은 중요도에서 최신 공지를 보여 주는 정렬은?
//    1. important ASC, createdAt ASC
//    2. important DESC, createdAt DESC
//    3. viewCount ASC만 적용
//    4. 제목 길이만 오름차순
//    정답·해설: practice/workbook-questions.md#w029
//    정답: 2번 · 참조 구현은 중요도·생성시각·ID 내림차순으로 순서를 안정화합니다.

// 선택형 [W035] existsByItemCode와 findByItemCode의 차이는?
//    1. 앞은 존재 여부, 뒤는 품목 조회를 표현한다
//    2. 둘 다 모든 주문을 삭제한다
//    3. exists는 페이지 목록을 반환한다
//    4. find는 HTTP 경로를 등록한다
//    정답·해설: practice/workbook-questions.md#w035
//    정답: 1번 · Spring Data는 메서드 이름의 접두사와 속성 조건으로 쿼리를 구성합니다.

// 선택형 [W049] 동시 발주번호 채번의 안전한 전략은?
//    1. count + 1만 읽으면 항상 유일하다
//    2. 사용자 이름만 발주번호로 쓴다
//    3. 같은 번호를 허용하고 나중에 주문을 삭제한다
//    4. DB unique와 충돌 재시도 등 동시성 대책을 결합한다
//    정답·해설: practice/workbook-questions.md#w049
//    정답: 4번 · 참조 구현은 최대 번호와 unique 충돌의 독립 트랜잭션 재시도를 사용합니다.

// 선택형 [W133] findByPartner_BusinessNumber의 속성 경로 의미는?
//    1. HTTP 쿠키 이름 조건이다
//    2. 연관된 partner의 businessNumber 조건이다
//    3. 테이블 이름을 BusinessNumber로 변경한다
//    4. 모든 파트너를 삭제한다
//    정답·해설: practice/workbook-questions.md#w133
//    정답: 2번 · 중첩 속성 탐색 구분자를 사용해 연관관계의 속성을 조회합니다.

// 선택형 [W134] JPQL orderDate BETWEEN :from AND :to의 일반적 경계는?
//    1. 양 끝을 모두 제외한다
//    2. 날짜와 무관하게 모든 행을 삭제한다
//    3. from만 사용하고 to는 무시한다
//    4. 양 끝 날짜를 포함하는 범위 조건이다
//    정답·해설: practice/workbook-questions.md#w134
//    정답: 4번 · 날짜 범위 조건과 from≤to 검증을 함께 적용합니다.

// 선택형 [W135] 조회수 원자적 증가 쿼리의 핵심은?
//    1. DB에서 viewCount = viewCount + 1을 직접 실행한다
//    2. 조회한 값을 브라우저에서 1 더해 덮어쓴다
//    3. 항상 viewCount=1로 저장한다
//    4. 공지 ID를 조회수로 바꾼다
//    정답·해설: practice/workbook-questions.md#w135
//    정답: 1번 · Modifying Query와 트랜잭션에서 DB가 증가 연산을 수행해 유실을 줄입니다.

// 선택형 [W136] 복잡해진 메서드 이름 쿼리를 정리하는 기준은?
//    1. 메서드 이름을 무한히 늘린다
//    2. Controller에 SQL 문자열을 모두 붙인다
//    3. 가독성·조합 조건·조인 요구에 따라 Query나 Specification을 선택한다
//    4. 조회 조건을 문서에서 삭제한다
//    정답·해설: practice/workbook-questions.md#w136
//    정답: 3번 · 명시 쿼리와 동적 조건 추상화의 목적에 맞게 선택합니다.

// 선택형 [W230] 입고 이력이 없는 품목도 재고 화면에 표시하려면?
//    1. Item을 기준으로 Stock을 LEFT JOIN하고 없는 재고를 0으로 처리한다
//    2. Stock 테이블만 조회한다
//    3. INNER JOIN만 사용한다
//    4. 입고 이력이 없으면 품목을 삭제한다
//    정답·해설: practice/workbook-questions.md#w230
//    정답: 1번 · 재고 행이 없는 품목을 누락하지 않도록 품목 중심 조회와 null 처리로 구성합니다.

// 선택형 [W354] 파생 쿼리 메서드의 속성과 DB 컬럼 이름의 관계는?
//    1. DB 컬럼 이름만을 Java 메서드로 무조건 그대로 파싱한다
//    2. 컬럼명 변경은 HTTP 경로만 변경한다
//    3. 엔티티 속성을 기준으로 파싱하며 Column 매핑이 DB 이름에 연결한다
//    4. 속성명을 바꿔도 파생 쿼리는 항상 자동 수정된다
//    정답·해설: practice/workbook-questions.md#w354
//    정답: 3번 · 속성명을 바꾸면 메서드 경로를 함께 고쳐야 하며 DB 컬럼만 변경하는 경우 매핑을 대조합니다.

// 선택형 [W355] 연관관계가 있는 모델의 findByPartner_Id 같은 조회는?
//    1. 항상 모든 엔티티를 메모리에 조회한다
//    2. partner.id 속성 경로를 조건으로 해석하며 실제 SQL은 매핑·최적화에 따라 결정된다
//    3. 어떤 경우에도 SQL JOIN이 발생하지 않는다
//    4. 무조건 세션 쿠키를 검색한다
//    정답·해설: practice/workbook-questions.md#w355
//    정답: 2번 · 속성 경로와 실제 SQL JOIN 유무를 단정하지 말고 매핑과 쿼리 로그를 확인합니다.
