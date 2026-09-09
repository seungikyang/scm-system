package com.example.scm.controller.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.example.scm.common.auth.LoginUser;
import com.example.scm.common.auth.SessionConst;
import com.example.scm.domain.enums.UserRole;
import com.example.scm.dto.auth.LoginForm;
import com.example.scm.service.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.validation.BeanPropertyBindingResult;

@ExtendWith(MockitoExtension.class)
@DisplayName("LoginController 테스트")
class LoginControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private LoginController controller;

    @Test
    @DisplayName("로그인 성공 시 세션 ID를 교체해 session fixation을 방지한다")
    void login_rotatesSessionId() {
        LoginForm form = new LoginForm();
        form.setEmail("user@scm.com");
        form.setPassword("password1!");
        LoginUser loginUser =
                new LoginUser(1L, "사용자", "user@scm.com", UserRole.USER);
        given(authService.login(form.getEmail(), form.getPassword())).willReturn(loginUser);

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpSession session = new MockHttpSession();
        request.setSession(session);
        String previousSessionId = session.getId();

        String view = controller.login(form, new BeanPropertyBindingResult(form, "loginForm"),
                request, new ExtendedModelMap());

        assertThat(view).isEqualTo("redirect:/");
        assertThat(session.getId()).isNotEqualTo(previousSessionId);
        assertThat(session.getAttribute(SessionConst.LOGIN_USER)).isEqualTo(loginUser);
    }
}
