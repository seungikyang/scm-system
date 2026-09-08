package com.example.scm.controller.web;

import com.example.scm.common.auth.CurrentUser;
import com.example.scm.common.auth.LoginUser;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * 모든 웹 화면의 Model에 로그인 사용자를 공통으로 넣는다.
 *
 * <p>학습 모듈 35의 시작점이다. 이 파일 → ItemWebController → layout.html → item 템플릿
 * → 발주 템플릿과 JavaScript 순서로 읽는다.</p>
 *
 * <p>각 컨트롤러가 {@code model.addAttribute("currentUser", ...)}를 반복하지 않아도
 * layout.html의 내비게이션이 이름과 권한별 메뉴를 표시할 수 있다. 미로그인 상태에서는
 * null이 들어간다.</p>
 */
@ControllerAdvice(basePackages = "com.example.scm.controller.web")
public class GlobalModelAdvice {

    @ModelAttribute("currentUser")
    public LoginUser currentUser(@CurrentUser LoginUser loginUser) {
        return loginUser;
    }
}
