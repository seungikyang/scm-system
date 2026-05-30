package com.example.scm.dto.purchaseorder;

import com.example.scm.domain.PurchaseOrder;
import com.example.scm.domain.enums.PurchaseOrderStatus;
import lombok.Builder;
import lombok.Getter;

/**
 * 상태 변경 결과 (submit/cancel/approve/reject/receive). (02_contracts §1.2/1.5/1.7/1.8/1.9)
 * { purchaseOrderId, orderNumber, status, message }
 */
@Getter
@Builder
public class PurchaseOrderStatusResponse {

    private final Long purchaseOrderId;
    private final String orderNumber;
    private final PurchaseOrderStatus status;
    private final String message;

    public static PurchaseOrderStatusResponse of(PurchaseOrder po, String message) {
        return PurchaseOrderStatusResponse.builder()
                .purchaseOrderId(po.getId())
                .orderNumber(po.getOrderNumber())
                .status(po.getStatus())
                .message(message)
                .build();
    }
}
