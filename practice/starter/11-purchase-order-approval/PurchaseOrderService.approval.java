// 발주 승인/반려 Service (TRD 3.8.3, 3.10.2) · 4지 선다
// 브라우저에서 풀기: ../../sw-quiz/index.html?bank=workbook&module=11
// 각 문제에서 1~4번 중 하나를 고릅니다. 정답·해설 보기: practice/answers.md

// 선택형 [W051] REQUESTED가 아닌 발주의 승인·반려를 막는 설계는?
//    1. 도메인 전이 메서드에 상태 가드를 둔다
//    2. 화면 버튼만 숨긴다
//    3. 승인 후 작성자 이메일을 바꾼다
//    4. DTO 필드 이름을 바꾸면 충분하다
//    정답·해설: practice/workbook-questions.md#w051
//    정답: 1번 · Service 인가와 별도로 도메인 메서드가 상태 불변식을 보호합니다.

// 선택형 [W052] 반려 사유가 공백이면 어떻게 처리하는가?
//    1. 자동으로 승인한다
//    2. RECEIVED로 바꾼다
//    3. 검증 오류로 거절하고 상태를 변경하지 않는다
//    4. 공백을 그대로 저장해 반려한다
//    정답·해설: practice/workbook-questions.md#w052
//    정답: 3번 · 필수 사유는 DTO 및 도메인·Service 검증으로 보장합니다.

// 선택형 [W053] approvedAt과 approverId는 언제 함께 기록하는가?
//    1. 발주 목록 조회마다
//    2. 로그아웃할 때
//    3. 유효한 승인 상태 전이가 성공할 때
//    4. 프로그램 종료할 때
//    정답·해설: practice/workbook-questions.md#w053
//    정답: 3번 · 상태와 행위자·시각이 같은 업무 처리 안에서 일관되게 변경되어야 합니다.

// 선택형 [W054] 발주 승인·반려·입고가 허용되는 역할은?
//    1. ADMIN 또는 MANAGER
//    2. USER만
//    3. 모든 비로그인 사용자
//    4. 거래처 유형이 CUSTOMER인 사용자만
//    정답·해설: practice/workbook-questions.md#w054
//    정답: 1번 · 현재 참조 구현은 ADMIN과 MANAGER를 허용하며 USER는 거절합니다.

// 선택형 [W055] 입고 상태 변경과 재고 증가를 같은 트랜잭션으로 묶는 이유는?
//    1. 상태만 성공하고 재고 실패는 무시하기 위해서다
//    2. 조회수를 증가시키기 위해서다
//    3. 권한을 USER로 바꾸기 위해서다
//    4. 둘 중 하나가 실패하면 전체를 되돌리기 위해서다
//    정답·해설: practice/workbook-questions.md#w055
//    정답: 4번 · 입고 이벤트와 모든 재고 반영은 원자적인 업무 작업입니다.

// 선택형 [W056] 같은 발주를 두 관리자가 동시에 승인하면 필요한 대응은?
//    1. 둘 다 승인 이력을 덮어써도 무방하다
//    2. 버전 충돌을 감지해 중복 상태 변경을 차단한다
//    3. 인터넷 속도가 빠르면 충돌이 없다
//    4. 각 요청이 다른 DB를 초기화한다
//    정답·해설: practice/workbook-questions.md#w056
//    정답: 2번 · PurchaseOrder의 Version 낙관적 락이 경쟁 상태 변경을 감지합니다.

// 선택형 [W107] PreAuthorize와 Service 가드를 함께 설계할 때 올바른 설명은?
//    1. 메서드 보안 활성화가 필요하며 Service 업무 인가도 별도로 검토한다
//    2. 어노테이션 문구만 쓰면 현재 프로젝트에 무조건 작동한다
//    3. Service 가드가 있으면 어떤 경로도 인증 없이 공개한다
//    4. 두 검사는 항상 모든 코드에서 동일하게 중복해야 한다
//    정답·해설: practice/workbook-questions.md#w107
//    정답: 1번 · 현재 구현은 Service 가드 중심이며 대안의 PreAuthorize에는 EnableMethodSecurity 등이 필요합니다.

// 선택형 [W164] 도메인 IllegalStateException을 API ErrorCode로 바꾸는 위치는?
//    1. Entity가 HttpServletResponse를 직접 조작한다
//    2. CSS에서 HTTP status를 바꾼다
//    3. Service 등 명시한 경계에서 일관된 업무 예외로 변환한다
//    4. 브라우저가 DB 예외를 그대로 출력한다
//    정답·해설: practice/workbook-questions.md#w164
//    정답: 3번 · 현재 Service는 도메인 상태 실패를 INVALID_STATUS 등 안정적인 계약으로 변환합니다.

// 선택형 [W182] 발주 입고의 정상 전이는?
//    1. DRAFT → RECEIVED
//    2. REQUESTED → APPROVED
//    3. APPROVED → RECEIVED
//    4. CANCELED → RECEIVED
//    정답·해설: practice/workbook-questions.md#w182
//    정답: 3번 · 입고는 승인된 발주만 가능하며 재고 증가와 같은 트랜잭션에서 처리합니다.

// 선택형 [W183] 발주 입고가 성공하면 재고는 어떻게 변하는가?
//    1. 각 라인 수량만큼 항상 감소한다
//    2. 현재고가 무조건 0이 된다
//    3. 안전재고가 자동 삭제된다
//    4. 각 라인의 입고 수량만큼 증가한다
//    정답·해설: practice/workbook-questions.md#w183
//    정답: 4번 · StockService가 입고 수량을 반영하며 중복 입고와 경쟁 요청을 방어합니다.

// 선택형 [W184] 경쟁 상태 변경을 다루는 두 가지 DB 잠금 방식은?
//    1. 낙관적 락과 비관적 락
//    2. HTML 락과 CSS 락
//    3. 세션 글꼴과 북마크
//    4. 문자열 escape와 URL encoding
//    정답·해설: practice/workbook-questions.md#w184
//    정답: 1번 · 버전 충돌 감지와 DB 잠금은 서로 다른 전략이며 경쟁 수준과 업무 요구로 선택합니다.

// 선택형 [W225] 세 번째 입고 라인에서 RuntimeException이 발생하면 기대 결과는?
//    1. 앞선 두 재고만 증가하고 상태는 RECEIVED로 남는다
//    2. 예외가 발생한 라인만 자동 삭제된다
//    3. 모든 과거 발주까지 삭제된다
//    4. 발주 상태와 앞선 재고 변경까지 함께 롤백된다
//    정답·해설: practice/workbook-questions.md#w225
//    정답: 4번 · 입고 상태·재고 반영을 같은 트랜잭션으로 묶어 부분 반영을 막습니다.
