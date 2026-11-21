package com.example.scm.common.auth;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Component;

/**
 * 사용자별로 지금 로그인되어 있는 세션을 기억합니다.
 * 비밀번호를 바꾸면 다른 브라우저·기기에 남아 있던 로그인을 끝내기 위해 사용합니다.
 *
 * <p>업무 규칙: 비밀번호가 새어 나가 바꾼 경우, 예전 비밀번호로 이미 로그인한 다른 사람이 계속
 * 사용할 수 있으면 안 됩니다. 그래서 {@link #invalidateOthers}로 지금 요청한 세션만 남기고 끝냅니다.</p>
 *
 * <p>{@link HttpSessionListener}를 구현한 Bean은 Spring Boot가 내장 서버에 자동 등록합니다.
 * 세션이 만료·로그아웃으로 끝나면 {@link #sessionDestroyed}가 불려 기록을 지우므로 메모리가 계속 늘지 않습니다.
 * 주의: 서버 메모리 기준이라 서버를 여러 대 띄우면 각 서버의 세션만 알 수 있습니다.</p>
 */
@Component
public class LoginSessionRegistry implements HttpSessionListener {

    private final Map<Long, Set<HttpSession>> sessionsByUser = new ConcurrentHashMap<>();
    // 비밀번호 변경(세션 철회)이 일어날 때마다 1씩 늘어나는 전체 순번입니다.
    private final AtomicLong epoch = new AtomicLong();
    // 사용자별로 마지막 철회가 일어난 순번입니다. 이 순번보다 먼저 시작한 로그인은 예전 비밀번호로 확인했을 수 있습니다.
    private final Map<Long, Long> revokedAtEpoch = new ConcurrentHashMap<>();

    /**
     * 로그인 확인(비밀번호 비교)을 시작하기 직전에 불러 현재 순번을 받아 둡니다.
     * 받은 값은 {@link #register}에 그대로 넘깁니다.
     */
    public long beginLogin() {
        return epoch.get();
    }

    /**
     * 로그인 성공 직후, 세션에 LoginUser를 넣은 다음에 부릅니다.
     *
     * <p>주의: 로그인 확인과 등록 사이에 같은 사용자의 비밀번호가 바뀌면, 이 로그인은 예전 비밀번호로
     * 확인했을 수 있습니다. 그 경우 세션을 바로 끝내고 false를 돌려줍니다. 호출한 쪽은 로그인 실패로 안내합니다.</p>
     *
     * @param loginEpoch {@link #beginLogin}이 돌려준 값
     * @return 세션을 유지해도 되면 true
     */
    public boolean register(Long userId, HttpSession session, long loginEpoch) {
        // compute는 같은 사용자 기록을 한 번에 하나씩 바꿉니다. 세션 종료 정리와 동시에 일어나도 기록이 빠지지 않습니다.
        sessionsByUser.compute(userId, (id, sessions) -> {
            Set<HttpSession> target = sessions == null ? ConcurrentHashMap.newKeySet() : sessions;
            target.add(session);
            return target;
        });
        // 먼저 목록에 넣고 나서 철회 순번을 확인합니다. 철회 쪽은 순번을 먼저 올리고 목록을 훑으므로,
        // 두 작업이 동시에 일어나도 "철회가 이 세션을 보거나, 이 등록이 철회 순번을 보거나" 둘 중 하나는 반드시 일어납니다.
        if (revokedAtEpoch.getOrDefault(userId, -1L) > loginEpoch) {
            removeAndInvalidate(userId, session);
            return false;
        }
        return true;
    }

    /** 현재 세션을 제외한 같은 사용자의 다른 세션을 모두 끝냅니다. 비밀번호 변경이 저장된 뒤에 부릅니다. */
    public void invalidateOthers(Long userId, HttpSession current) {
        // 순번을 먼저 올려, 지금 진행 중인(예전 비밀번호로 확인했을 수 있는) 로그인이 등록 단계에서 거절되게 합니다.
        revokedAtEpoch.put(userId, epoch.incrementAndGet());
        Set<HttpSession> sessions = sessionsByUser.get(userId);
        if (sessions == null) {
            return;
        }
        for (HttpSession session : sessions) {
            // 세션 객체가 같은지로 비교합니다. 로그인 때 changeSessionId로 번호가 바뀌어도 객체는 같습니다.
            if (session != current) {
                removeAndInvalidate(userId, session);
            }
        }
    }

    private void removeAndInvalidate(Long userId, HttpSession session) {
        Set<HttpSession> sessions = sessionsByUser.get(userId);
        if (sessions != null) {
            sessions.remove(session);
        }
        try {
            session.invalidate();
        } catch (IllegalStateException alreadyInvalidated) {
            // 이미 만료된 세션은 끝낼 필요가 없으므로 기록만 지웁니다.
        }
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        HttpSession destroyed = event.getSession();
        // 빈 기록은 null을 돌려 지웁니다. 사용자별로 원자적으로 처리해 새 로그인 등록과 충돌하지 않습니다.
        for (Long userId : sessionsByUser.keySet()) {
            sessionsByUser.computeIfPresent(userId, (id, sessions) -> {
                sessions.remove(destroyed);
                return sessions.isEmpty() ? null : sessions;
            });
        }
    }
}
