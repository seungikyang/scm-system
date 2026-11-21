package com.example.scm.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import com.example.scm.common.auth.LoginUser;
import com.example.scm.common.exception.BusinessException;
import com.example.scm.common.exception.ErrorCode;
import com.example.scm.domain.User;
import com.example.scm.domain.enums.UserRole;
import com.example.scm.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService 단위 테스트")
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    // 실패 횟수 규칙까지 함께 확인하도록 실제 제한기를 사용합니다.
    @org.mockito.Spy
    private LoginAttemptLimiter loginAttemptLimiter = new LoginAttemptLimiter();

    @InjectMocks
    private AuthService service;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .email("user@scm.com")
                .password("encoded-password")
                .name("사용자")
                .role(UserRole.USER)
                .build();
        ReflectionTestUtils.setField(user, "id", 10L);
    }

    @Test
    @DisplayName("등록된 사용자와 올바른 비밀번호면 세션용 사용자 정보를 반환한다")
    void login_success() {
        given(userRepository.findByEmail("user@scm.com")).willReturn(Optional.of(user));
        given(passwordEncoder.matches("password1!", "encoded-password")).willReturn(true);

        LoginUser result = service.login("user@scm.com", "password1!");

        assertThat(result.id()).isEqualTo(10L);
        assertThat(result.email()).isEqualTo("user@scm.com");
        assertThat(result.role()).isEqualTo(UserRole.USER);
    }

    @Test
    @DisplayName("존재하지 않는 이메일은 계정 존재 여부를 숨긴 공통 로그인 실패로 처리한다")
    void login_unknownEmail() {
        given(userRepository.findByEmail("missing@scm.com")).willReturn(Optional.empty());

        assertLoginFailure(() -> service.login("missing@scm.com", "password1!"));
    }

    @Test
    @DisplayName("틀린 비밀번호도 이메일 오류와 같은 메시지로 처리한다")
    void login_wrongPassword() {
        given(userRepository.findByEmail("user@scm.com")).willReturn(Optional.of(user));
        given(passwordEncoder.matches("wrong-password", "encoded-password")).willReturn(false);

        assertLoginFailure(() -> service.login("user@scm.com", "wrong-password"));
    }

    @Test
    @DisplayName("연속으로 5번 틀리면 맞는 비밀번호도 잠시 거절해 무차별 대입을 늦춘다")
    void login_locksAfterRepeatedFailures() {
        given(userRepository.findByEmail("user@scm.com")).willReturn(Optional.of(user));
        given(passwordEncoder.matches("wrong-password", "encoded-password")).willReturn(false);
        for (int i = 0; i < LoginAttemptLimiter.MAX_FAILURES; i++) {
            assertLoginFailure(() -> service.login("user@scm.com", "wrong-password"));
        }

        // 대소문자만 바꾼 이메일로도 잠금을 피할 수 없어야 한다.
        assertThatThrownBy(() -> service.login("USER@scm.com", "password1!"))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> ((BusinessException) e).getErrorCode())
                .isEqualTo(ErrorCode.TOO_MANY_LOGIN_ATTEMPTS);
    }

    @Test
    @DisplayName("없는 이메일도 BCrypt 비교를 실행해 응답 시간으로 계정 존재를 알 수 없게 한다")
    void login_unknownEmail_stillRunsPasswordCheck() {
        given(userRepository.findByEmail("missing@scm.com")).willReturn(Optional.empty());

        assertLoginFailure(() -> service.login("missing@scm.com", "password1!"));

        org.mockito.Mockito.verify(passwordEncoder)
                .matches(org.mockito.ArgumentMatchers.eq("password1!"), org.mockito.ArgumentMatchers.startsWith("$2a$10$"));
    }

    private void assertLoginFailure(org.junit.jupiter.api.function.Executable executable) {
        assertThatThrownBy(executable::execute)
                .isInstanceOf(BusinessException.class)
                .satisfies(error -> {
                    BusinessException exception = (BusinessException) error;
                    assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.INVALID_INPUT);
                    assertThat(exception.getMessage()).isEqualTo("이메일 또는 비밀번호가 올바르지 않습니다.");
                });
    }
}
