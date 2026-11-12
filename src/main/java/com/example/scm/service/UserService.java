package com.example.scm.service;

import com.example.scm.common.exception.BusinessException;
import com.example.scm.common.exception.ErrorCode;
import com.example.scm.domain.User;
import com.example.scm.dto.user.PasswordChangeRequest;
import com.example.scm.dto.user.UserResponse;
import com.example.scm.repository.UserRepository;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 내 정보를 확인하고 내 비밀번호를 바꾸는 규칙을 처리합니다.
 * 비밀번호를 바꿀 때는 현재 비밀번호가 맞는지 확인한 뒤 새 비밀번호의 해시를 저장합니다.
 *
 * 로그인 사용자의 조회와 비밀번호 변경 규칙을 담당한다.
 * 엔티티를 컨트롤러에 그대로 넘기지 않고 {@link UserResponse}로 변환해 비밀번호 같은
 * 내부 필드가 응답에 섞이지 않도록 한다.
 * 학습 모듈 23에서는 getMe를 먼저 읽고, 현재 비밀번호 확인 → 새 비밀번호 해시 → 변경
 * 감지 순서로 changePassword를 읽는다.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public UserResponse getMe(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        return UserResponse.from(user);
    }

    @Transactional
    public void changePassword(Long userId, PasswordChangeRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 현재 비밀번호가 맞아야 새 비밀번호로 바꿀 수 있습니다.
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "현재 비밀번호가 올바르지 않습니다.");
        }

        // BCrypt는 문자 수가 아닌 UTF-8 바이트 수를 제한한다. 한글·이모지는
        // DTO의 64자 제한 이내여도 72바이트를 넘을 수 있으므로 해시 전에 거부한다.
        if (request.getNewPassword().getBytes(StandardCharsets.UTF_8).length > 72) {
            throw new BusinessException(ErrorCode.INVALID_INPUT,
                    "새 비밀번호는 UTF-8 기준 72바이트 이하여야 합니다.");
        }

        // 평문은 저장하지 않는다. 트랜잭션 종료 시 JPA 변경 감지가 UPDATE를 실행한다.
        // 새 비밀번호도 그대로 저장하지 않고 BCrypt로 해시를 만든 뒤 저장합니다.
        // 여기서 읽어 온 user의 변경을 JPA가 추적합니다. 저장 묶음이 성공하면 바뀐 비밀번호가 반영됩니다.
        user.changePassword(passwordEncoder.encode(request.getNewPassword()));
    }
}
