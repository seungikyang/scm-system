package com.example.scm.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.example.scm.common.exception.BusinessException;
import com.example.scm.common.exception.ErrorCode;
import com.example.scm.domain.User;
import com.example.scm.domain.enums.UserRole;
import com.example.scm.dto.user.PasswordChangeRequest;
import com.example.scm.dto.user.UserResponse;
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
@DisplayName("UserService 단위 테스트")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService service;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .email("user@scm.com")
                .password("old-encoded")
                .name("사용자")
                .role(UserRole.USER)
                .build();
        ReflectionTestUtils.setField(user, "id", 10L);
    }

    @Test
    @DisplayName("내 정보 응답에는 비밀번호 없이 공개 가능한 필드만 포함한다")
    void getMe_success() {
        given(userRepository.findById(10L)).willReturn(Optional.of(user));

        UserResponse result = service.getMe(10L);

        assertThat(result.getUserId()).isEqualTo(10L);
        assertThat(result.getEmail()).isEqualTo("user@scm.com");
        assertThat(result.getName()).isEqualTo("사용자");
        assertThat(result.getRole()).isEqualTo(UserRole.USER);
    }

    @Test
    @DisplayName("존재하지 않는 사용자의 내 정보 조회는 USER_NOT_FOUND다")
    void getMe_notFound() {
        given(userRepository.findById(404L)).willReturn(Optional.empty());

        assertCode(() -> service.getMe(404L), ErrorCode.USER_NOT_FOUND);
    }

    @Test
    @DisplayName("현재 비밀번호가 맞으면 새 비밀번호를 해시해 변경한다")
    void changePassword_success() {
        given(userRepository.findById(10L)).willReturn(Optional.of(user));
        given(passwordEncoder.matches("old-password", "old-encoded")).willReturn(true);
        given(passwordEncoder.encode("new-password1!")).willReturn("new-encoded");

        service.changePassword(10L, passwordRequest("old-password", "new-password1!"));

        assertThat(user.getPassword()).isEqualTo("new-encoded");
        verify(passwordEncoder).encode("new-password1!");
    }

    @Test
    @DisplayName("현재 비밀번호가 틀리면 새 비밀번호를 해시하거나 저장하지 않는다")
    void changePassword_wrongCurrentPassword() {
        given(userRepository.findById(10L)).willReturn(Optional.of(user));
        given(passwordEncoder.matches("wrong-password", "old-encoded")).willReturn(false);

        assertCode(
                () -> service.changePassword(10L, passwordRequest("wrong-password", "new-password1!")),
                ErrorCode.INVALID_INPUT);
        assertThat(user.getPassword()).isEqualTo("old-encoded");
        verify(passwordEncoder, never()).encode("new-password1!");
    }

    private PasswordChangeRequest passwordRequest(String currentPassword, String newPassword) {
        PasswordChangeRequest request = new PasswordChangeRequest();
        request.setCurrentPassword(currentPassword);
        request.setNewPassword(newPassword);
        return request;
    }

    private void assertCode(org.junit.jupiter.api.function.Executable executable, ErrorCode expected) {
        assertThatThrownBy(executable::execute)
                .isInstanceOf(BusinessException.class)
                .extracting(error -> ((BusinessException) error).getErrorCode())
                .isEqualTo(expected);
    }
}
