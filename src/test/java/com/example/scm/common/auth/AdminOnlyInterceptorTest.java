package com.example.scm.common.auth;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.scm.domain.enums.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

@DisplayName("AdminOnlyInterceptor 테스트")
class AdminOnlyInterceptorTest {

    private final AdminOnlyInterceptor interceptor = new AdminOnlyInterceptor();

    @Test
    @DisplayName("일반 사용자의 관리 경로 접근을 403으로 차단한다")
    void regularUser_isForbidden() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.getSession().setAttribute(SessionConst.LOGIN_USER,
                new LoginUser(3L, "사용자", "user@scm.com", UserRole.USER));
        MockHttpServletResponse response = new MockHttpServletResponse();

        boolean allowed = interceptor.preHandle(request, response, new Object());

        assertThat(allowed).isFalse();
        assertThat(response.getStatus()).isEqualTo(403);
    }

    @Test
    @DisplayName("ADMIN은 관리 경로에 접근할 수 있다")
    void admin_isAllowed() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.getSession().setAttribute(SessionConst.LOGIN_USER,
                new LoginUser(1L, "관리자", "admin@scm.com", UserRole.ADMIN));

        boolean allowed = interceptor.preHandle(
                request, new MockHttpServletResponse(), new Object());

        assertThat(allowed).isTrue();
    }
}
