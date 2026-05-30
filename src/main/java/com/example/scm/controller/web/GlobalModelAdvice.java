package com.example.scm.controller.web;

import com.example.scm.common.auth.CurrentUser;
import com.example.scm.common.auth.LoginUser;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * web 계층 모든 뷰에 currentUser(LoginUser, 미로그인 시 null)를 주입한다. nav 프래그먼트가 사용.
 */
@ControllerAdvice(basePackages = "com.example.scm.controller.web")
public class GlobalModelAdvice {

    @ModelAttribute("currentUser")
    public LoginUser currentUser(@CurrentUser LoginUser loginUser) {
        return loginUser;
    }
}
