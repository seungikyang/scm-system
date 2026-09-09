package com.example.scm.dto.stock;

import lombok.Getter;

/**
 * 재고 목록의 한 행을 나타내는 읽기 전용 DTO.
 *
 * <p>학습 모듈 11: StockRepository의 생성자 표현식 다음에 읽어 현재고에서 부족수량과
 * 표시 상태가 어떻게 파생되는지 확인한다.</p>
 *
 * <p>StockRepository의 JPQL 생성자 표현식이 이 생성자를 직접 호출한다. 아직 Stock 행이
 * 없는 품목의 null 수량은 0으로 바꾸고, 부족 수량과 표시 상태는 서버에서 계산해 API와
 * 웹 화면이 같은 기준을 사용하게 한다.</p>
 */
@Getter
public class StockListView {

    private final Long itemId;
    private final String itemCode;
    private final String itemName;
    private final String unit;
    private final Integer quantity;
    private final Integer safetyStock;
    private final Integer shortageQuantity;
    private final StockLevel level;

    public StockListView(Long itemId, String itemCode, String itemName, String unit,
                         Integer quantity, Integer safetyStock) {
        this.itemId = itemId;
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.unit = unit;
        this.quantity = quantity != null ? quantity : 0;
        this.safetyStock = safetyStock != null ? safetyStock : 0;
        // 현재고가 충분할 때 부족 수량이 음수가 되지 않도록 최솟값을 0으로 둔다.
        this.shortageQuantity = Math.max(0, this.safetyStock - this.quantity);
        this.level = resolveLevel(this.quantity, this.safetyStock);
    }

    private StockLevel resolveLevel(int currentQuantity, int safetyQuantity) {
        // 0은 안전재고 값과 관계없이 가장 구체적인 OUT_OF_STOCK으로 먼저 분류한다.
        if (currentQuantity == 0) {
            return StockLevel.OUT_OF_STOCK;
        }
        if (currentQuantity <= safetyQuantity) {
            return StockLevel.LOW;
        }
        return StockLevel.NORMAL;
    }
}
