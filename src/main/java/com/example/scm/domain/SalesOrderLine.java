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
 * 수주서의 물건 한 종류, 수량, 가격을 담는 한 줄입니다.
 * 예를 들어 공책 3개 × 2,000원이면 이 줄의 금액은 6,000원입니다.
 *
 * 수주 헤더에 속하는 라인. 금액은 서버에서 수량 × 단가로 계산한다.
 */
@Entity
@Getter
@Table(name = "sales_order_lines",
        indexes = {
                @Index(name = "idx_sol_so", columnList = "sales_order_id"),
                @Index(name = "idx_sol_item", columnList = "item_id")
        })
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SalesOrderLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    // 여러 물건 줄이 같은 수주서에 속할 수 있습니다. LAZY는 수주서 자료가 필요할 때 읽는 방식입니다.
    @JoinColumn(name = "sales_order_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_sol_so"))
    private SalesOrder salesOrder;

    // 품목도 번호만 저장합니다. 품목 이름이 나중에 바뀌어도 주문 줄 자체는 그대로 두고, 화면에 보여 줄 때 이름을 찾습니다.
    @Column(name = "item_id", nullable = false)
    private Long itemId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "unit_price", nullable = false, precision = 15, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "line_amount", nullable = false, precision = 15, scale = 2)
    // quantity × unitPrice 서버 계산값입니다. 브라우저가 보낸 금액을 믿으면 화면을 조작해 금액을 바꿀 수 있으므로,
    // 금액은 받지 않고 항상 서버가 직접 계산해 저장합니다.
    private BigDecimal lineAmount;

    @Builder
    public SalesOrderLine(Long itemId, Integer quantity, BigDecimal unitPrice) {
        this.itemId = itemId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.lineAmount = calcLineAmount(quantity, unitPrice);
    }

    /**
     * 헤더와 라인을 양쪽에서 일치시키기 위해 SalesOrder.addLine에서만 호출한다.
     * public이 아닌 이유: 다른 곳에서 이 함수만 부르면 줄은 주문에 연결됐는데 주문 총액에는 더해지지 않는
     * 어긋난 상태가 생깁니다. 그래서 같은 패키지의 SalesOrder.addLine만 부를 수 있게 막아 두었습니다.
     */
    void setSalesOrder(SalesOrder salesOrder) {
        this.salesOrder = salesOrder;
    }

    private static BigDecimal calcLineAmount(Integer quantity, BigDecimal unitPrice) {
        // 이 계산의 0 반환은 빈 값에 대한 처리입니다. 실제 주문의 필수값 검사는 DTO와 Service가 합니다.
        if (quantity == null || unitPrice == null) {
            return BigDecimal.ZERO;
        }
        // multiply는 곱하기입니다. quantity를 금액 계산에 맞는 BigDecimal로 바꿔 단가와 곱합니다.
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
