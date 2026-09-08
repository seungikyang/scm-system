package com.example.scm.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 발주에 포함된 품목 한 줄을 나타내는 자식 엔티티.
 *
 * <p>학습 모듈 04: PurchaseOrderStatus 다음, PurchaseOrder 헤더 전에 읽는다.</p>
 *
 * <p>예를 들어 품목 A 3개와 품목 B 2개를 주문하면 라인이 두 개 생긴다. 라인은 단독으로
 * 저장하지 않고 항상 {@link PurchaseOrder}에 추가해 함께 저장한다. 금액 조작을 막기 위해
 * {@code lineAmount}는 클라이언트에서 받지 않고 서버가 수량 × 단가로 계산한다.</p>
 */
@Entity
@Getter
@Table(name = "purchase_order_lines",
        indexes = {
                @Index(name = "idx_pol_po", columnList = "purchase_order_id"),
                @Index(name = "idx_pol_item", columnList = "item_id")
        })
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PurchaseOrderLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "purchase_order_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_pol_po"))
    private PurchaseOrder purchaseOrder;

    @Column(name = "item_id", nullable = false)
    private Long itemId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "unit_price", nullable = false, precision = 15, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "line_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal lineAmount;                    // quantity × unitPrice 서버 계산값

    @Builder
    public PurchaseOrderLine(Long itemId, Integer quantity, BigDecimal unitPrice) {
        this.itemId = itemId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.lineAmount = calcLineAmount(quantity, unitPrice);
    }

    /** 헤더와 라인을 양쪽에서 일치시키기 위해 PurchaseOrder.addLine에서만 호출한다. */
    void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    private static BigDecimal calcLineAmount(Integer quantity, BigDecimal unitPrice) {
        if (quantity == null || unitPrice == null) {
            return BigDecimal.ZERO;
        }
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
