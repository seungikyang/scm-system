// DTO 검증 (TRD 3.7, 비기능 검증) · 4지 선다
// 브라우저에서 풀기: ../../sw-quiz/index.html?bank=workbook&module=17
// 각 문제에서 1~4번 중 하나를 고릅니다. 정답·해설 보기: practice/answers.md

// 선택형 [W074] NotBlank·Positive·Size·Pattern의 역할 조합은?
//    1. 공백 금지·양수·길이/개수·정규식 검증
//    2. DB 생성·로그인·정렬·출고 처리
//    3. 모두 같은 문자열 길이 검사
//    4. 모두 권한 검사
//    정답·해설: practice/workbook-questions.md#w074
//    정답: 1번 · 각 어노테이션은 적용 타입과 검사하는 입력 조건이 다릅니다.

// 선택형 [W075] MVC 요청 DTO의 Valid 검증은 주로 언제 수행되는가?
//    1. CSS를 읽는 순간 수행된다
//    2. 컨트롤러 인자 바인딩·해석 과정에서 수행된다
//    3. DB 백업할 때만 수행된다
//    4. 모든 Repository 메서드가 무조건 자동 실행한다
//    정답·해설: practice/workbook-questions.md#w075
//    정답: 2번 · 다른 호출 경로에는 Service 검증 등 추가 방어가 필요할 수 있습니다.

// 선택형 [W076] 요청 DTO와 응답 DTO를 분리하는 이유는?
//    1. 모든 응답에 비밀번호를 넣기 위해서다
//    2. DTO가 DB 잠금을 대체하기 때문이다
//    3. 입력 허용 필드와 출력 공개 필드의 계약이 다르기 때문이다
//    4. JPA 엔티티를 두 번 저장하기 위해서다
//    정답·해설: practice/workbook-questions.md#w076
//    정답: 3번 · 각 경계에 필요한 데이터와 검증 정책을 독립적으로 관리합니다.

// 선택형 [W327] MVC 요청 DTO에서 Valid가 빠지면 생길 수 있는 문제는?
//    1. 해당 입력 경계의 Bean Validation이 실행되지 않아 잘못된 값이 다음 계층으로 갈 수 있다
//    2. DB unique가 자동 삭제된다
//    3. 모든 입력이 무조건 404가 된다
//    4. 모든 Service 검증도 자동으로 제거된다
//    정답·해설: practice/workbook-questions.md#w327
//    정답: 1번 · Valid의 호출 지점과 별도로 Service·도메인 검증이 있는지 확인해야 합니다.

// 선택형 [W335] NotNull·NotEmpty·NotBlank의 차이는?
//    1. 세 가지가 모두 동일한 숫자 범위 검사다
//    2. NotNull은 빈 문자열도 항상 거절한다
//    3. NotEmpty는 공백 문자열을 항상 거절한다
//    4. null 금지·null과 빈 값 금지·문자열 공백까지 금지
//    정답·해설: practice/workbook-questions.md#w335
//    정답: 4번 · 문자열·컬렉션 등 적용 타입과 공백 처리 조건을 구분합니다.

// 선택형 [W387] BigDecimal 단가에서 0을 허용하고 음수만 금지하는 제약은?
//    1. @Positive
//    2. @NotBlank
//    3. @Past
//    4. @DecimalMin("0.00")
//    정답·해설: practice/workbook-questions.md#w387
//    정답: 4번 · @DecimalMin의 기본 inclusive=true는 0.00도 허용합니다. 필수 입력이면 @NotNull과 함께 사용하고 자릿수도 별도로 제한합니다.
