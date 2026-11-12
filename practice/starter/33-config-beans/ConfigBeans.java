// 핵심 Bean 설정 — JPA Auditing / PasswordEncoder / WebMvcConfig (TRD 3.5, 3.11) · 4지 선다
// 브라우저에서 풀기: ../../sw-quiz/index.html?bank=workbook&module=33
// 각 문제에서 1~4번 중 하나를 고릅니다. 정답·해설 보기: practice/answers.md

// 선택형 [W008] createdAt과 updatedAt을 자동 기록하려면?
//    1. 필드 이름만 있으면 DB가 항상 자동 기록한다
//    2. 모든 조회에서 createdAt을 현재 시각으로 바꾼다
//    3. 컨트롤러가 비밀번호로 날짜를 계산한다
//    4. 감사 어노테이션·리스너와 JPA Auditing 설정을 연결한다
//    정답·해설: practice/workbook-questions.md#w008
//    정답: 4번 · 참조 구현은 BaseTimeEntity와 JpaAuditingConfig로 생성·수정 시각을 관리합니다.

// 선택형 [W084] 현재 보안 책임 분담은?
//    1. Security가 모든 세션 사용자 인가를 자동으로 맡는다
//    2. Thymeleaf만 모든 인가를 수행한다
//    3. Repository가 세션 쿠키를 발급한다
//    4. Interceptor·Service는 인증/인가, Security는 CSRF·헤더를 맡는다
//    정답·해설: practice/workbook-questions.md#w084
//    정답: 4번 · 현재 참조 구현과 Spring Security가 전체 인증을 맡는 대안 설계를 구분합니다.

// 선택형 [W085] 인증·인가를 Spring Security로 옮길 때 필요한 정리는?
//    1. 기존 검사와 새 검사를 목적 없이 모두 추가한다
//    2. 기존 세션 인터셉터와 사용자 해석의 중복 책임을 재설계한다
//    3. Service의 업무 소유권 검사도 전부 삭제한다
//    4. 모든 API를 permitAll로 바꾼다
//    정답·해설: practice/workbook-questions.md#w085
//    정답: 2번 · 새 인증 원천과 기존 계약을 맞추면서 필요한 Service 업무 인가는 유지합니다.

// 선택형 [W143] JPA Auditing 활성화가 빠지면 생길 수 있는 문제는?
//    1. 모든 엔티티가 자동 ADMIN이 된다
//    2. 모든 날짜가 SQL 기본키가 된다
//    3. 감사 어노테이션이 붙어도 시각 자동 기록이 동작하지 않을 수 있다
//    4. Hibernate가 서버 포트를 바꾼다
//    정답·해설: practice/workbook-questions.md#w143
//    정답: 3번 · 감사 리스너와 EnableJpaAuditing 등 관련 설정이 연결되어야 합니다.

// 선택형 [W144] 비밀번호에 BCrypt를 사용하는 이유는?
//    1. salt와 조절 가능한 연산 비용으로 대량 추측 비용을 높인다
//    2. 평문을 누구나 쉽게 복원할 수 있다
//    3. 빠른 SHA-256 한 번이면 항상 더 안전하다
//    4. 같은 비밀번호가 항상 같은 해시를 만들게 한다
//    정답·해설: practice/workbook-questions.md#w144
//    정답: 1번 · 비밀번호 해시는 빠른 일반 해시와 목적이 다르며 BCrypt는 matches로 검증합니다.

// 선택형 [W145] WebMvcConfigurer의 두 등록 메서드 역할은?
//    1. 둘 다 DB 스키마를 생성한다
//    2. 둘 다 CSS를 압축한다
//    3. addInterceptors는 요청 검사, addArgumentResolvers는 인자 해석 등록이다
//    4. 둘 다 BCrypt 비용을 변경한다
//    정답·해설: practice/workbook-questions.md#w145
//    정답: 3번 · MVC 요청 처리의 확장 지점을 설정에 연결합니다.

