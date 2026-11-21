package com.example.scm.dto.dashboard;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 조회 기간과 발주·수주의 상태별 건수, 금액을 담은 통계 결과입니다.
 * 안쪽 StatusTotal은 상태 한 종류의 결과입니다. 예를 들어 출고 주문 2건의 금액 합계입니다.
 */
public record OrderStatisticsResponse(LocalDate from, LocalDate to,
                                      List<StatusTotal> purchaseOrders, List<StatusTotal> salesOrders) {
    public record StatusTotal(String status, long orderCount, BigDecimal totalAmount) {}
}
