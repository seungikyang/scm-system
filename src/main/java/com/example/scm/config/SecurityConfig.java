package com.example.scm.config;

import com.example.scm.common.auth.LoginUser;
import com.example.scm.common.auth.SessionConst;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 웹 요청이 지켜야 할 보안 규칙을 설정합니다.
 * CSRF 검사는 다른 사이트가 사용자 몰래 우리 화면의 변경 요청을 보내는 일을 막는 장치입니다.
 *
 * 인증/인가는 기존 세션 인터셉터와 Service 권한 검사를 유지하고,
 * Spring Security는 웹 폼 CSRF, 기본 보안 헤더 및 H2 콘솔 접근 제어를 담당한다.
 *
 * <p>학습 모듈 20: SecurityFilterChain → LoginInterceptor → Authz의 역할을 표로 나눠
 * 적은 뒤 읽는다. 실제 로그인 구현은 모듈 23, MVC 등록 코드는 모듈 33~34에서 이어진다.</p>
 */
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
            @Value("${spring.h2.console.path:/h2-console}") String consolePath) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        // H2는 MVC 인터셉터를 지나지 않는 별도 서블릿이다. 필터에서 ADMIN 세션을 검사한다.
                        // 설정된 콘솔 경로를 사용해 경로를 바꾸어도 동일한 권한 규칙을 적용한다.
                        .requestMatchers(consolePath, consolePath + "/**")
                        .access((authentication, context) -> {
                            HttpSession session = context.getRequest().getSession(false);
                            Object user = session == null ? null : session.getAttribute(SessionConst.LOGIN_USER);
                            return new AuthorizationDecision(user instanceof LoginUser loginUser && loginUser.isAdmin());
                        })
                        // 이 필터를 통과해도 LoginInterceptor와 Service에서 로그인·역할을 다시 검사합니다.
                        .anyRequest().permitAll())
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .logout(logout -> logout.disable())
                // JSON API는 기존 클라이언트 호환성을 유지한다. 브라우저에서는 CORS 기본 차단과
                // JSON RequestBody 요구가 교차 출처의 단순 폼 요청을 막는다.
                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**", consolePath, consolePath + "/**"))
                // 같은 사이트의 화면 안에만 이 화면을 끼워 넣도록 허용합니다. H2 콘솔도 이 방식을 씁니다.
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));
        return http.build();
    }
}
