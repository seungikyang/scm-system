package com.example.scm.service;

import com.example.scm.common.exception.BusinessException;
import com.example.scm.common.exception.ErrorCode;
import com.example.scm.repository.SalesOrderRepository;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 수주서에 날짜와 순번으로 된 번호를 붙입니다.
 * 예: SO-20261003-0001. 번호가 겹칠 때 다시 저장하는 처리는 SalesOrderService가 맡습니다.
 *
 * 날짜별 SO 번호 생성. UNIQUE 충돌은 별도 트랜잭션으로 재시도한다.
 */
@Component
@RequiredArgsConstructor
public class SalesOrderNumberGenerator {

    private static final String PREFIX = "SO-";
    private static final int MAX_DAILY_SEQUENCE = 9999;
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final SalesOrderRepository salesOrderRepository;

    /** 해당 일자의 다음 수주번호를 생성한다. (SO-YYYYMMDD-####) */
    public String generate(LocalDate date) {
        String datePart = date.format(DATE_FORMAT);
        String dayPrefix = PREFIX + datePart + "-";
        String latestOrderNumber = salesOrderRepository.findMaxOrderNumber(dayPrefix);
        // PO는 사 오는 주문, SO는 팔아 주는 주문의 표시입니다. 날짜별 순번은 서로 따로 매깁니다.
        // 오늘 번호가 하나도 없으면 1번, 있으면 마지막 순번에 1을 더합니다.
        long sequence = (latestOrderNumber == null)
                ? 1
                : parseSequence(latestOrderNumber, dayPrefix) + 1;
        if (sequence > MAX_DAILY_SEQUENCE) {
            throw new BusinessException(ErrorCode.INTERNAL_ERROR,
                    "당일 수주번호가 9999건을 초과했습니다.");
        }
        // %04d는 1을 0001처럼 네 자리로 맞춰 쓰라는 뜻입니다.
        return String.format("%s%04d", dayPrefix, sequence);
    }

    private long parseSequence(String orderNumber, String dayPrefix) {
        if (!orderNumber.startsWith(dayPrefix)) {
            throw invalidStoredOrderNumber(orderNumber);
        }
        String suffix = orderNumber.substring(dayPrefix.length());
        if (suffix.length() != 4 || !suffix.chars().allMatch(Character::isDigit)) {
            throw invalidStoredOrderNumber(orderNumber);
        }
        return Long.parseLong(suffix);
    }

    private BusinessException invalidStoredOrderNumber(String orderNumber) {
        return new BusinessException(ErrorCode.INTERNAL_ERROR,
                "저장된 수주번호 형식이 올바르지 않습니다: " + orderNumber);
    }
}
