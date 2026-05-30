package com.example.scm.service;

import com.example.scm.repository.PurchaseOrderRepository;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 발주번호 채번 (OQ-14, datamodel §7). 형식: PO-YYYYMMDD-#### (일자별 4자리 시퀀스, 0001부터).
 * 시퀀스 산출: countByOrderNumberStartingWith("PO-YYYYMMDD-") + 1.
 * 동시성 최종 방어선은 order_number UNIQUE 제약 + Service 의 재채번 재시도 루프.
 */
@Component
@RequiredArgsConstructor
public class OrderNumberGenerator {

    private static final String PREFIX = "PO-";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final PurchaseOrderRepository purchaseOrderRepository;

    /** 해당 일자의 다음 발주번호를 생성한다. (PO-YYYYMMDD-####) */
    public String generate(LocalDate date) {
        String datePart = date.format(DATE_FORMAT);
        String dayPrefix = PREFIX + datePart + "-";
        long sequence = purchaseOrderRepository.countByOrderNumberStartingWith(dayPrefix) + 1;
        return String.format("%s%04d", dayPrefix, sequence);
    }
}