// 선택형 [W146] 세션 인터셉터와 Security 책임을 정리해야 하는 이유는?
//    1. 인증 원천·리다이렉트·예외 정책 충돌을 피하기 위해서다
//    2. Security를 추가하면 Service 검사가 금지된다
//    3. 항상 모든 기능에 같은 검사를 세 번 추가해야 한다
//    4. 둘을 같이 사용하면 HTTP 요청이 불가능하다
//    정답·해설: practice/workbook-questions.md#w146
//    정답: 1번 · 현재 구조는 역할을 분담하고 전체 인증 이전 때는 중복 계약을 조정해야 합니다.

// 선택형 [W148] Servlet Filter와 MVC Interceptor의 실행 순서는?
//    1. Interceptor가 모든 Filter보다 먼저 실행된다
//    2. Filter가 MVC에 앞서고 Interceptor는 Controller 호출 전후에 동작한다
//    3. 둘은 DB 커밋 후에만 실행된다
//    4. 둘은 Java 컴파일 시에만 실행된다
//    정답·해설: practice/workbook-questions.md#w148
//    정답: 2번 · Filter는 Servlet 계층, Interceptor는 MVC 핸들러 처리 계층에 있습니다.

// 선택형 [W226] PasswordConfig의 Bean이 구현하는 비밀번호 해시·검증 인터페이스는?
//    1. Pageable
//    2. PasswordEncoder
//    3. HttpSession
//    4. PurchaseOrder
//    정답·해설: practice/workbook-questions.md#w226
//    정답: 2번 · BCryptPasswordEncoder는 PasswordEncoder 인터페이스를 구현합니다. 현재 설정의 반환 타입과 Service 주입 타입은 BCryptPasswordEncoder입니다.

// 선택형 [W227] WebMvcConfig의 보호 경로·예외 경로에 대한 현재 설명은?
//    1. 모든 admin 경로가 문자열만으로 자동 보호된다
//    2. H2 전용 서블릿도 MVC 인터셉터만으로 항상 보호된다
//    3. 전반적 로그인 검사에서 로그인·정적 리소스 등을 제외한다
//    4. 정적 CSS마다 DB 트랜잭션을 생성한다
//    정답·해설: practice/workbook-questions.md#w227
//    정답: 3번 · H2 콘솔 서블릿의 실제 보호는 SecurityConfig에서 별도 처리합니다.

// 선택형 [W356] 현재 프로젝트에서 PasswordEncoder를 Bean으로 명시하는 이유는?
//    1. Security starter가 없으면 Java에서 BCrypt를 호출할 수 없기 때문이다
//    2. Bean을 만들면 비밀번호가 평문으로 자동 저장되기 때문이다
//    3. EnableJpaAuditing이 모든 암호화 객체를 자동 생성하기 때문이다
//    4. 알고리즘·설정과 주입할 객체를 애플리케이션이 명확히 선택하기 위해서다
//    정답·해설: practice/workbook-questions.md#w356
//    정답: 4번 · 자동 설정으로 원하는 Encoder가 항상 생긴다고 가정하지 않고 PasswordConfig의 실제 등록을 확인합니다.

// 선택형 [W357] 감사 사용자 필드와 시각 필드에 대한 설명은?
//    1. CreatedDate는 AuditorAware가 없으면 항상 비밀번호가 된다
//    2. createdBy가 없으면 createdAt도 기술적으로 절대 기록할 수 없다
//    3. 모든 감사 필드는 기본 ADMIN으로 기록된다
//    4. CreatedBy·LastModifiedBy는 감사 사용자 공급이 필요하고 시각 감사와 별도다
//    정답·해설: practice/workbook-questions.md#w357
//    정답: 4번 · 사용자 감사에는 AuditorAware 등의 정책이 필요하며 현재 시각 감사 구성과 구분합니다.

// 선택형 [W393] springdoc를 추가하고 문서 공개를 승인한 설계에서 기본 Swagger UI·API 문서 경로 조합은?
//    1. /css/**와 /js/**
//    2. /swagger-ui/**와 /v3/api-docs/**
//    3. /api/items/**와 /api/partners/**
//    4. /h2-console/**와 /api/users/**
//    정답·해설: practice/workbook-questions.md#w393
//    정답: 2번 · 기본 Swagger UI와 OpenAPI 문서 경로입니다. 현재 프로젝트가 해당 라이브러리를 이미 제공한다는 뜻은 아니며 운영 공개 여부는 별도 권한 정책으로 결정합니다. 참조: https://springdoc.org/
