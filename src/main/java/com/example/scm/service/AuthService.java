package com.example.scm.service;

import com.example.scm.common.auth.LoginUser;
import com.example.scm.common.exception.BusinessException;
import com.example.scm.common.exception.ErrorCode;
import com.example.scm.domain.User;
import com.example.scm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * 이메일/비밀번호 검증 후 세션 저장용 LoginUser 반환.
     * 사용자 미존재/비밀번호 불일치 모두 동일 메시지로 INVALID_INPUT 처리(계정 존재 노출 방지).
     */
    @Transactional(readOnly = true)
    public LoginUser login(String email, String rawPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.INVALID_INPUT, "이메일 또는 비밀번호가 올바르지 않습니다."));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        return new LoginUser(user.getId(), user.getName(), user.getEmail(), user.getRole());
    }
}
