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
        if (bindingResult.hasErrors()) {
            return "login";
        }
        try {
            LoginUser loginUser = authService.login(loginForm.getEmail(), loginForm.getPassword());
            HttpSession session = request.getSession(true);
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
