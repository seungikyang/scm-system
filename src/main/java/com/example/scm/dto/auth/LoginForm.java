package com.example.scm.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 로그인 HTML 폼 값을 받는 DTO.
 * {@code th:field}가 기본 생성자와 setter를 사용해 값을 채우고, {@code @Valid}가
 * 아래 Bean Validation 애너테이션을 실행한다. 비밀번호 확인 자체는 AuthService 몫이다.
 */
@Getter
@Setter
@NoArgsConstructor
public class LoginForm {

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;
}
