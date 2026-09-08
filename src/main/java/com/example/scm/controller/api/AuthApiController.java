package com.example.scm.controller.api;

import com.example.scm.common.auth.LoginUser;
import com.example.scm.common.auth.SessionConst;
import com.example.scm.dto.auth.LoginRequest;
import com.example.scm.dto.user.UserResponse;
import com.example.scm.service.AuthService;
import com.example.scm.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * JSON 기반 로그인·로그아웃 API의 진입점.
 *
 * <p>학습 모듈 23: AuthService를 먼저 읽은 뒤 로그인 성공 → 세션 생성 → 세션 ID 교체
 * → LoginUser 저장 → 응답 변환 순서로 따라간다.</p>
 *
 * <p>{@code @RestController}의 반환값은 화면 이름이 아니라 JSON/HTTP 응답 본문이 된다.
 * 비밀번호 검증은 AuthService에 맡기고, 이 컨트롤러는 HTTP 세션을 생성하거나 폐기하는
 * 웹 계층의 책임만 담당한다.</p>
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthApiController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@Valid @RequestBody LoginRequest request,
                                              HttpServletRequest httpRequest) {
        LoginUser loginUser = authService.login(request.getEmail(), request.getPassword());
        // true: 세션이 없으면 새로 만든다. 로그인 성공 전에는 세션에 사용자를 넣지 않는다.
        HttpSession session = httpRequest.getSession(true);
        // 로그인 전 세션 ID를 그대로 쓰지 않아 세션 고정(session fixation) 공격을 막는다.
        httpRequest.changeSessionId();
        session.setAttribute(SessionConst.LOGIN_USER, loginUser);
        return ResponseEntity.ok(userService.getMe(loginUser.id()));
    }

    @PostMapping(value = "/logout", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> logout(HttpServletRequest httpRequest) {
        // false: 로그아웃 때문에 빈 세션을 새로 만들지는 않는다.
        HttpSession session = httpRequest.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.noContent().build();
    }
}
