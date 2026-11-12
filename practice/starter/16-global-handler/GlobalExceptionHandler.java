// GlobalExceptionHandler (TRD 3.9) · 4지 선다
// 브라우저에서 풀기: ../../sw-quiz/index.html?bank=workbook&module=16
// 각 문제에서 1~4번 중 하나를 고릅니다. 정답·해설 보기: practice/answers.md

// 선택형 [W071] RestControllerAdvice의 역할은?
//    1. 모든 엔티티를 자동 삭제한다
//    2. HTML의 CSS를 컴파일한다
//    3. 세션 쿠키를 DB 기본키로 바꾼다
//    4. 여러 API 컨트롤러의 예외를 공통 응답으로 처리한다
//    정답·해설: practice/workbook-questions.md#w071
//    정답: 4번 · ExceptionHandler와 함께 API 예외 처리 책임을 모읍니다.

// 선택형 [W072] 업무·입력 검증·예상 밖 예외를 구분하는 이유는?
//    1. 모든 예외의 스택을 사용자에게 공개한다
//    2. 검증 실패를 성공으로 응답한다
//    3. 사용자 오류와 내부 오류를 서로 맞는 계약으로 응답한다
//    4. 업무 예외가 나면 테스트를 삭제한다
//    정답·해설: practice/workbook-questions.md#w072
//    정답: 3번 · 알려진 ErrorCode와 검증 메시지를 처리하고 내부 오류 정보는 노출을 제한합니다.

// 선택형 [W073] DTO 검증 오류에서 필드별 메시지를 모으는 방법은?
//    1. 비밀번호 해시를 메시지로 출력한다
//    2. JSESSIONID를 오류 코드로 쓴다
//    3. 모든 필드를 SQL 문자열로 연결한다
//    4. BindingResult의 FieldError를 순회해 필드와 메시지를 추출한다
//    정답·해설: practice/workbook-questions.md#w073
//    정답: 4번 · 검증 예외의 바인딩 결과에 필드 이름과 기본 메시지가 들어 있습니다.

// 선택형 [W275] 필수 DTO 입력 검증 실패의 현재 공통 업무 코드는?
//    1. ACCESS_DENIED
//    2. INVALID_INPUT
//    3. ITEM_NOT_FOUND
//    4. INTERNAL_ERROR
//    정답·해설: practice/workbook-questions.md#w275
//    정답: 2번 · 현재 API 예외 처리기는 입력 검증 실패를 400과 INVALID_INPUT으로 전달합니다.

// 선택형 [W332] RestControllerAdvice와 ControllerAdvice의 차이는?
//    1. 후자는 예외를 처리할 수 없다
//    2. 전자는 Repository만 처리한다
//    3. 전자는 ResponseBody 의미를 포함하며 후자는 View 등 다른 응답 방식도 사용할 수 있다
//    4. 둘 다 DB 스키마를 생성한다
//    정답·해설: practice/workbook-questions.md#w332
//    정답: 3번 · 응답 표현과 적용 대상에 맞춰 API·Web 예외 처리기를 구분합니다.

// 선택형 [W333] 예상 밖 예외의 상세 메시지 공개가 위험한 이유는?
//    1. 모든 메시지가 자동으로 BCrypt 해시가 되기 때문이다
//    2. 오류 메시지가 DTO 검증을 대신하기 때문이다
//    3. 내부 구조·SQL·경로·비밀정보가 노출될 수 있다
//    4. 세션 ID를 공개해야만 오류를 진단할 수 있기 때문이다
//    정답·해설: practice/workbook-questions.md#w333
//    정답: 3번 · 사용자에게 안정적인 오류 계약을 응답하고 서버에서 필요한 진단 정보를 관리합니다.

// 선택형 [W334] 컨트롤러 그룹별로 Advice를 나누는 방법은?
//    1. basePackages·annotations 등 적용 범위를 명시하고 우선순위를 검토한다
//    2. 어노테이션 이름만 다르게 바꾼다
//    3. 같은 타입의 핸들러를 무제한 중복 선언한다
//    4. 모든 컨트롤러가 직접 스택을 출력한다
//    정답·해설: practice/workbook-questions.md#w334
//    정답: 1번 · 예외 처리 대상·응답 형식·Advice 간 적용 순서를 일관되게 구성합니다.

// 선택형 [W385] 예외 처리기에서 HTTP 상태와 오류 DTO 본문을 함께 구성하는 반환 타입은?
//    1. HttpSession
//    2. Pageable
//    3. ResponseEntity<ErrorResponse>
//    4. EntityManager
//    정답·해설: practice/workbook-questions.md#w385
//    정답: 3번 · ResponseEntity.status(...).body(...)로 상태와 본문을 지정할 수 있습니다. 도메인 예외의 HTTP 변환은 API 처리기의 책임입니다.

// 선택형 [W386] 현재 ApiExceptionHandler가 예상 밖 서버 예외를 기록하는 로그 수준은?
//    1. TRACE
//    2. ERROR
//    3. DEBUG
//    4. INFO
//    정답·해설: practice/workbook-questions.md#w386
//    정답: 2번 · 예상 밖 예외는 log.error로 요청 경로와 예외 증거를 남깁니다. 클라이언트 응답에는 내부 상세를 노출하지 않습니다.
