package com.example.scm.repository;

import java.math.BigDecimal;

/**
 * 데이터베이스에서 계산한 상태 한 종류의 주문 건수와 금액입니다.
 * 예를 들어 확정 수주 3건과 그 주문 금액 합계를 한 줄로 받습니다.
 *
 * 헤더만 집계해 여러 라인으로 인해 건수/금액이 중복되지 않도록 한다.
 */
public interface OrderStatisticsRow {
    String getStatus();
    long getOrderCount();
    BigDecimal getTotalAmount();
}
