package com.example.scm.service;

import com.example.scm.common.exception.BusinessException;
import com.example.scm.common.exception.ErrorCode;
import com.example.scm.repository.PurchaseOrderRepository;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 사람이 읽기 쉬운 발주번호를 만드는 컴포넌트(채번기).
 *
 * <p>학습 모듈 10: PurchaseOrderService의 작성 흐름에서 generate 호출을 먼저 찾은 뒤
 * 이 파일로 들어온다. 동시 요청 검증은 모듈 21의 통합 테스트에서 다시 확인한다.</p>
 *
 * <p>형식은 {@code PO-YYYYMMDD-####}이고 날짜마다 0001부터 시작한다. 당일 저장된 가장 큰
 * 번호에 1을 더하므로 중간 번호가 비어도 재사용하지 않는다. 동시 요청이 같은 다음 번호를
 * 계산할 수 있으므로 최종 중복 방지는 DB UNIQUE 제약과 Service의 재시도가 담당한다.</p>
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
