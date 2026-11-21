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
 * 입력한 이메일과 비밀번호가 맞는지 확인하는 담당자입니다.
 * 이메일로 사용자를 찾고 비밀번호를 검사한 뒤, 비밀번호를 뺀 로그인 출입증을 만듭니다.
 *
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
    private final LoginAttemptLimiter loginAttemptLimiter;

    // 없는 이메일일 때도 BCrypt 비교를 한 번 실행하기 위한 가짜 해시입니다("unused-dummy-password"의 해시).
    // 비교를 건너뛰면 응답이 눈에 띄게 빨라져, 응답 시간만으로 가입된 이메일인지 알아낼 수 있기 때문입니다.
    private static final String DUMMY_HASH = "$2a$10$9Am/4XKKtv1lnbtY35t7buQkBrBw0wZPcqlSINmlZWToXr1QjQruq";

    /**
     * 이메일/비밀번호 검증 후 세션 저장용 LoginUser 반환.
     * 사용자 미존재/비밀번호 불일치 모두 같은 메시지로 처리해 계정 존재 여부가 노출되지 않게 한다.
     */
    @Transactional(readOnly = true)
    public LoginUser login(String email, String rawPassword) {
        // readOnly는 조회를 위한 설정입니다. 비밀번호가 맞는지는 아래의 검사로 따로 확인합니다.
        // 0. 연속 실패로 잠긴 이메일이면 비밀번호를 확인하지 않고 거절합니다. 잠금 중에 정답 여부를 알려 주지 않기 위해서입니다.
        if (loginAttemptLimiter.isLocked(email)) {
            throw new BusinessException(ErrorCode.TOO_MANY_LOGIN_ATTEMPTS);
        }

        // 1. 이메일로 사용자 장부를 찾습니다. 없는 이메일도 실패 횟수를 세어 이메일 존재 여부와 상관없이 같은 규칙을 적용합니다.
        User user = userRepository.findByEmail(email).orElse(null);

        // 2. 해시는 복호화하지 않습니다. 입력한 비밀번호가 저장된 해시와 맞는지 도구로 검사합니다.
        //    사용자가 없을 때도 가짜 해시와 비교해 처리 시간을 비슷하게 맞춥니다.
        String storedHash = user == null ? DUMMY_HASH : user.getPassword();
        boolean matches = passwordEncoder.matches(rawPassword, storedHash);
        if (user == null || !matches) {
            loginAttemptLimiter.recordFailure(email);
            throw new BusinessException(ErrorCode.INVALID_INPUT, "이메일 또는 비밀번호가 올바르지 않습니다.");
        }
        loginAttemptLimiter.recordSuccess(email);

        // 3. 로그인 성공 결과에는 비밀번호를 담지 않고 출입증에 필요한 정보만 담습니다.
        return new LoginUser(user.getId(), user.getName(), user.getEmail(), user.getRole());
    }
}
