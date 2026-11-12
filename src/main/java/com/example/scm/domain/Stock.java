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
 * 창고에 남아 있는 물건 수량을 기록하는 장부입니다.
 * 입고는 더하기, 출고는 빼기입니다. 수량이 모자라면 출고를 막아 음수 재고가 생기지 않게 합니다.
 *
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
    // 내가 읽은 수정 번호와 저장된 번호가 다르면 다른 작업이 먼저 고친 것이므로 충돌을 알립니다.
    @Column(name = "version", nullable = false)
    private Long version;

    public Stock(Long itemId, Integer quantity) {
        this.itemId = itemId;
        this.quantity = (quantity != null) ? quantity : 0;
    }

    /** 출고 시 음수 재고를 허용하지 않는다. */
    public void decrease(int amount) {
        if (amount <= 0) throw new BusinessException(ErrorCode.INVALID_INPUT);
        // 장부에 3개밖에 없는데 5개를 보내려 하면 실제로 보낼 수 없으므로 중단합니다.
        if (quantity < amount) throw new BusinessException(ErrorCode.INSUFFICIENT_STOCK);
        // -=는 '현재 값에서 이만큼 빼고 다시 보관'입니다. 예: 10개에서 3개를 보내면 7개가 남습니다.
        quantity -= amount;
    }

    /** 입고 수량을 더한다. 0 이하와 int 범위를 넘는 합계는 업무 오류로 거부한다. */
    public void increase(int amount) {
        if (amount <= 0) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "입고 수량은 0보다 커야 합니다.");
        }
        try {
            // int가 담을 수 있는 최대 수를 넘으면 덧셈 결과가 잘못 뒤집히므로 안전한 덧셈으로 검사합니다.
            this.quantity = Math.addExact(this.quantity, amount);
        } catch (ArithmeticException e) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "재고 수량이 허용 범위를 초과합니다.");
        }
    }
}
