package com.example.scm.common.auth;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;

@DisplayName("LoginSessionRegistry 단위 테스트")
class LoginSessionRegistryTest {

    @Test
    @DisplayName("비밀번호 확인을 시작한 뒤 비밀번호가 바뀌면, 늦게 등록되는 세션도 거절하고 끝낸다")
    void loginRacingWithPasswordChange_isRejected() {
        LoginSessionRegistry registry = new LoginSessionRegistry();
        MockHttpSession changer = new MockHttpSession();
        registry.register(1L, changer, registry.beginLogin());

        // 다른 기기가 예전 비밀번호로 로그인 확인을 시작한 뒤(순번 확보),
        long racingEpoch = registry.beginLogin();
        // 등록하기 전에 비밀번호 변경과 철회가 끝난 상황.
        registry.invalidateOthers(1L, changer);
        MockHttpSession racing = new MockHttpSession();

        assertThat(registry.register(1L, racing, racingEpoch)).isFalse();
        assertThat(racing.isInvalid()).isTrue();
        assertThat(changer.isInvalid()).isFalse();
    }

    @Test
    @DisplayName("철회 뒤에 새로 시작한 로그인과 다른 사용자의 로그인은 영향을 받지 않는다")
    void loginsAfterRevocation_andOtherUsers_areKept() {
        LoginSessionRegistry registry = new LoginSessionRegistry();
        MockHttpSession changer = new MockHttpSession();
        registry.register(1L, changer, registry.beginLogin());
        long otherUserEpoch = registry.beginLogin();
        registry.invalidateOthers(1L, changer);

        MockHttpSession fresh = new MockHttpSession();
        assertThat(registry.register(1L, fresh, registry.beginLogin())).isTrue();
        MockHttpSession otherUser = new MockHttpSession();
        assertThat(registry.register(2L, otherUser, otherUserEpoch)).isTrue();
        assertThat(fresh.isInvalid()).isFalse();
        assertThat(otherUser.isInvalid()).isFalse();
    }
}
