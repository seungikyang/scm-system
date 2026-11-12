package com.example.scm.common.auth;

import com.example.scm.common.exception.ErrorCode;
import com.example.scm.common.response.ErrorResponse;
import tools.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 화면이나 기능에 들어가기 전에 로그인 출입증을 확인합니다.
 * 로그인하지 않은 브라우저는 로그인 화면으로 보내고, API 요청에는 로그인 필요 오류를 보냅니다.
 *
 * 화이트리스트 외 모든 요청에 세션 LoginUser 를 요구한다.
 * 학습 모듈 34에서 세션 값의 존재 확인 → API/Web 실패 응답 분기 → false 반환 순으로
 * preHandle을 읽는다. 다음에는 CurrentUserArgumentResolver로 이동한다.
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
        // false는 출입증이 없을 때 새 보관함(세션)을 만들지 말고, 기존 보관함만 찾으라는 뜻입니다.
        HttpSession session = request.getSession(false);
        Object loginUser = (session == null) ? null : session.getAttribute(SessionConst.LOGIN_USER);
        if (loginUser != null) {
            // true이면 다음 검사와 Controller로 진행합니다. 아래의 false이면 여기서 요청을 끝냅니다.
            return true;
        }

        String servletPath = request.getServletPath();
        if (servletPath.startsWith("/api/")) {
            // 다른 프로그램은 로그인 화면보다 오류 자료가 필요하므로 JSON과 401(로그인 필요)을 보냅니다.
            response.setStatus(ErrorCode.AUTHENTICATION_REQUIRED.getHttpStatus().value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.getWriter()
                    .write(objectMapper.writeValueAsString(ErrorResponse.of(ErrorCode.AUTHENTICATION_REQUIRED)));
        } else {
            response.sendRedirect(request.getContextPath() + "/login");
        }
        return false;
    }
}
