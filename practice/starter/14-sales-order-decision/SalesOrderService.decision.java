// 수주 출고/완료/취소 Service (TRD 3.8.5, 3.10.4) · 4지 선다
// 브라우저에서 풀기: ../../sw-quiz/index.html?bank=workbook&module=14
// 각 문제에서 1~4번 중 하나를 고릅니다. 정답·해설 보기: practice/answers.md

// 선택형 [W031] 수주의 정상 처리 상태 흐름은?
//    1. DRAFT → CONFIRMED → SHIPPED → COMPLETED
//    2. DRAFT → RECEIVED → REQUESTED → COMPLETED
//    3. SHIPPED → DRAFT → APPROVED
//    4. COMPLETED → CONFIRMED → DRAFT
//    정답·해설: practice/workbook-questions.md#w031
//    정답: 1번 · 취소 가능 상태는 별도로 관리하며 COMPLETED·CANCELED는 종료 상태입니다.

// 선택형 [W064] 수주 출고·완료 상태 전이의 올바른 규칙은?
//    1. DRAFT에서 바로 완료한다
//    2. COMPLETED에서 다시 출고한다
//    3. CANCELED에서 재고를 차감한다
//    4. CONFIRMED에서 출고하고 SHIPPED에서 완료한다
//    정답·해설: practice/workbook-questions.md#w064
//    정답: 4번 · 종료 상태 추가 전이는 차단하고 취소 가능 상태를 따로 검증합니다.

// 선택형 [W065] 수주 처리자 검증의 올바른 기준은?
//    1. 클라이언트가 보낸 managerId만 신뢰한다
//    2. 현재 인증 사용자의 역할과 해당 수주의 접근 정책을 확인한다
//    3. 매니저 문자열이 요청 본문에 있으면 허용한다
//    4. 주문 ID가 짝수면 권한을 부여한다
//    정답·해설: practice/workbook-questions.md#w065
//    정답: 2번 · 역할 검증과 소유권·담당 정책은 서버에서 인증 사용자 기준으로 적용합니다.

// 선택형 [W066] shippedAt과 completedAt을 설정하는 시점은?
//    1. 각각 출고·완료 상태 전이가 성공할 때
//    2. 목록 조회마다 둘 다 갱신한다
//    3. 수주 생성 시 둘 다 완료 시각으로 기록한다
//    4. 로그인 실패 시 기록한다
//    정답·해설: practice/workbook-questions.md#w066
//    정답: 1번 · 업무 이벤트 발생 시각은 그 상태 변경과 같은 작업에서 기록합니다.

// 선택형 [W067] 수주 취소 사유 정책은?
//    1. 모든 취소는 사유 없이 처리한다
//    2. 사유가 있으면 완료 상태도 무조건 취소한다
//    3. 공백이 아닌 사유를 요구한다
//    4. 사유를 비밀번호 필드에 저장한다
//    정답·해설: practice/workbook-questions.md#w067
//    정답: 3번 · 사유 검증과 취소 가능한 상태 검증을 모두 수행합니다.

// 선택형 [W189] 현재 수주 취소의 올바른 정책은?
//    1. 비로그인 누구나 완료 수주를 취소한다
//    2. 작성자 또는 ADMIN/MANAGER가 허용 상태에서 사유를 입력한다
//    3. 사유를 넣으면 모든 종료 상태도 취소한다
//    4. USER가 URL ID만 바꾸면 타인 수주 취소가 가능하다
//    정답·해설: practice/workbook-questions.md#w189
//    정답: 2번 · DRAFT·CONFIRMED·SHIPPED에서만 취소하며 출고 후 취소는 재고를 복원합니다.

// 선택형 [W259] 현재 수주 출고·완료 처리가 허용되는 역할은?
//    1. USER만
//    2. 비로그인만
//    3. SUPPLIER 또는 CUSTOMER
//    4. ADMIN 또는 MANAGER
//    정답·해설: practice/workbook-questions.md#w259
//    정답: 4번 · 역할 검증 뒤 허용 상태·재고 등 업무 조건도 검사합니다.

// 선택형 [W363] 수주 출고 시 재고 차감을 처리하는 올바른 업무 경계는?
//    1. 서버에서 출고 상태 변경과 모든 라인 재고 차감을 같은 트랜잭션으로 묶는다
//    2. 출고 상태만 먼저 커밋하고 재고 실패를 무시한다
//    3. 브라우저 계산값을 재고로 그대로 저장한다
//    4. 재고 부족이어도 항상 출고를 완료한다
//    정답·해설: practice/workbook-questions.md#w363
//    정답: 1번 · 현재 Service는 출고 상태·품목 잠금·재고 차감을 조정합니다. 한 라인 실패 시 부분 출고가 남지 않게 합니다.
