package com.example.scm.dto.purchaseorder;

import com.example.scm.domain.PurchaseOrder;
import com.example.scm.domain.enums.PurchaseOrderStatus;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

/**
 * 발주서 작성 결과. (02_contracts §1.1 Response 201)
 * { purchaseOrderId, orderNumber, status, totalAmount }
 */
@Getter
@Builder
public class PurchaseOrderCreateResponse {

    private final Long purchaseOrderId;
    private final String orderNumber;
    private final PurchaseOrderStatus status;
    private final BigDecimal totalAmount;

    public static PurchaseOrderCreateResponse from(PurchaseOrder po) {
        return PurchaseOrderCreateResponse.builder()
                .purchaseOrderId(po.getId())
                .orderNumber(po.getOrderNumber())
                .status(po.getStatus())
                .totalAmount(po.getTotalAmount())
                .build();
    }
}
