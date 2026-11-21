package com.example.scm.common.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 관리자만 들어갈 수 있는 곳에서 출입증을 확인하는 문지기입니다.
 * 로그인했다고 모두 관리자는 아니므로, 로그인 여부와 관리자 여부를 따로 확인합니다.
 *
 * 개발용 관리 화면처럼 ADMIN에게만 허용할 경로의 보조 가드.
 */
public class AdminOnlyInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HttpSession session = request.getSession(false);
        Object sessionUser = session == null ? null : session.getAttribute(SessionConst.LOGIN_USER);
        if (sessionUser instanceof LoginUser loginUser && loginUser.isAdmin()) {
            return true;
        }
        response.sendError(HttpStatus.FORBIDDEN.value());
        return false;
    }
}
