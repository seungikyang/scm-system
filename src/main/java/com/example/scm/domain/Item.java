package com.example.scm.domain;

import com.example.scm.common.entity.BaseTimeEntity;
import com.example.scm.domain.enums.ItemStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "items")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "item_code", nullable = false, unique = true, length = 50)
    private String itemCode;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Column(nullable = false, length = 20)
    private String unit;

    @Column(name = "unit_price", nullable = false, precision = 15, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "safety_stock", nullable = false)
    private Integer safetyStock;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ItemStatus status;

    @Builder
    public Item(String itemCode, String name, Long categoryId, String unit,
                BigDecimal unitPrice, Integer safetyStock, ItemStatus status) {
        this.itemCode = itemCode;
        this.name = name;
        this.categoryId = categoryId;
        this.unit = unit;
        this.unitPrice = unitPrice;
        this.safetyStock = (safetyStock != null) ? safetyStock : 0;
        this.status = (status != null) ? status : ItemStatus.ACTIVE;
    }

    public void update(String name, Long categoryId, String unit,
                       BigDecimal unitPrice, Integer safetyStock) {
        this.name = name;
        this.categoryId = categoryId;
        this.unit = unit;
        this.unitPrice = unitPrice;
        this.safetyStock = (safetyStock != null) ? safetyStock : 0;
    }

    public void discontinue() {
        this.status = ItemStatus.DISCONTINUED;
    }

    public boolean isActive() {
        return this.status == ItemStatus.ACTIVE;
    }
}
