package com.example.scm.domain;

import com.example.scm.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 재고 (신규, OQ-4). 품목별 현재고를 itemId 기준 1행으로 관리.
 * 입고(APPROVED→RECEIVED) 트랜잭션 안에서 라인별 수량만큼 quantity 증가.
 * 동시 증가 정합성을 위해 @Version 낙관적 락. (02_architect_datamodel §2.3)
 */
@Entity
@Getter
@Table(name = "stocks",
        uniqueConstraints = @UniqueConstraint(name = "uk_stock_item", columnNames = "item_id"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Stock extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "item_id", nullable = false, unique = true)
    private Long itemId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity = 0;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    public Stock(Long itemId, Integer quantity) {
        this.itemId = itemId;
        this.quantity = (quantity != null) ? quantity : 0;
    }

    /** 입고 시 재고 증가 (OQ-4). */
    public void increase(int amount) {
        this.quantity += amount;
    }
}
