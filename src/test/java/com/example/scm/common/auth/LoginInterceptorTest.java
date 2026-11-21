package com.example.scm.common.auth;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import tools.jackson.databind.json.JsonMapper;

@DisplayName("LoginInterceptor 테스트")
class LoginInterceptorTest {

    private final LoginInterceptor interceptor =
            new LoginInterceptor(JsonMapper.builder().findAndAddModules().build());

    @Test
    @DisplayName("context path가 있는 미인증 API도 JSON 401로 응답한다")
    void unauthenticatedApi_withContextPath_returnsJson401() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/scm/api/items");
        request.setContextPath("/scm");
        request.setServletPath("/api/items");
        MockHttpServletResponse response = new MockHttpServletResponse();

        boolean allowed = interceptor.preHandle(request, response, new Object());

        assertThat(allowed).isFalse();
        assertThat(response.getStatus()).isEqualTo(401);
        assertThat(response.getContentAsString()).contains("AUTHENTICATION_REQUIRED");
    }

    @Test
    @DisplayName("context path가 있는 웹 요청은 올바른 로그인 경로로 보낸다")
    void unauthenticatedWeb_withContextPath_redirectsToScopedLogin() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/scm/items");
        request.setContextPath("/scm");
        request.setServletPath("/items");
        MockHttpServletResponse response = new MockHttpServletResponse();

        boolean allowed = interceptor.preHandle(request, response, new Object());

        assertThat(allowed).isFalse();
        assertThat(response.getRedirectedUrl()).isEqualTo("/scm/login");
    }
}
