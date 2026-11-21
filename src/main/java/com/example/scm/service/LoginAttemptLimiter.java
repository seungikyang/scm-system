package com.example.scm.service;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

/**
 * 같은 이메일로 비밀번호를 계속 틀리는 시도를 잠시 막는 장치입니다.
 * 비밀번호를 무작정 대입해 보는 공격(무차별 대입)을 늦추기 위해 사용합니다.
 *
 * <p>업무 규칙: {@code MAX_FAILURES}번 연속으로 틀리면 {@code LOCK_DURATION} 동안 그 이메일의
 * 로그인을 거절합니다. 잠금 중에는 맞는 비밀번호를 넣어도 거절해야 공격자가 잠금 중에도
 * 정답을 확인하지 못합니다. 로그인에 성공하면 실패 기록을 지웁니다.</p>
 *
 * <p>주의: 기록은 이 서버 메모리에만 있어 재시작하면 사라지고, 서버를 여러 대 띄우면 서버마다
 * 따로 셉니다. 여러 대로 운영할 때는 프록시나 공용 저장소의 제한을 함께 사용해야 합니다.</p>
 */
@Component
public class LoginAttemptLimiter {

    static final int MAX_FAILURES = 5;
    static final Duration LOCK_DURATION = Duration.ofMinutes(5);
    // 아무 이메일이나 계속 보내 메모리를 채우는 일을 막기 위해, 기록이 이만큼 쌓이면 오래된 기록을 정리합니다.
    private static final int CLEANUP_THRESHOLD = 10_000;

    private final Map<String, Attempt> attempts = new ConcurrentHashMap<>();
    private final Clock clock;

    public LoginAttemptLimiter() {
        this(Clock.systemUTC());
    }

    LoginAttemptLimiter(Clock clock) {
        this.clock = clock;
    }

    /** 지금 이 이메일의 로그인이 잠겨 있으면 true를 돌려줍니다. */
    public boolean isLocked(String email) {
        Attempt attempt = attempts.get(key(email));
        return attempt != null && attempt.lockedUntil() != null && clock.instant().isBefore(attempt.lockedUntil());
    }

    /** 비밀번호가 틀렸을 때 부릅니다. 정해진 횟수에 도달하면 잠금 시각을 기록합니다. */
    public void recordFailure(String email) {
        if (attempts.size() >= CLEANUP_THRESHOLD) {
            removeExpired();
        }
        Instant now = clock.instant();
        // compute는 같은 이메일에 대한 동시 요청이 서로의 횟수를 덮어쓰지 않도록 한 번에 계산합니다.
        attempts.compute(key(email), (k, old) -> {
            // 잠금이 끝난 기록은 처음부터 다시 셉니다.
            int failures = (old == null || old.isExpired(now)) ? 1 : old.failures() + 1;
            Instant lockedUntil = failures >= MAX_FAILURES ? now.plus(LOCK_DURATION) : null;
            return new Attempt(failures, lockedUntil);
        });
    }

    /** 로그인에 성공하면 그동안의 실패 기록을 지웁니다. */
    public void recordSuccess(String email) {
        attempts.remove(key(email));
    }

    private void removeExpired() {
        Instant now = clock.instant();
        attempts.entrySet().removeIf(entry -> entry.getValue().isExpired(now));
        // 잠금이 없는 기록(아직 횟수만 센 기록)까지 너무 많으면 모두 비워 메모리 사용량에 상한을 둡니다.
        if (attempts.size() >= CLEANUP_THRESHOLD) {
            attempts.clear();
        }
    }

    // 대소문자나 앞뒤 공백만 바꿔 제한을 피하지 못하도록 같은 이메일로 취급합니다.
    private static String key(String email) {
        return email == null ? "" : email.trim().toLowerCase(Locale.ROOT);
    }

    private record Attempt(int failures, Instant lockedUntil) {
        boolean isExpired(Instant now) {
            return lockedUntil != null && !now.isBefore(lockedUntil);
        }
    }
}
