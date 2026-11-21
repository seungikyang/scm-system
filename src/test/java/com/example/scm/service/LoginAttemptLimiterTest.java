package com.example.scm.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("LoginAttemptLimiter 단위 테스트")
class LoginAttemptLimiterTest {

    /** 테스트에서 시간을 직접 앞으로 돌리기 위한 시계입니다. */
    private static final class MutableClock extends Clock {
        private Instant now = Instant.parse("2026-11-21T00:00:00Z");

        void advance(Duration duration) {
            now = now.plus(duration);
        }

        @Override public ZoneOffset getZone() { return ZoneOffset.UTC; }
        @Override public Clock withZone(java.time.ZoneId zone) { return this; }
        @Override public Instant instant() { return now; }
    }

    @Test
    @DisplayName("정해진 횟수 전에는 잠그지 않고, 도달하면 잠근 뒤 잠금 시간이 지나면 풀린다")
    void locksAtThresholdAndUnlocksAfterDuration() {
        MutableClock clock = new MutableClock();
        LoginAttemptLimiter limiter = new LoginAttemptLimiter(clock);

        for (int i = 0; i < LoginAttemptLimiter.MAX_FAILURES - 1; i++) {
            limiter.recordFailure("a@scm.com");
        }
        assertThat(limiter.isLocked("a@scm.com")).isFalse();

        limiter.recordFailure("a@scm.com");
        assertThat(limiter.isLocked("a@scm.com")).isTrue();
        // 다른 이메일에는 영향을 주지 않는다.
        assertThat(limiter.isLocked("b@scm.com")).isFalse();

        clock.advance(LoginAttemptLimiter.LOCK_DURATION);
        assertThat(limiter.isLocked("a@scm.com")).isFalse();
        // 잠금이 끝난 뒤의 실패는 1회부터 다시 센다.
        limiter.recordFailure("a@scm.com");
        assertThat(limiter.isLocked("a@scm.com")).isFalse();
    }

    @Test
    @DisplayName("로그인에 성공하면 실패 기록을 지운다")
    void successResetsFailures() {
        LoginAttemptLimiter limiter = new LoginAttemptLimiter();
        for (int i = 0; i < LoginAttemptLimiter.MAX_FAILURES - 1; i++) {
            limiter.recordFailure("a@scm.com");
        }
        limiter.recordSuccess(" A@SCM.com ");
        limiter.recordFailure("a@scm.com");
        assertThat(limiter.isLocked("a@scm.com")).isFalse();
    }
}
