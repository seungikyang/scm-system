// ErrorCode 와 응답 모델 (TRD 3.9) · 4지 선다
// 브라우저에서 풀기: ../../sw-quiz/index.html?bank=workbook&module=15
// 각 문제에서 1~4번 중 하나를 고릅니다. 정답·해설 보기: practice/answers.md

// 선택형 [W068] HTTP status와 비즈니스 ErrorCode의 관계는?
//    1. 둘 다 화면 글꼴을 지정한다
//    2. ErrorCode는 TCP 포트다
//    3. status가 400이면 모든 오류 원인이 같다
//    4. status는 HTTP 결과 분류, code는 구체적인 업무 오류를 나타낸다
//    정답·해설: practice/workbook-questions.md#w068
//    정답: 4번 · 동일한 HTTP 상태 안에도 중복·상태 오류 등 다른 업무 코드가 있습니다.

// 선택형 [W069] 공통 ErrorResponse의 기본 필드 조합은?
//    1. status·code·message·timestamp
//    2. password·salt·sessionId·token
//    3. HTML·CSS·font·color
//    4. writerId·unitPrice·categoryId만
//    정답·해설: practice/workbook-questions.md#w069
//    정답: 1번 · 공통 응답은 결과·오류 식별자·메시지·발생 시각으로 구성합니다.

// 선택형 [W070] 도메인별 오류를 enum으로 모으는 장점은?
//    1. 예외가 절대 발생하지 않는다
//    2. 코드·상태·기본 메시지의 일관성을 유지한다
//    3. 모든 오류를 200으로 바꾼다
//    4. DB unique 검사가 없어져도 된다
//    정답·해설: practice/workbook-questions.md#w070
//    정답: 2번 · Service와 예외 처리기가 같은 오류 계약을 공유합니다.

// 선택형 [W330] BusinessException을 RuntimeException으로 정의하는 효과는?
//    1. 예외가 전혀 발생하지 않게 된다
//    2. 모든 SQL 오류를 성공으로 바꾼다
//    3. 항상 스택 트레이스를 사용자에게 공개한다
//    4. 기본 트랜잭션 롤백 대상이 되며 호출부의 checked 예외 선언을 요구하지 않는다
//    정답·해설: practice/workbook-questions.md#w330
//    정답: 4번 · 업무 오류 계약을 예외 처리기로 전달하며 트랜잭션 기본 롤백 정책과 맞춥니다.

// 선택형 [W331] LocalDateTime을 서로 다른 시간대 시스템 사이에서 사용할 때 주의할 점은?
//    1. 시간대·오프셋이 없어 실제 시점을 해석하는 기준을 정해야 한다
//    2. UTC 정보가 항상 자동 포함된다
//    3. 모든 날짜가 같은 실제 시점을 뜻한다
//    4. JSON으로 보내면 시간대가 항상 자동 복원된다
//    정답·해설: practice/workbook-questions.md#w331
//    정답: 1번 · 이벤트의 절대 시점을 전달할 때 Instant·오프셋 또는 명시한 시간대 정책을 고려합니다.

// 선택형 [W384] 현재 ErrorResponse에서 오류 발생 시각을 담는 timestamp 필드의 Java 타입은?
//    1. LocalDate
//    2. Duration
//    3. LocalDateTime
//    4. Period
//    정답·해설: practice/workbook-questions.md#w384
//    정답: 3번 · ErrorResponse는 LocalDateTime.now()로 timestamp를 생성합니다. 시간대가 필요한 외부 시스템 연동에서는 별도의 시점 표현 정책을 정합니다.
