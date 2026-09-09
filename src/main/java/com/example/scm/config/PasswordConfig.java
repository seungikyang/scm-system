package com.example.scm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 비밀번호 해시 생성·검증기를 Spring Bean으로 등록한다.
 *
 * <p>학습 모듈 33: JpaAuditingConfig → 이 클래스 → WebMvcConfig 순서로 읽는다.
 * 모듈 23에서 사용했던 BCryptPasswordEncoder가 어디서 만들어지는지 여기서 확인한다.</p>
 *
 * <p>{@code @Bean}으로 만든 객체는 Spring 컨테이너가 한 번 생성하고 AuthService와
 * UserService에 주입한다. BCrypt는 같은 비밀번호도 매번 다른 해시가 나오도록 salt를
 * 포함하므로, 로그인 확인은 문자열 비교가 아니라 {@code matches()}로 해야 한다.
 * 저장된 해시에서 원래 비밀번호를 복호화하는 방식이 아니다.</p>
 */
@Configuration
public class PasswordConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
