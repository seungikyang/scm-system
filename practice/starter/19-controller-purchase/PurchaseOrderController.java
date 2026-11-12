// PurchaseOrderController (TRD 3.7.5) · 4지 선다
// 브라우저에서 풀기: ../../sw-quiz/index.html?bank=workbook&module=19
// 각 문제에서 1~4번 중 하나를 고릅니다. 정답·해설 보기: practice/answers.md

// 선택형 [W080] 일반 발주 API와 관리자 API 경로를 나누어도 필요한 것은?
//    1. 경로 이름만 있으면 누구나 접근해도 된다
//    2. 관리자 경로에서 실제 역할 인가를 수행한다
//    3. 관리자 API는 세션을 제거한다
//    4. 모든 API에 같은 작성자 ID를 저장한다
//    정답·해설: practice/workbook-questions.md#w080
//    정답: 2번 · 경로 분리는 계약을 명확히 하지만 실행 권한은 서버 검사로 보장합니다.

// 선택형 [W081] approve 동작을 별도 PATCH 경로로 표현할 때의 특징은?
//    1. PATCH이면 DB 상태가 자동 검증된다
//    2. PATCH는 응답 본문을 가질 수 없다
//    3. 업무 명령을 명확히 표현하지만 상태 전이·중복 요청 정책이 필요하다
//    4. URL만으로 낙관적 락이 생성된다
//    정답·해설: practice/workbook-questions.md#w081
//    정답: 3번 · 행위 중심 경로의 편의와 상태·멱등 정책을 함께 설계합니다.

// 선택형 [W082] 현재 프로젝트에서 로그인 사용자 정보를 받는 방식은?
//    1. CurrentUser ArgumentResolver가 LoginUser를 주입한다
//    2. 요청 본문의 userId를 인증 결과로 신뢰한다
//    3. Thymeleaf가 DB에서 비밀번호를 공개한다
//    4. Principal을 사용하면 항상 현재 세션 구현과 동일하다
//    정답·해설: practice/workbook-questions.md#w082
//    정답: 1번 · 현재 인증 구조는 세션과 LoginUser·CurrentUser이며 다른 인증 방식의 인자와 구분해야 합니다.

// 선택형 [W147] CurrentUser ArgumentResolver의 역할은?
//    1. 클라이언트의 임의 역할을 인증 없이 승인한다
//    2. 모든 엔티티를 JSON으로 저장한다
//    3. 비밀번호를 브라우저 주소에 붙인다
//    4. 세션 로그인 정보를 컨트롤러 파라미터 값으로 해석한다
//    정답·해설: practice/workbook-questions.md#w147
//    정답: 4번 · supportsParameter와 resolveArgument로 지원 파라미터를 찾고 LoginUser를 제공합니다.

// 선택형 [W221] 주문 라인 최소 개수 규칙의 입력 1차 방어는?
//    1. CSS margin 설정
//    2. DTO의 Size(min=1)와 중첩 입력 검증
//    3. RequestParam의 기본 포트 변경
//    4. 조회수 내림차순
//    정답·해설: practice/workbook-questions.md#w221
//    정답: 2번 · 입력 경계에서 검증하고 Service·도메인에서도 필요한 불변식을 보장합니다.

// 선택형 [W388] API 발주 반려 요청의 사유를 JSON DTO로 읽는 컨트롤러 인자 애너테이션은?
//    1. @RequestBody
//    2. @PathVariable
//    3. @RequestHeader
//    4. @CookieValue
//    정답·해설: practice/workbook-questions.md#w388
//    정답: 1번 · 반려 사유 DTO는 JSON 본문에서 읽고 @Valid 등으로 입력을 검증합니다. 발주 ID는 별도의 경로 변수입니다.
