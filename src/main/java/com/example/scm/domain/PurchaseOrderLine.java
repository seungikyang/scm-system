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
 * 발주 라인. (02_architect_datamodel §2.2)
 * 헤더에 종속(aggregate child) — 단독 저장 금지, 항상 헤더 cascade 로 저장.
 * lineAmount 는 서버 계산(quantity × unitPrice, OQ-1).
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
    private BigDecimal lineAmount;                    // 서버 계산 (OQ-1)

    @Builder
    public PurchaseOrderLine(Long itemId, Integer quantity, BigDecimal unitPrice) {
        this.itemId = itemId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.lineAmount = calcLineAmount(quantity, unitPrice);
    }

    /** 헤더 연관 설정 (양방향 동기화 — PurchaseOrder.addLine 에서 호출). */
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
