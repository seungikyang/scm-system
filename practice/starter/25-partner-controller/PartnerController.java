// 거래처 Controller / 카테고리 Controller + DTO (TRD 3.7.2, 3.7.3) · 4지 선다
// 브라우저에서 풀기: ../../sw-quiz/index.html?bank=workbook&module=25
// 각 문제에서 1~4번 중 하나를 고릅니다. 정답·해설 보기: practice/answers.md

// 선택형 [W107] PreAuthorize와 Service 가드를 함께 설계할 때 올바른 설명은?
//    1. 메서드 보안 활성화가 필요하며 Service 업무 인가도 별도로 검토한다
//    2. 어노테이션 문구만 쓰면 현재 프로젝트에 무조건 작동한다
//    3. Service 가드가 있으면 어떤 경로도 인증 없이 공개한다
//    4. 두 검사는 항상 모든 코드에서 동일하게 중복해야 한다
//    정답·해설: practice/workbook-questions.md#w107
//    정답: 1번 · 현재 구현은 Service 가드 중심이며 대안의 PreAuthorize에는 EnableMethodSecurity 등이 필요합니다.

// 선택형 [W108] 전체 수정과 부분 수정의 HTTP 메서드 조합은?
//    1. GET / TRACE
//    2. OPTIONS / HEAD
//    3. PUT / PATCH
//    4. DELETE / CONNECT
//    정답·해설: practice/workbook-questions.md#w108
//    정답: 3번 · PUT은 전체 표현 교체, PATCH는 부분 변경 계약에 사용합니다.

// 선택형 [W109] 거래처 상세에 주문 통계를 포함할지 판단할 기준은?
//    1. 통계가 있으면 인증을 제거한다
//    2. 항상 모든 주문 엔티티를 그대로 직렬화한다
//    3. 통계 계산이 DB unique를 대체한다
//    4. 화면 요구와 조회 비용·응답 크기를 함께 고려한다
//    정답·해설: practice/workbook-questions.md#w109
//    정답: 4번 · 통계를 같은 응답에 넣거나 별도 API로 나누는 선택을 명시합니다.

// 선택형 [W110] 소규모 카테고리 목록에 자연스러운 응답과 확장 방향은?
//    1. 언제나 비밀번호 배열을 반환한다
//    2. 카테고리 목록은 무조건 void만 반환한다
//    3. List로 시작하되 규모가 커지면 Page 계약을 고려한다
//    4. 페이징을 쓰면 카테고리를 저장할 수 없다
//    정답·해설: practice/workbook-questions.md#w110
//    정답: 3번 · 목록 규모와 사용자 경험에 맞춰 반환 계약을 정합니다.

// 선택형 [W111] 카테고리 상세에 모든 품목을 중첩할 때 고려할 것은?
//    1. 응답 크기와 N+1, 필요하면 별도 품목 페이징이다
//    2. JSON이면 조회 비용이 없다
//    3. nested DTO면 SQL이 자동 최적화된다
//    4. 모든 품목의 비밀번호를 포함한다
//    정답·해설: practice/workbook-questions.md#w111
//    정답: 1번 · 응답 구조와 조회 계획은 함께 검토해야 합니다.

// 선택형 [W112] CATEGORY_HAS_ITEMS의 현재 HTTP 응답은?
//    1. 200 OK
//    2. 400 Bad Request
//    3. 401 Unauthorized
//    4. 500 Internal Server Error
//    정답·해설: practice/workbook-questions.md#w112
//    정답: 2번 · 현재 ErrorCode는 업무 삭제 금지 규칙을 400으로 응답합니다.

// 선택형 [W113] 카테고리 변경·조회 권한의 현재 정책은?
//    1. 변경은 ADMIN, 조회는 로그인 사용자에게 허용한다
//    2. 변경은 USER만, 조회는 비로그인만 허용한다
//    3. 모든 변경을 공개한다
//    4. MANAGER만 모든 카테고리를 조회한다
//    정답·해설: practice/workbook-questions.md#w113
//    정답: 1번 · 조회와 변경은 서로 다른 권한 범위를 갖습니다.

// 선택형 [W251] 거래처 상세 경로 /api/partners/{id}의 PathVariable은?
//    1. 조회할 거래처 ID
//    2. 사용자 권한 문자열
//    3. 공지 조회수
//    4. 페이지 총 개수
//    정답·해설: practice/workbook-questions.md#w251
//    정답: 1번 · 조회 대상 ID를 받아 존재 여부와 접근 조건을 확인합니다.

// 선택형 [W278] ADMIN이 현재 거래처 삭제 API로 비활성화를 성공하면 응답과 상태는?
//    1. 204 / INACTIVE
//    2. 200 / ACTIVE
//    3. 403 / INACTIVE
//    4. 201 / DRAFT
//    정답·해설: practice/workbook-questions.md#w278
//    정답: 1번 · PartnerApiController는 noContent, Service는 비활성화 상태 변경을 수행합니다.
