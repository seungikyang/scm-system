package com.example.scm.service;

import com.example.scm.common.exception.BusinessException;
import com.example.scm.common.exception.ErrorCode;
import com.example.scm.domain.User;
import com.example.scm.dto.user.PasswordChangeRequest;
import com.example.scm.dto.user.UserResponse;
import com.example.scm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
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

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "현재 비밀번호가 올바르지 않습니다.");
        }

        // 평문은 저장하지 않는다. 트랜잭션 종료 시 JPA 변경 감지가 UPDATE를 실행한다.
        user.changePassword(passwordEncoder.encode(request.getNewPassword()));
    }
}
