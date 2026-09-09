package com.example.scm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration;

/**
 * 애플리케이션 시작점(엔트리 포인트).
 *
 * <p>학습 모듈 00: build.gradle과 application.yml을 확인한 다음 이 파일에서 프로그램이
 * 어디서 시작하는지만 익힌다. 세부 Bean 설정은 모듈 33에서 다시 읽는다.</p>
 *
 * - @SpringBootApplication: 자동 설정 + 컴포넌트 스캔을 켠다. 이 클래스가 속한
 *   com.example.scm 패키지 "하위 전체"에서 @Service/@Repository/@Controller 등을
 *   찾아 Spring 이 객체를 만들어 관리한다.
 *
 * - UserDetailsServiceAutoConfiguration 제외 이유: Spring Security 는 기본적으로
 *   랜덤 비밀번호 콘솔 로그인을 제공하지만, 이 프로젝트는 세션 기반 자체 로그인
 *   (AuthService + LoginInterceptor)을 사용하므로 그 기본 설정을 끈다.
 */
@SpringBootApplication(exclude = UserDetailsServiceAutoConfiguration.class)
public class ScmApplication {

    public static void main(String[] args) {
        // 설정을 읽고 Spring 빈들을 연결하며 내장 웹 서버를 시작한다.
        // main()에서 Controller를 직접 호출하지 않고, HTTP 요청이 들어오면 MVC가 호출한다.
        SpringApplication.run(ScmApplication.class, args);
    }
}
