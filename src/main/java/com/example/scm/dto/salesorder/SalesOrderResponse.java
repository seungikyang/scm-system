package com.example.scm.dto.salesorder;
import com.example.scm.domain.enums.SalesOrderStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
/**
 * 고객 주문의 회사, 작성자, 상태, 금액 등을 보여 주는 결과입니다.
 * 상세 조회에는 물건 목록도 담고, 요약 목록에서는 물건 목록을 빈 배열로 보냅니다.
 */
// record는 결과 자료 꾸러미입니다. 값을 읽는 함수와 생성자를 Java가 준비해 주며, 담긴 필드는 다시 대입할 수 없습니다.
public record SalesOrderResponse(Long salesOrderId, String orderNumber, Long partnerId, String partnerName,
        Long writerId, String writerName, Long managerId, LocalDate orderDate, LocalDate shipDate,
        BigDecimal totalAmount, SalesOrderStatus status, String cancelReason, LocalDateTime createdAt,
        LocalDateTime confirmedAt, LocalDateTime shippedAt, LocalDateTime completedAt, List<Line> lines) {
    public record Line(Long itemId, String itemCode, String itemName, Integer quantity,
                       BigDecimal unitPrice, BigDecimal lineAmount) {}
}
