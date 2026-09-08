package com.example.scm.config;

import com.example.scm.common.auth.AdminOnlyInterceptor;
import com.example.scm.common.auth.CurrentUserArgumentResolver;
import com.example.scm.common.auth.LoginInterceptor;
import tools.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring MVC의 공통 동작을 등록하는 설정 클래스.
 *
 * <p>학습 모듈 33에서는 두 MVC 확장 지점이 등록된다는 사실만 확인한다. 다음 모듈 34에서
 * LoginInterceptor → CurrentUserArgumentResolver 순으로 구현을 열어 실제 동작을 읽는다.</p>
 *
 * <p>요청은 대략 {@code 인터셉터 -> 컨트롤러 -> 서비스} 순서로 흐른다. 여기서는
 * {@link CurrentUserArgumentResolver}를 등록해 로그인 사용자를 컨트롤러 파라미터에
 * 넣고, 인터셉터를 등록해 컨트롤러에 도착하기 전에 로그인 여부를 검사한다.</p>
 */
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final CurrentUserArgumentResolver currentUserArgumentResolver;
    private final ObjectMapper objectMapper;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        // 이제 컨트롤러에서 @CurrentUser LoginUser 형식을 사용할 수 있다.
        resolvers.add(currentUserArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // order가 작은 로그인 검사가 먼저 실행된다. 로그인 화면과 정적 파일은 예외다.
        registry.addInterceptor(new LoginInterceptor(objectMapper))
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/login",
                        "/logout",
                        "/api/auth/login",
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/favicon.ico",
                        "/error"
                );
        // 기본 콘솔 경로가 MVC로 들어올 때의 보조 검사다(예: 콘솔 비활성 시).
        // 실제 H2 서블릿은 인터셉터를 지나지 않으므로 SecurityConfig에서 별도로 보호한다.
        registry.addInterceptor(new AdminOnlyInterceptor())
                .order(2)
                .addPathPatterns("/h2-console/**");
    }
}
