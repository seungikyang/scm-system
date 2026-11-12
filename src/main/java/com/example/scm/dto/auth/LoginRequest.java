package com.example.scm.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 다른 프로그램이 JSON으로 보내는 이메일과 비밀번호를 받는 양식입니다.
 * JSON은 필드 이름과 값을 짝지어 보내는 글 형식이며, 입력 검사 후 로그인 담당자에게 전달합니다.
 *
 * 로그인 REST API의 JSON 본문을 받는 DTO. 이메일 형식과 필수 입력을 먼저 검증한다.
 */
@Getter
@Setter
@NoArgsConstructor
public class LoginRequest {

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;
}
