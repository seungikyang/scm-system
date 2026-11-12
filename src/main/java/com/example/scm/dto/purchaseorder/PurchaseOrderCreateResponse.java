package com.example.scm.dto.purchaseorder;

import com.example.scm.domain.PurchaseOrder;
import com.example.scm.domain.enums.PurchaseOrderStatus;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

/**
 * 발주서를 만든 뒤 알려 주는 번호, 상태, 총금액입니다.
 * 사용자는 이 결과의 번호로 방금 만든 발주서를 다시 조회할 수 있습니다.
 *
 * 발주서 작성 후 생성된 ID·발주번호·초기 상태·서버 계산 총액을 반환하는 DTO.
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
