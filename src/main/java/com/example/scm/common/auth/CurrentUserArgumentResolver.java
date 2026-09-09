package com.example.scm.common.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 컨트롤러 파라미터 주입기(ArgumentResolver).
 * 학습 모듈 34의 마지막 구현 파일이다. LoginInterceptor가 요청을 통과시킨 뒤 이 객체가
 * 컨트롤러 인자 값을 만드는 순서를 확인한다.
 * 컨트롤러 메서드에 "@CurrentUser LoginUser loginUser" 가 있으면, 세션에서 로그인 정보를
 * 꺼내 자동으로 넣어 준다. 컨트롤러마다 session.getAttribute(...) 를 반복 쓰지 않게 하는 장치다.
 *
 * 동작 방식: supportsParameter() → "이 파라미터를 내가 처리할 수 있는가?" 판단,
 * resolveArgument() → 실제 값을 만들어 주입. 등록은 WebMvcConfig 에서 한다.
 * 미로그인 시 null 을 주입하며, 로그인 강제는 LoginInterceptor 의 역할로 분리되어 있다.
 */
@Component
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

    /** {@code @CurrentUser}가 붙었고 타입이 LoginUser 계열일 때만 이 리졸버가 개입한다. */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUser.class)
                && LoginUser.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        return session.getAttribute(SessionConst.LOGIN_USER);
    }
}
