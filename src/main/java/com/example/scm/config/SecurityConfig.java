package com.example.scm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 인증/인가는 기존 세션 인터셉터와 Service 권한 검사를 유지하고,
 * Spring Security는 웹 폼 CSRF 및 기본 보안 헤더만 담당한다.
 */
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .logout(logout -> logout.disable())
                // JSON API는 기존 클라이언트 호환성을 유지한다. 브라우저에서는 CORS 기본 차단과
                // JSON RequestBody 요구가 교차 출처의 단순 폼 요청을 막는다.
                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**", "/h2-console/**"))
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));
        return http.build();
    }
}
