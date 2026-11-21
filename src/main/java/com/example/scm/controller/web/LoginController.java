package com.example.scm.controller.web;

import com.example.scm.common.auth.LoginSessionRegistry;
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
 * 로그인 화면을 열고 입력한 이메일과 비밀번호를 담당자에게 전달합니다.
 * 로그인에 성공하면 세션에 출입증을 보관하고, 로그아웃하면 세션을 끝냅니다.
 *
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
    private final LoginSessionRegistry loginSessionRegistry;

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
        // 이메일 형식이나 빈칸에 문제가 있으면 로그인 화면을 다시 보여 주고 수정하게 합니다.
        if (bindingResult.hasErrors()) {
            return "login";
        }
        try {
            // 비밀번호 확인 전에 철회 순번을 받아 둡니다. 확인 도중 비밀번호가 바뀌면 아래 register가 거절합니다.
            long loginEpoch = loginSessionRegistry.beginLogin();
            LoginUser loginUser = authService.login(loginForm.getEmail(), loginForm.getPassword());
            HttpSession session = request.getSession(true);
            // 인증 직후 세션 ID를 교체해 세션 고정 공격을 방지한다.
            // 로그인 전의 방문 번호를 바꿔 다른 사람이 알고 있던 번호로 로그인 상태를 이용하지 못하게 합니다.
            request.changeSessionId();
            // 서버의 임시 보관함인 세션에 로그인 출입증을 넣습니다.
            session.setAttribute(SessionConst.LOGIN_USER, loginUser);
            // 비밀번호 변경 시 다른 기기의 로그인을 끝낼 수 있도록 이 세션을 사용자별 목록에 기록합니다.
            if (!loginSessionRegistry.register(loginUser.id(), session, loginEpoch)) {
                model.addAttribute("errorMessage", "로그인 중 비밀번호가 변경되었습니다. 다시 로그인하세요.");
                return "login";
            }
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
            // 서버가 기억하던 로그인 상태를 끝내 출입증을 더 이상 사용할 수 없게 합니다.
            session.invalidate();
        }
        return "redirect:/login";
    }
}
