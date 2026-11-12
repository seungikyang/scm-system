package com.example.scm.service;

import com.example.scm.common.exception.BusinessException;
import com.example.scm.common.exception.ErrorCode;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 발주번호·수주번호가 겹쳤을 때 다시 시도하는 횟수와 대기 시간을 정합니다.
 *
 * <p>번호는 "오늘 가장 큰 번호 + 1"로 만들기 때문에, 동시에 들어온 요청들은 같은 번호를 계산합니다.
 * DB UNIQUE 제약이 한 건만 저장하고 나머지는 다시 시도하는데, 곧바로 다시 시도하면 남은 요청들이
 * 또 같은 순간에 같은 번호를 계산해 계속 부딪힙니다. 그래서 시도 사이에 무작위로 조금씩 다르게
 * 기다려(지터) 요청들이 한 줄로 흩어지게 합니다. 기다리는 상한은 시도할수록 두 배로 늘어납니다.</p>
 *
 * <p>최악의 경우 총 대기 시간은 1초 안팎이라 사용자가 체감하기 어렵고, 그 안에 저장하지 못하면
 * 기존처럼 채번 실패 오류를 돌려줍니다.</p>
 */
final class OrderNumberRetry {

    /** 번호 충돌 시 저장을 시도하는 최대 횟수(첫 시도 포함). */
    static final int MAX_ATTEMPTS = 10;

    private static final long BASE_DELAY_MILLIS = 5;
    private static final long MAX_DELAY_MILLIS = 200;

    private OrderNumberRetry() {
    }

    /**
     * attempt번째 시도가 번호 충돌로 실패한 뒤 다음 시도 전에 잠깐 기다린다.
     * 대기 상한은 5ms, 10ms, 20ms ... 로 늘어나며 200ms를 넘지 않는다.
     */
    static void backoff(int attempt) {
        long upperBound = Math.min(MAX_DELAY_MILLIS, BASE_DELAY_MILLIS << Math.min(attempt, 10));
        long delay = ThreadLocalRandom.current().nextLong(upperBound + 1);
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            // 서버 종료 등으로 스레드가 중단되면 중단 표시를 되살리고 더 기다리지 않는다.
            Thread.currentThread().interrupt();
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, "주문번호 채번이 중단되었습니다. 다시 시도해 주세요.");
        }
    }
}
