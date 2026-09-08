package com.example.scm.domain;

import com.example.scm.common.entity.BaseTimeEntity;
import com.example.scm.common.exception.BusinessException;
import com.example.scm.common.exception.ErrorCode;
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
 * 품목별 현재 수량을 보관하는 재고 엔티티.
 *
 * <p>학습 모듈 11: PurchaseOrderService.receive의 상태 변경을 먼저 읽고, 재고 한 행이
 * 어떻게 증가하는지 확인할 때 이 클래스로 이동한다.</p>
 *
 * <p>같은 {@code itemId}에는 재고 행이 하나만 존재한다. 발주가 승인에서 입고 완료로
 * 바뀌는 트랜잭션 안에서 각 라인의 수량만큼 증가한다. {@code @Version}은 두 요청이 같은
 * 재고를 동시에 수정했을 때 한쪽 변경을 조용히 덮어쓰지 않고 충돌로 감지하게 한다.</p>
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

    /** 입고 수량을 더한다. 0 이하와 int 범위를 넘는 합계는 업무 오류로 거부한다. */
    public void increase(int amount) {
        if (amount <= 0) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "입고 수량은 0보다 커야 합니다.");
        }
        try {
            this.quantity = Math.addExact(this.quantity, amount);
        } catch (ArithmeticException e) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "재고 수량이 허용 범위를 초과합니다.");
        }
    }
}
