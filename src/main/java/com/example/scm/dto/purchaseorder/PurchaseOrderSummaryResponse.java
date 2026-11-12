package com.example.scm.dto.purchaseorder;

import com.example.scm.domain.PurchaseOrder;
import com.example.scm.domain.enums.PurchaseOrderStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

/**
 * 발주 목록에서 한 건을 간단히 보여 주는 요약입니다.
 * 전체 물건 목록은 상세 화면에서 보고, 목록에는 번호·회사·금액·상태 등을 표시합니다.
 *
 * 내 발주 목록과 관리자 목록에서 공통으로 사용하는 한 행의 DTO.
 * 엔티티에는 거래처 ID만 있으므로 표시용 {@code partnerName}은 Service가 조회해 채운다.
 * 학습 모듈 32에서 목록용 최소 필드를 먼저 확인한 뒤 PurchaseOrderDetailResponse로 이동한다.
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
