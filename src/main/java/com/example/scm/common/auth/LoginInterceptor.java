package com.example.scm.common.auth;

import com.example.scm.common.exception.ErrorCode;
import com.example.scm.common.response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 화이트리스트 외 모든 요청에 세션 LoginUser 를 요구한다.
 * - 미인증 web 요청: /login 으로 redirect
 * - 미인증 api(/api/**) 요청: 401 AUTHENTICATION_REQUIRED (JSON)
 */
public class LoginInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper;

    public LoginInterceptor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HttpSession session = request.getSession(false);
        Object loginUser = (session == null) ? null : session.getAttribute(SessionConst.LOGIN_USER);
        if (loginUser != null) {
            return true;
        }

        String uri = request.getRequestURI();
        if (uri.startsWith("/api/")) {
            response.setStatus(ErrorCode.AUTHENTICATION_REQUIRED.getHttpStatus().value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.getWriter()
                    .write(objectMapper.writeValueAsString(ErrorResponse.of(ErrorCode.AUTHENTICATION_REQUIRED)));
        } else {
            response.sendRedirect("/login");
        }
        return false;
    }
}
