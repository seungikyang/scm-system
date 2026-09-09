package com.example.scm.controller.web;

import com.example.scm.common.auth.LoginUser;
import com.example.scm.common.auth.SessionConst;
import com.example.scm.common.exception.BusinessException;
import com.example.scm.dto.auth.LoginForm;
import com.example.scm.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Thymeleaf 로그인 화면과 폼 제출을 처리하는 MVC 컨트롤러.
 *
 * <p>학습 모듈 23: AuthApiController 다음에 읽고, 같은 인증 규칙이 JSON 응답 대신
 * login.html과 redirect로 이어지는 차이를 비교한다.</p>
 *
 * <p>문자열 {@code "login"}은 templates/login.html을 렌더링하라는 뜻이고,
 * {@code "redirect:/"}는 브라우저에 새 GET 요청을 보내라는 뜻이다. 로그인 규칙은
 * REST API와 똑같이 AuthService를 재사용한다.</p>
 */
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final AuthService authService;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm loginForm) {
        return "login";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("loginForm") LoginForm loginForm,
                        BindingResult bindingResult,
                        HttpServletRequest request,
                        Model model) {
        // BindingResult는 @Valid 대상 바로 뒤에 두어 입력 오류를 예외 대신 화면에 표시한다.
        if (bindingResult.hasErrors()) {
            return "login";
        }
        try {
            LoginUser loginUser = authService.login(loginForm.getEmail(), loginForm.getPassword());
            HttpSession session = request.getSession(true);
            // 인증 직후 세션 ID를 교체해 세션 고정 공격을 방지한다.
            request.changeSessionId();
            session.setAttribute(SessionConst.LOGIN_USER, loginUser);
            return "redirect:/";
        } catch (BusinessException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "login";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/login";
    }
}
