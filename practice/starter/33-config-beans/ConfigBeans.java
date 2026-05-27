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
    private final CurrentUserArgumentResolver currentUserArgumentResolver;
    private final CurrentUserRoleArgumentResolver currentUserRoleArgumentResolver;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authCheckInterceptor)
            // TODO 04: 어떤 경로를 보호 대상에서 제외해야 할까요? (로그인, 정적 리소스, H2 콘솔)
            .excludePathPatterns(
                "/api/auth/login",
                "/api/auth/logout",
                "/h2-console/**",
                "/css/**", "/js/**", "/images/**",
                // TODO 05: API 명세 자동화 (springdoc-openapi) 를 쓴다면 제외할 경로 두 가지는?
                "/____/**", "/____/**"
            )
            .addPathPatterns("/api/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        // TODO 06: 두 가지 ArgumentResolver 모두 등록.
        resolvers.add(currentUserArgumentResolver);
        resolvers.add(currentUserRoleArgumentResolver);
    }
}


// =====================================================================
// 4. (학습 2차) Spring Security 단계의 SecurityFilterChain — 보강용 미리보기
// =====================================================================
// 이 단계는 TRD 3.11.2 의 "확장 구현" 입니다.
// 1차(세션) 단계와 2차(Security) 단계를 동시에 켜지 않습니다.
//
// @Configuration
// @EnableMethodSecurity
// public class SecurityConfig {
//     @Bean
//     public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//         http
//             .csrf(csrf -> csrf.disable())
//             .sessionManagement(s -> s.sessionFixation().changeSessionId())
//             .authorizeHttpRequests(auth -> auth
//                 .requestMatchers("/api/auth/**", "/h2-console/**").permitAll()
//                 .requestMatchers("/api/admin/**").hasRole("ADMIN")
//                 .requestMatchers(
//                     "/api/sales-orders/pending",
//                     "/api/sales-orders/*/ship",
//                     "/api/sales-orders/*/complete"
//                 ).hasAnyRole("MANAGER", "ADMIN")
//                 .anyRequest().authenticated()
//             )
//             .formLogin(form -> form.loginProcessingUrl("/api/auth/login").permitAll())
//             .logout(logout -> logout.logoutUrl("/api/auth/logout"));
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
// Q6. 1차(세션) Config 와 2차(Security) Config 를 동시에 켜면 어떤 충돌이 나는가?
//     A:
