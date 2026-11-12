package com.example.scm.dto.purchaseorder;

import com.example.scm.domain.PurchaseOrder;
import com.example.scm.domain.enums.PurchaseOrderStatus;
import lombok.Builder;
import lombok.Getter;

/**
 * 발주 처리 후 바뀐 상태와 안내 문구를 보내는 결과입니다.
 * 예를 들어 승인 작업이 끝나면 APPROVED 상태와 승인 안내를 알려 줍니다.
 *
 * 결재 요청·취소·승인·반려·입고 처리 후 바뀐 상태와 안내 메시지를 반환하는 DTO.
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
