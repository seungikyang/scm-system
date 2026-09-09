package com.example.scm.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 비밀번호 변경 API의 입력 DTO. 여기서는 빈 값과 길이 같은 형식만 검증하며,
 * 현재 비밀번호 일치 여부와 새 비밀번호 암호화는 UserService가 처리한다.
 */
@Getter
@Setter
@NoArgsConstructor
public class PasswordChangeRequest {

    @NotBlank(message = "현재 비밀번호는 필수입니다.")
    private String currentPassword;

    @NotBlank(message = "새 비밀번호는 필수입니다.")
    @Size(min = 8, max = 64, message = "비밀번호는 8자 이상 64자 이하여야 합니다.")
    private String newPassword;
}
