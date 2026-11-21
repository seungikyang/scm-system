// SalesOrder 엔티티와 라인 (TRD 3.3.8, 3.6.5) · 4지 선다
// 브라우저에서 풀기: ../../sw-quiz/index.html?bank=workbook&module=06
// 각 문제에서 1~4번 중 하나를 고릅니다. 정답·해설 보기: practice/answers.md

// 선택형 [W031] 수주의 정상 처리 상태 흐름은?
//    1. DRAFT → CONFIRMED → SHIPPED → COMPLETED
//    2. DRAFT → RECEIVED → REQUESTED → COMPLETED
//    3. SHIPPED → DRAFT → APPROVED
//    4. COMPLETED → CONFIRMED → DRAFT
//    정답·해설: practice/workbook-questions.md#w031
//    정답: 1번 · 취소 가능 상태는 별도로 관리하며 COMPLETED·CANCELED는 종료 상태입니다.

// 선택형 [W032] 작성자와 처리자 ID가 같을 수 있는지 판단할 때의 기준은?
//    1. 필드가 둘이면 JPA가 자동으로 동일인을 금지한다
//    2. 같은 ID면 무조건 관리자 권한이 생긴다
//    3. 처리자 필드가 있으면 소유권 검사를 생략한다
//    4. 역할·소유권 정책을 명시하며 ID 분리만으로 동일인을 금지하지 않는다
//    정답·해설: practice/workbook-questions.md#w032
//    정답: 4번 · 행위자 동일 여부는 명시적인 업무 정책이며 기술 매핑에서 자동으로 결정되지 않습니다.

// 선택형 [W033] shippedAt과 completedAt이 초기에는 null일 수 있는 이유는?
//    1. 모든 날짜 필드는 항상 null이어야 하기 때문이다
//    2. 작성자 비밀번호를 숨기기 때문이다
//    3. 아직 출고·완료 이벤트가 발생하지 않았기 때문이다
//    4. 재고 부족을 표현하는 필드이기 때문이다
//    정답·해설: practice/workbook-questions.md#w033
//    정답: 3번 · 상태 전이 시 해당 이벤트 시각을 함께 설정합니다.

// 선택형 [W034] 수주 라인을 헤더 생명주기에 묶는 설계는?
//    1. 각 라인을 사용자 세션에만 저장한다
//    2. 헤더의 라인 관계와 cascade·삭제 정책을 명시한다
//    3. 품목 마스터까지 헤더 삭제에 연결한다
//    4. 라인을 저장할 때마다 별도 수주 헤더를 생성한다
//    정답·해설: practice/workbook-questions.md#w034
//    정답: 2번 · 라인은 주문 구성 요소이며 품목·거래처 마스터와 같은 삭제 정책을 쓰지 않습니다.

// 선택형 [W064] 수주 출고·완료 상태 전이의 올바른 규칙은?
//    1. DRAFT에서 바로 완료한다
//    2. COMPLETED에서 다시 출고한다
//    3. CANCELED에서 재고를 차감한다
//    4. CONFIRMED에서 출고하고 SHIPPED에서 완료한다
//    정답·해설: practice/workbook-questions.md#w064
//    정답: 4번 · 종료 상태 추가 전이는 차단하고 취소 가능 상태를 따로 검증합니다.

// 선택형 [W180] 새 수주의 초기 상태는?
//    1. COMPLETED
//    2. DRAFT
//    3. SHIPPED
//    4. CANCELED
//    정답·해설: practice/workbook-questions.md#w180
//    정답: 2번 · 작성 후 confirm을 통해 CONFIRMED로 전이합니다.

// 선택형 [W256] 수주 확정의 정상 상태 전이는?
//    1. CONFIRMED → SHIPPED
//    2. SHIPPED → COMPLETED
//    3. DRAFT → CONFIRMED
//    4. COMPLETED → DRAFT
//    정답·해설: practice/workbook-questions.md#w256
//    정답: 3번 · 확정과 출고·완료는 별도 명령과 검증 경계를 갖습니다.
