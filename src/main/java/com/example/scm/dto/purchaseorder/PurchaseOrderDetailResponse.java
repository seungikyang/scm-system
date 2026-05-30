package com.example.scm.dto.purchaseorder;

import com.example.scm.domain.PurchaseOrder;
import com.example.scm.domain.enums.PurchaseOrderStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

/**
 * 발주서 상세 (헤더 + 라인). 표시값(partnerName/writerName/approverName/itemCode/itemName)은
 * Service 가 마스터 조회해 채운다(OSIV off). (02_contracts §1.4, §2.2)
 */
@Getter
@Builder
public class PurchaseOrderDetailResponse {

    private final Long purchaseOrderId;
    private final String orderNumber;
    private final Long partnerId;
    private final String partnerName;
    private final Long writerId;
    private final String writerName;
    private final Long approverId;
    private final String approverName;
    private final LocalDate orderDate;
    private final LocalDate dueDate;
    private final BigDecimal totalAmount;
    private final PurchaseOrderStatus status;
    private final String rejectReason;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final LocalDateTime approvedAt;
    private final LocalDateTime receivedAt;
    private final List<LineResponse> lines;

    public static PurchaseOrderDetailResponse of(PurchaseOrder po,
                                                 String partnerName,
                                                 String writerName,
                                                 String approverName,
                                                 List<LineResponse> lines) {
        return PurchaseOrderDetailResponse.builder()
                .purchaseOrderId(po.getId())
                .orderNumber(po.getOrderNumber())
                .partnerId(po.getPartnerId())
                .partnerName(partnerName)
                .writerId(po.getWriterId())
                .writerName(writerName)
                .approverId(po.getApproverId())
                .approverName(approverName)
                .orderDate(po.getOrderDate())
                .dueDate(po.getDueDate())
                .totalAmount(po.getTotalAmount())
                .status(po.getStatus())
                .rejectReason(po.getRejectReason())
                .createdAt(po.getCreatedAt())
                .updatedAt(po.getUpdatedAt())
                .approvedAt(po.getApprovedAt())
                .receivedAt(po.getReceivedAt())
                .lines(lines)
                .build();
    }

    /**
     * 발주 상세 라인. itemCode/itemName 은 Service 가 채운다. (02_contracts §1.4 lines[], §2.2)
     */
    @Getter
    @Builder
    public static class LineResponse {

        private final Long lineId;
        private final Long itemId;
        private final String itemCode;
        private final String itemName;
        private final Integer quantity;
        private final BigDecimal unitPrice;
        private final BigDecimal lineAmount;
    }
}
