package com.example.scm.config;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.scm.common.auth.LoginUser;
import com.example.scm.common.auth.SessionConst;
import com.example.scm.domain.enums.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("웹 보안 통합 테스트")
class WebSecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Thymeleaf 로그인 폼에 CSRF 토큰을 자동 렌더링한다")
    void loginForm_containsCsrfToken() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("name=\"_csrf\"")));
    }

    @Test
    @DisplayName("CSRF 토큰이 없는 웹 변경 요청을 거부한다")
    void webMutation_withoutCsrf_isForbidden() throws Exception {
        LoginUser user = new LoginUser(3L, "사용자", "user@scm.com", UserRole.USER);

        mockMvc.perform(post("/logout").sessionAttr(SessionConst.LOGIN_USER, user))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("유효한 CSRF 토큰이 있는 웹 변경 요청은 기존 컨트롤러로 전달한다")
    void webMutation_withCsrf_reachesController() throws Exception {
        LoginUser user = new LoginUser(3L, "사용자", "user@scm.com", UserRole.USER);

        mockMvc.perform(post("/logout")
                        .sessionAttr(SessionConst.LOGIN_USER, user)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    @DisplayName("JSON API 로그인은 기존처럼 CSRF 토큰 없이 사용할 수 있다")
    void apiLogin_remainsCsrfCompatible() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"user@scm.com\",\"password\":\"password1!\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("API 로그아웃은 교차 출처 단순 폼 요청을 허용하지 않는다")
    void apiLogout_rejectsSimpleFormContentType() throws Exception {
        LoginUser user = new LoginUser(3L, "사용자", "user@scm.com", UserRole.USER);

        mockMvc.perform(post("/api/auth/logout")
                        .sessionAttr(SessionConst.LOGIN_USER, user)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isUnsupportedMediaType());
    }
}
