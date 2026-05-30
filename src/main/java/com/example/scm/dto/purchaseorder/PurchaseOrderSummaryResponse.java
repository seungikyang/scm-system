package com.example.scm.dto.purchaseorder;

import com.example.scm.domain.PurchaseOrder;
import com.example.scm.domain.enums.PurchaseOrderStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

/**
 * 발주 목록 항목 (내 목록 / 관리자 목록 공용). partnerName 은 Service 가 채운다(OSIV off). (02_contracts §1.3/1.6)
 */
@Getter
@Builder
public class PurchaseOrderSummaryResponse {

    private final Long purchaseOrderId;
    private final String orderNumber;
    private final Long partnerId;
    private final String partnerName;
    private final LocalDate orderDate;
    private final LocalDate dueDate;
    private final BigDecimal totalAmount;
    private final PurchaseOrderStatus status;
    private final LocalDateTime createdAt;

    public static PurchaseOrderSummaryResponse from(PurchaseOrder po, String partnerName) {
        return PurchaseOrderSummaryResponse.builder()
                .purchaseOrderId(po.getId())
                .orderNumber(po.getOrderNumber())
                .partnerId(po.getPartnerId())
                .partnerName(partnerName)
                .orderDate(po.getOrderDate())
                .dueDate(po.getDueDate())
                .totalAmount(po.getTotalAmount())
                .status(po.getStatus())
                .createdAt(po.getCreatedAt())
                .build();
    }
}
