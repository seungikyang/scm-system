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

/**
 * 로그인 자격 증명(이메일·비밀번호)을 확인하는 서비스.
 *
 * <p>학습 모듈 23: UserRepository 조회 → BCrypt matches → LoginUser 변환 순서로 읽은 뒤
 * AuthApiController 또는 LoginController에서 세션에 저장되는 과정을 확인한다.</p>
 *
 * <p>이 클래스는 HTTP 세션을 직접 다루지 않는다. 사용자 확인 후 세션에 넣어도 안전한
 * 작은 객체인 {@link LoginUser}를 반환하고, 세션 생성은 Web/API 컨트롤러가 담당한다.
 * 이렇게 나누면 같은 로그인 규칙을 두 종류의 컨트롤러가 함께 사용할 수 있다.</p>
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * 이메일/비밀번호 검증 후 세션 저장용 LoginUser 반환.
     * 사용자 미존재/비밀번호 불일치 모두 같은 메시지로 처리해 계정 존재 여부가 노출되지 않게 한다.
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
