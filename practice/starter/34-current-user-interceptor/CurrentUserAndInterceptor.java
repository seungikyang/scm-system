// 세션 인터셉터 + @CurrentUser ArgumentResolver (TRD 3.11.1) · 4지 선다
// 브라우저에서 풀기: ../../sw-quiz/index.html?bank=workbook&module=34
// 각 문제에서 1~4번 중 하나를 고릅니다. 정답·해설 보기: practice/answers.md

// 선택형 [W084] 현재 보안 책임 분담은?
//    1. Security가 모든 세션 사용자 인가를 자동으로 맡는다
//    2. Thymeleaf만 모든 인가를 수행한다
//    3. Repository가 세션 쿠키를 발급한다
//    4. Interceptor·Service는 인증/인가, Security는 CSRF·헤더를 맡는다
//    정답·해설: practice/workbook-questions.md#w084
//    정답: 4번 · 현재 참조 구현과 Spring Security가 전체 인증을 맡는 대안 설계를 구분합니다.

// 선택형 [W146] 세션 인터셉터와 Security 책임을 정리해야 하는 이유는?
//    1. 인증 원천·리다이렉트·예외 정책 충돌을 피하기 위해서다
//    2. Security를 추가하면 Service 검사가 금지된다
//    3. 항상 모든 기능에 같은 검사를 세 번 추가해야 한다
//    4. 둘을 같이 사용하면 HTTP 요청이 불가능하다
//    정답·해설: practice/workbook-questions.md#w146
//    정답: 1번 · 현재 구조는 역할을 분담하고 전체 인증 이전 때는 중복 계약을 조정해야 합니다.

// 선택형 [W147] CurrentUser ArgumentResolver의 역할은?
//    1. 클라이언트의 임의 역할을 인증 없이 승인한다
//    2. 모든 엔티티를 JSON으로 저장한다
//    3. 비밀번호를 브라우저 주소에 붙인다
//    4. 세션 로그인 정보를 컨트롤러 파라미터 값으로 해석한다
//    정답·해설: practice/workbook-questions.md#w147
//    정답: 4번 · supportsParameter와 resolveArgument로 지원 파라미터를 찾고 LoginUser를 제공합니다.

// 선택형 [W148] Servlet Filter와 MVC Interceptor의 실행 순서는?
//    1. Interceptor가 모든 Filter보다 먼저 실행된다
//    2. Filter가 MVC에 앞서고 Interceptor는 Controller 호출 전후에 동작한다
//    3. 둘은 DB 커밋 후에만 실행된다
//    4. 둘은 Java 컴파일 시에만 실행된다
//    정답·해설: practice/workbook-questions.md#w148
//    정답: 2번 · Filter는 Servlet 계층, Interceptor는 MVC 핸들러 처리 계층에 있습니다.

// 선택형 [W149] CurrentUser resolver가 없다면 반복될 수 있는 코드는?
//    1. 브라우저 CSS 다운로드 코드
//    2. Gradle 의존성 선언
//    3. DB 초기화 SQL만
//    4. 컨트롤러마다 세션 로그인 정보를 꺼내 확인하는 코드
//    정답·해설: practice/workbook-questions.md#w149
//    정답: 4번 · 사용자 해석을 공통화하면 컨트롤러의 반복과 일관성 문제를 줄입니다.

// 선택형 [W150] 세션에서 JWT로 바꿀 때 바뀌는 부분은?
//    1. 모든 도메인 상태 전이를 삭제한다
//    2. ItemStatus를 UserRole로 바꾼다
//    3. 인증 정보 추출·토큰 검증·사용자 해석과 폐기 정책이다
//    4. 재고 테이블을 HTML로 바꾼다
//    정답·해설: practice/workbook-questions.md#w150
//    정답: 3번 · 인증 원천은 달라져도 업무 권한과 소유권 규칙은 계속 필요합니다.

// 선택형 [W227] WebMvcConfig의 보호 경로·예외 경로에 대한 현재 설명은?
//    1. 모든 admin 경로가 문자열만으로 자동 보호된다
//    2. H2 전용 서블릿도 MVC 인터셉터만으로 항상 보호된다
//    3. 전반적 로그인 검사에서 로그인·정적 리소스 등을 제외한다
//    4. 정적 CSS마다 DB 트랜잭션을 생성한다
//    정답·해설: practice/workbook-questions.md#w227
//    정답: 3번 · H2 콘솔 서블릿의 실제 보호는 SecurityConfig에서 별도 처리합니다.

// 선택형 [W343] SessionAttribute와 커스텀 CurrentUser의 차이는?
//    1. 전자는 지정 세션 속성 바인딩, 후자는 사용자 해석 정책을 공통 resolver로 추상화한다
//    2. 둘 다 무조건 JWT 서명만 검사한다
//    3. 전자는 Repository 구현을 생성한다
//    4. 후자는 DB 비밀번호를 자동 암호화한다
//    정답·해설: practice/workbook-questions.md#w343
//    정답: 1번 · 현재 CurrentUser는 LoginUser·미인증 처리 등 공통 정책을 컨트롤러 인자 해석에 모읍니다.

// 선택형 [W358] ArgumentResolver가 던진 업무 예외를 Advice로 처리하려면?
//    1. 어떤 Filter 예외도 MVC Advice가 항상 처리한다
//    2. resolver가 HTML을 직접 출력해야 한다
//    3. MVC 예외 처리 흐름·Advice 적용 범위·해당 핸들러가 맞아야 한다
//    4. 예외를 null로 바꾸면 오류 계약이 유지된다
//    정답·해설: practice/workbook-questions.md#w358
//    정답: 3번 · 컨트롤러 인자 해석 단계의 예외도 처리할 수 있으나 Filter 등 다른 실행 경계와 구분합니다.

// 선택형 [W362] AuthenticationPrincipal과 현재 커스텀 CurrentUser의 인증 원천 차이는?
//    1. 둘 다 언제나 같은 세션 속성에 자동 연결된다
//    2. 전자는 JPA 영속성 컨텍스트다
//    3. 후자는 반드시 JWT를 사용한다
//    4. 전자는 Security 인증 principal, 후자는 현재 세션 LoginUser 해석을 사용한다
//    정답·해설: practice/workbook-questions.md#w362
//    정답: 4번 · 인증 구조를 옮길 때 컨트롤러 인자 계약과 인증 원천을 함께 맞춰야 합니다.
