// 실제 구현 위치 예: src/main/java/com/example/scm/dto/*Response.java
// 목표: Entity → DTO 정적 팩토리와 헤더-라인 nested 매핑을 채우세요. TRD 3.16.3 참고.

// ===== Item =====
public record ItemResponse(
        Long itemId,
        String itemCode,
        String name,
        Long categoryId,
        String categoryName,
        String unit,
        BigDecimal unitPrice,
        Integer safetyStock,
        String status
) {
    // TODO 01: 정적 팩토리.
    public static ItemResponse ____(Item item) {
        return new ItemResponse(
            item.getId(),
            item.getItemCode(),
            item.getName(),
            item.getCategory().getId(),
            item.getCategory().getName(),
            item.getUnit(),
            item.getUnitPrice(),
            item.getSafetyStock(),
            item.getStatus().name()
        );
    }
}

// ===== Partner =====
public record PartnerResponse(
        Long partnerId,
        String name,
        String businessNumber,
        String partnerType,
        String status
) {
    public static PartnerResponse from(Partner p) {
        return new PartnerResponse(
            p.getId(), p.getName(), p.getBusinessNumber(),
            p.getPartnerType().name(), p.getStatus().name()
        );
    }
}

// ===== Purchase Order (헤더 + 라인) =====
public record PurchaseOrderResponse(
        Long purchaseOrderId,
        String orderNumber,
        Long partnerId,
        String partnerName,
        Long writerId,
        Long approverId,
        LocalDate orderDate,
        LocalDate dueDate,
        BigDecimal totalAmount,
        String status,
        String rejectReason,
        LocalDateTime createdAt,
        LocalDateTime approvedAt,
        LocalDateTime receivedAt,
        // TODO 02: 라인 nested 목록.
        List<____> lines
) {
    public static PurchaseOrderResponse from(PurchaseOrder po) {
        return new PurchaseOrderResponse(
            po.getId(),
            po.getOrderNumber(),
            po.getPartner().getId(),
            po.getPartner().getName(),
            po.getWriterId(),
            po.getApproverId(),
            po.getOrderDate(),
            po.getDueDate(),
            po.getTotalAmount(),
            po.getStatus().name(),
            po.getRejectReason(),
            po.getCreatedAt(),
            po.getApprovedAt(),
            po.getReceivedAt(),
            // TODO 03: 라인을 DTO 리스트로 변환.
            po.getLines().stream().map(PurchaseOrderLineResponse::from).____()
        );
    }
}

public record PurchaseOrderLineResponse(
        Long lineId,
        Long itemId,
        String itemCode,
        String itemName,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal lineAmount
) {
    public static PurchaseOrderLineResponse from(PurchaseOrderLine l) {
        return new PurchaseOrderLineResponse(
            l.getId(),
            l.getItem().getId(),
            l.getItem().getItemCode(),
            l.getItem().getName(),
            l.getQuantity(),
            l.getUnitPrice(),
            l.getLineAmount()
        );
    }
}

// ===== Sales Order (헤더 + 라인) =====
// (PurchaseOrderResponse 와 동일한 패턴으로 작성)

// ===== Page 변환 패턴 =====
class PagingPattern {
    public Page<ItemResponse> convert(Page<Item> page) {
        // TODO 04: Page 의 메타 정보(totalElements 등) 를 유지하며 변환하려면?
        return page.____(ItemResponse::from);
    }
}

// 학습 질문:
// Q1. record DTO 와 class + Lombok DTO 의 트레이드오프는?
//     A:
// Q2. Entity → DTO 변환을 Service 가 아닌 DTO 의 정적 팩토리에 둔 이유는?
//     A:
// Q3. MapStruct / ModelMapper 를 도입할 시점은 언제일까?
//     A:
