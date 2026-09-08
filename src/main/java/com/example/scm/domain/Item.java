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

/**
 * 품목 엔티티.
 *
 * <p>학습 모듈 03: 모듈 02의 Category를 이해한 뒤 ItemStatus → 이 클래스 순서로 읽는다.
 * 등록·검색 Service는 모듈 08~09, 수정·단종 흐름은 모듈 28에서 이어진다.</p>
 *
 * 초보자 포인트:
 * - unitPrice 는 반드시 BigDecimal. double 은 부동소수점 오차(0.1 + 0.2 != 0.3) 때문에
 *   금액 계산에 쓰면 안 된다. precision/scale 로 DB 소수 자릿수도 고정한다.
 * - 삭제(DELETE) 대신 DISCONTINUED 단종 상태로 전환한다. 과거 발주 라인이 이 품목을
 *   계속 참조하므로 데이터는 남기고 "새 발주에 못 쓰게"만 막는다.
 * - safetyStock(안전재고): 현재고가 이 값 이하면 재고 화면에서 부족으로 경고한다.
 */
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
