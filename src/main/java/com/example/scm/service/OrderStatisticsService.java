package com.example.scm.service;

import com.example.scm.common.auth.Authz;
import com.example.scm.common.auth.LoginUser;
import com.example.scm.common.exception.BusinessException;
import com.example.scm.common.exception.ErrorCode;
import com.example.scm.domain.enums.PurchaseOrderStatus;
import com.example.scm.domain.enums.SalesOrderStatus;
import com.example.scm.domain.enums.UserRole;
import com.example.scm.dto.dashboard.OrderStatisticsResponse;
import com.example.scm.dto.dashboard.OrderStatisticsResponse.StatusTotal;
import com.example.scm.repository.OrderStatisticsRow;
import com.example.scm.repository.PurchaseOrderRepository;
import com.example.scm.repository.SalesOrderRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 선택한 기간의 주문을 상태별로 세고 주문 금액을 더합니다.
 * 주문일을 기준으로 계산합니다. 취소한 주문도 따로 표시하므로 이 금액이 모두 실제 매출은 아닙니다.
 */
@Service
@RequiredArgsConstructor
public class OrderStatisticsService {
    private final PurchaseOrderRepository purchases;
    private final SalesOrderRepository sales;

    @Transactional(readOnly = true)
    public OrderStatisticsResponse summarize(LocalDate from, LocalDate to, LoginUser user) {
        // 회사 전체 주문 금액은 경영 정보라 일반 직원에게는 보여 주지 않고, 관리자·매니저만 볼 수 있게 합니다.
        Authz.requireRole(user, UserRole.ADMIN, UserRole.MANAGER);
        LocalDate today = LocalDate.now();
        // null은 입력한 값이 없다는 뜻입니다. 시작일이 없으면 이번 달 1일을 사용합니다.
        LocalDate start = from == null ? today.withDayOfMonth(1) : from;
        LocalDate end = to == null ? today : to;
        // 시작일이 종료일보다 늦으면 거꾸로 된 기간이므로 작업을 멈춥니다.
        if (start.isAfter(end)) throw new BusinessException(ErrorCode.INVALID_DATE_RANGE);
        // 데이터베이스가 상태별 건수·금액을 먼저 계산하고, totals가 결과 표의 빈칸을 0으로 채웁니다.
        // 주문을 전부 읽어 자바에서 더하면 주문이 많을 때 느리고 메모리를 많이 쓰므로, 합계는 DB에 맡깁니다.
        return new OrderStatisticsResponse(start, end,
                totals(PurchaseOrderStatus.values(), purchases.summarize(start, end)),
                totals(SalesOrderStatus.values(), sales.summarize(start, end)));
    }

    // 발주 상태(PurchaseOrderStatus)와 수주 상태(SalesOrderStatus)는 서로 다른 종류지만,
    // 둘 다 Enum이므로 Enum<?>[]로 받아 한 함수로 같은 방식의 표를 만듭니다.
    private List<StatusTotal> totals(Enum<?>[] statuses, List<OrderStatisticsRow> rows) {
        // 상태 이름 → 집계 결과 짝을 만들어 APPROVED 같은 이름으로 계산 결과를 찾습니다.
        Map<String, OrderStatisticsRow> byStatus = rows.stream()
                .collect(Collectors.toMap(OrderStatisticsRow::getStatus, Function.identity()));
        return Arrays.stream(statuses).map(status -> {
            // 주문이 없는 상태도 표에서 빠지지 않도록 건수와 금액을 0으로 넣습니다.
            OrderStatisticsRow row = byStatus.get(status.name());
            return new StatusTotal(status.name(), row == null ? 0 : row.getOrderCount(),
                    row == null ? BigDecimal.ZERO : row.getTotalAmount());
        }).toList();
    }
}
