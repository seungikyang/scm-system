// 실제 구현 위치 예: src/main/java/com/example/scm/config/*
// 목표: 학습용 세션 / Security / Audit 단계에서 필요한 핵심 Bean 들을 채우세요.
//       TRD 3.11 (보안), 3.5 (Audit 컬럼) 참고.

// =====================================================================
// 1. JPA Auditing 활성화 — createdAt/updatedAt 자동 채움
// =====================================================================
// TODO 01: @CreatedDate, @LastModifiedDate 를 사용하려면 부트스트랩에 어떤 어노테이션이 필요한가?
@Configuration
@____
public class JpaAuditingConfig {
    // 비어 있어도 됩니다. Entity 쪽에서 @EntityListeners(AuditingEntityListener.class) 를 잊지 마세요.

    // 향후 createdBy / modifiedBy 가 필요하면 AuditorAware<Long> 빈을 추가합니다.
    // @Bean
    // public AuditorAware<Long> auditorAware() {
    //     return () -> {
    //         HttpSession session = ...; // 현재 요청의 세션
    //         return Optional.ofNullable((Long) session.getAttribute("USER_ID"));
    //     };
    // }
}


// =====================================================================
// 2. PasswordEncoder Bean — 평문 저장 금지의 인프라
// =====================================================================
@Configuration
public class PasswordConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // TODO 02: 단방향 해시 + salt 자동 처리. SHA-256 만으로는 왜 부족한가요?
        return new ____PasswordEncoder();
    }
}


// =====================================================================
// 3. (학습 1차) 세션 기반 인증 인터셉터 등록
// =====================================================================
// TODO 03: HandlerInterceptor 와 ArgumentResolver 를 묶어 등록하려면 어떤 인터페이스를 구현해야 하나?
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements ____ {

    private final AuthCheckInterceptor authCheckInterceptor;
    private final AdminOnlyInterceptor adminOnlyInterceptor;
    private final CurrentUserArgumentResolver currentUserArgumentResolver;
    private final CurrentUserRoleArgumentResolver currentUserRoleArgumentResolver;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authCheckInterceptor)
            // TODO 04: 어떤 경로를 보호 대상에서 제외해야 할까요? (로그인, 정적 리소스)
            .excludePathPatterns(
                "/api/auth/login",
                "/api/auth/logout",
                "/css/**", "/js/**", "/images/**",
                // TODO 05: API 명세 자동화 (springdoc-openapi) 를 쓴다면 제외할 경로 두 가지는?
                "/____/**", "/____/**"
            )
            .addPathPatterns("/api/**", "/h2-console/**");

        // H2 console은 환경 변수로 켠 경우에도 로그인한 ADMIN만 접근합니다.
        registry.addInterceptor(adminOnlyInterceptor)
            .addPathPatterns("/h2-console/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        // TODO 06: 두 가지 ArgumentResolver 모두 등록.
        resolvers.add(currentUserArgumentResolver);
        resolvers.add(currentUserRoleArgumentResolver);
    }
}


// =====================================================================
// 4. 현재 참조 구현의 SecurityFilterChain — 역할 분담
// =====================================================================
// 인증/인가는 위 Interceptor + Service가 담당하고 Security는 CSRF/보안 헤더만 담당합니다.
// 역할을 분리한 하이브리드는 가능하지만, 두 체계가 모두 인증을 시도하게 만들면 안 됩니다.
//
// @Configuration
// public class SecurityConfig {
//     @Bean
//     public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//         http
//             .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
//             .formLogin(form -> form.disable())
//             .httpBasic(basic -> basic.disable())
//             .logout(logout -> logout.disable())
//             .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**", "/h2-console/**"))
//             .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));
//         return http.build();
//     }
// }


// =====================================================================
// 5. (학습 3차) JWT 단계 — 진화 방향
// =====================================================================
// @Configuration
// public class JwtConfig {
//     @Bean
//     public JwtAuthenticationFilter jwtAuthenticationFilter(...) { ... }
//
//     @Bean
//     public Key jwtSigningKey(@Value("${jwt.secret}") String secret) {
//         return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
//     }
// }


// =====================================================================
// 6. 학습 질문
// =====================================================================
// Q1. @EnableJpaAuditing 을 빼면 createdAt 이 null 로 들어옵니다. 왜 그럴까요?
//     A:
// Q2. BCrypt 가 SHA-256 보다 안전한 이유 2 가지(salt, work factor)를 한 줄로 설명해 보세요.
//     A:
// Q3. WebMvcConfigurer 와 SecurityConfig 가 공존할 때, 권한 검사 순서는 어떻게 되는가?
//     A:
// Q4. Spring Boot 의 자동 설정만으로 PasswordEncoder 가 자동 등록되지 않는 이유는?
//     A:
// Q5. AuditorAware<Long> 가 없으면 createdBy / modifiedBy 컬럼은 어떻게 들어가는가?
//     A:
// Q6. 현재 하이브리드에서 Interceptor/Service와 Security filter chain의 책임은 각각 무엇인가?
//     A:
// Q7. 인증까지 Spring Security로 옮긴다면 제거하거나 변경해야 할 기존 구성은 무엇인가?
//     A:
