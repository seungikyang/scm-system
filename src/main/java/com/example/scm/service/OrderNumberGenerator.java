package com.example.scm.service;

import com.example.scm.common.exception.BusinessException;
import com.example.scm.common.exception.ErrorCode;
import com.example.scm.repository.PurchaseOrderRepository;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 발주번호 채번 (OQ-14, datamodel §7). 형식: PO-YYYYMMDD-#### (일자별 4자리 시퀀스, 0001부터).
 * 시퀀스 산출: 당일 최대 번호의 suffix + 1. 중간 번호가 삭제돼도 중복 번호를 반복하지 않는다.
 * 동시성 최종 방어선은 order_number UNIQUE 제약 + Service 의 재채번 재시도 루프.
 */
@Component
@RequiredArgsConstructor
public class OrderNumberGenerator {

    private static final String PREFIX = "PO-";
    private static final int MAX_DAILY_SEQUENCE = 9999;
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final PurchaseOrderRepository purchaseOrderRepository;

    /** 해당 일자의 다음 발주번호를 생성한다. (PO-YYYYMMDD-####) */
    public String generate(LocalDate date) {
        String datePart = date.format(DATE_FORMAT);
        String dayPrefix = PREFIX + datePart + "-";
        String latestOrderNumber = purchaseOrderRepository.findMaxOrderNumber(dayPrefix);
        long sequence = (latestOrderNumber == null)
                ? 1
                : parseSequence(latestOrderNumber, dayPrefix) + 1;
        if (sequence > MAX_DAILY_SEQUENCE) {
            throw new BusinessException(ErrorCode.INTERNAL_ERROR,
                    "당일 발주번호가 9999건을 초과했습니다.");
        }
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
                "저장된 발주번호 형식이 올바르지 않습니다: " + orderNumber);
    }
}
