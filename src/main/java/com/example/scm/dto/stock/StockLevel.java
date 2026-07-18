package com.example.scm.dto.stock;

import lombok.Getter;

@Getter
public enum StockLevel {
    NORMAL("정상"),
    LOW("안전재고 이하"),
    OUT_OF_STOCK("재고 없음");

    private final String label;

    StockLevel(String label) {
        this.label = label;
    }
}
