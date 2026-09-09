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
 * 발주 헤더와 모든 라인을 한 번에 전달하는 상세 응답 DTO.
 * 엔티티에 없는 표시용 이름(거래처·작성자·결재자·품목)은 Service가 ID로 조회해 채운다.
 * 학습 모듈 32에서 헤더 변환 → LineResponse 중첩 목록 → Service의 일괄 이름 조회 순으로
 * 오가며 읽는다.
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

    /** 발주 상세에 포함되는 품목 한 줄. 품목 코드와 이름은 Service가 채운다. */
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
