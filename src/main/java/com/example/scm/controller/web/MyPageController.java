package com.example.scm.controller.web;

import com.example.scm.common.auth.CurrentUser;
import com.example.scm.common.auth.LoginUser;
import com.example.scm.dto.user.UserResponse;
import com.example.scm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MyPageController {

    private final UserService userService;

    @GetMapping("/me")
    public String myPage(@CurrentUser LoginUser loginUser, Model model) {
        UserResponse user = userService.getMe(loginUser.id());
        model.addAttribute("user", user);
        return "mypage";
    }
}
