// Notice 엔티티 (TRD 3.3.7, 3.5.7) · 4지 선다
// 브라우저에서 풀기: ../../sw-quiz/index.html?bank=workbook&module=05
// 각 문제에서 1~4번 중 하나를 고릅니다. 정답·해설 보기: practice/answers.md

// 선택형 [W028] 공지 본문에 TEXT 계열 컬럼을 사용하는 이유는?
//    1. HTML을 자동으로 안전하게 정화하기 위해서다
//    2. 조회수 잠금을 자동 생성하기 위해서다
//    3. 공지 제목의 unique를 보장하기 위해서다
//    4. 긴 본문을 저장할 수 있는 컬럼 용량을 확보하기 위해서다
//    정답·해설: practice/workbook-questions.md#w028
//    정답: 4번 · 저장 용량과 XSS 방어는 별도로 고려해야 합니다.

// 선택형 [W029] 중요 공지를 먼저, 같은 중요도에서 최신 공지를 보여 주는 정렬은?
//    1. important ASC, createdAt ASC
//    2. important DESC, createdAt DESC
//    3. viewCount ASC만 적용
//    4. 제목 길이만 오름차순
//    정답·해설: practice/workbook-questions.md#w029
//    정답: 2번 · 참조 구현은 중요도·생성시각·ID 내림차순으로 순서를 안정화합니다.

// 선택형 [W030] 조회수의 읽기-수정-저장을 동시에 수행하면 생길 수 있는 문제는?
//    1. 모든 요청의 증가분이 자동으로 두 배가 된다
//    2. 조회가 항상 무한 루프가 된다
//    3. 여러 요청의 증가분이 덮어써져 유실될 수 있다
//    4. unique 제약이 조회수를 암호화한다
//    정답·해설: practice/workbook-questions.md#w030
//    정답: 3번 · 원자적 UPDATE view_count = view_count + 1로 증가분 유실을 방지할 수 있습니다.

// 선택형 [W135] 조회수 원자적 증가 쿼리의 핵심은?
//    1. DB에서 viewCount = viewCount + 1을 직접 실행한다
//    2. 조회한 값을 브라우저에서 1 더해 덮어쓴다
//    3. 항상 viewCount=1로 저장한다
//    4. 공지 ID를 조회수로 바꾼다
//    정답·해설: practice/workbook-questions.md#w135
//    정답: 1번 · Modifying Query와 트랜잭션에서 DB가 증가 연산을 수행해 유실을 줄입니다.

// 선택형 [W228] ItemService의 생성자 주입에 대한 설명은?
//    1. Repository·관련 Service 등 필요한 협력 객체를 외부에서 받는다
//    2. 호출 때마다 비밀번호를 HTTP URL에서 읽는다
//    3. 필요한 Repository를 모두 static 전역으로 직접 생성한다
//    4. final 필드는 Spring 주입을 금지한다
//    정답·해설: practice/workbook-questions.md#w228
//    정답: 1번 · RequiredArgsConstructor와 final 필드를 통해 필수 협력 객체를 주입합니다.

// 선택형 [W381] 새 공지를 생성할 때 조회수의 초기값은?
//    1. 1L
//    2. -1L
//    3. Long.MAX_VALUE
//    4. 0L
//    정답·해설: practice/workbook-questions.md#w381
//    정답: 4번 · 아직 조회되지 않은 공지는 조회수 0으로 시작합니다. 조회수의 원자적 증가는 별도 조회 처리 규칙입니다.
