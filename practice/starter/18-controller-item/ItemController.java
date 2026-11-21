// ItemController (TRD 3.7.4) · 4지 선다
// 브라우저에서 풀기: ../../sw-quiz/index.html?bank=workbook&module=18
// 각 문제에서 1~4번 중 하나를 고릅니다. 정답·해설 보기: practice/answers.md

// 선택형 [W020] 단종 시 삭제 대신 DISCONTINUED를 사용하는 이유는?
//    1. 신규 주문에 단종 품목을 강제로 추가하기 위해서다
//    2. 과거 주문 라인의 참조와 거래 이력을 보존하기 위해서다
//    3. 재고를 무조건 0으로 만들기 위해서다
//    4. DB 백업 없이 테이블을 초기화하기 위해서다
//    정답·해설: practice/workbook-questions.md#w020
//    정답: 2번 · 단종은 신규 사용 제한이며 과거 데이터를 파괴하는 삭제와 다릅니다.

// 선택형 [W077] 품목 REST API의 기본 HTTP 메서드 조합은?
//    1. 조회 POST·등록 GET·수정 TRACE
//    2. 모든 기능을 GET만으로 처리
//    3. 모든 기능을 OPTIONS만으로 처리
//    4. 조회 GET·등록 POST·전체 수정 PUT
//    정답·해설: practice/workbook-questions.md#w077
//    정답: 4번 · 리소스 경로와 행위에 맞는 HTTP 계약을 정합니다. 부분 수정은 PATCH를 고려합니다.

// 선택형 [W078] PathVariable·RequestBody·RequestParam의 입력 위치는?
//    1. 경로 값·요청 본문·쿼리/폼 파라미터
//    2. 비밀번호·CSS·DB 스키마
//    3. 모두 HTTP 응답 본문
//    4. 모두 서버 환경 변수
//    정답·해설: practice/workbook-questions.md#w078
//    정답: 1번 · 서로 다른 요청 데이터 위치를 컨트롤러 파라미터에 바인딩합니다.

// 선택형 [W079] 관리자 전용 품목 변경을 보호하는 방법은?
//    1. URL에 admin을 쓰면 자동으로 보호된다
//    2. HTML 버튼을 숨기기만 한다
//    3. 인증된 사용자의 ADMIN 역할을 서버 가드에서 검사한다
//    4. 품목 코드에 ADMIN을 붙인다
//    정답·해설: practice/workbook-questions.md#w079
//    정답: 3번 · 경로 이름과 UI만으로는 권한을 보장할 수 없습니다.

// 선택형 [W196] PreAuthorize 사용을 위해 필요한 대표 설정은?
//    1. EnableJpaAuditing만
//    2. EntityScan만
//    3. NotBlank만
//    4. EnableMethodSecurity
//    정답·해설: practice/workbook-questions.md#w196
//    정답: 4번 · 어노테이션을 붙였다는 사실만으로 메서드 보안이 활성화되지는 않습니다.

// 선택형 [W250] 품목 상세 경로 /api/items/{id}의 PathVariable이 나타내는 것은?
//    1. 로그인 사용자 비밀번호
//    2. HTTP 상태 코드
//    3. DB 서버 포트
//    4. 조회할 품목 ID
//    정답·해설: practice/workbook-questions.md#w250
//    정답: 4번 · 인증·역할 확인과 별도로 조회 대상 식별자를 해석합니다.

// 선택형 [W336] 모든 수정 필드가 nullable일 때 필요한 입력 정책은?
//    1. null이면 항상 DB 전체를 삭제한다
//    2. 필드 누락과 명시적 null·삭제 의도를 구분하고 수정 의미를 정한다
//    3. 모든 입력 누락을 무조건 200으로 처리한다
//    4. nullable이면 업무 검증이 필요 없다
//    정답·해설: practice/workbook-questions.md#w336
//    정답: 2번 · 전체 교체와 부분 변경 계약을 구분하고 필요한 값의 검증을 명시합니다.

// 선택형 [W337] 전달된 선택 필드만 바꾸는 수정 DTO는 어떤 계약에 가까운가?
//    1. GET 조회
//    2. PATCH 부분 변경
//    3. HEAD 조회
//    4. TRACE 진단
//    정답·해설: practice/workbook-questions.md#w337
//    정답: 2번 · 이 문제는 선택 필드만 변경하는 정책을 전제로 하며 DTO의 nullable 여부만으로 PUT·PATCH가 결정되지는 않습니다.
