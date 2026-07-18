package com.example.scm.dto.stock;

import lombok.Getter;

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
        this.shortageQuantity = Math.max(0, this.safetyStock - this.quantity);
        this.level = resolveLevel(this.quantity, this.safetyStock);
    }

    private StockLevel resolveLevel(int currentQuantity, int safetyQuantity) {
        if (currentQuantity == 0) {
            return StockLevel.OUT_OF_STOCK;
        }
        if (currentQuantity <= safetyQuantity) {
            return StockLevel.LOW;
        }
        return StockLevel.NORMAL;
    }
}
