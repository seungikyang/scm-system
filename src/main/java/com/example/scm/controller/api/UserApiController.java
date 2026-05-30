package com.example.scm.controller.api;

import com.example.scm.common.auth.CurrentUser;
import com.example.scm.common.auth.LoginUser;
import com.example.scm.dto.user.PasswordChangeRequest;
import com.example.scm.dto.user.UserResponse;
import com.example.scm.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe(@CurrentUser LoginUser loginUser) {
        return ResponseEntity.ok(userService.getMe(loginUser.id()));
    }

    @PatchMapping("/me/password")
    public ResponseEntity<Void> changePassword(@CurrentUser LoginUser loginUser,
                                               @Valid @RequestBody PasswordChangeRequest request) {
        userService.changePassword(loginUser.id(), request);
        return ResponseEntity.noContent().build();
    }
}
