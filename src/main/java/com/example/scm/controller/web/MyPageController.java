package com.example.scm.controller.web;

import com.example.scm.common.auth.CurrentUser;
import com.example.scm.common.auth.LoginUser;
import com.example.scm.dto.user.UserResponse;
import com.example.scm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 로그인한 사용자의 내 정보 화면을 준비합니다.
 * 서버에 기억된 사용자 번호로 정보를 조회해 다른 사람의 정보가 섞이지 않게 합니다.
 *
 * 현재 로그인 사용자의 공개 가능한 정보만 mypage.html에 전달하는 MVC 컨트롤러.
 */
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
