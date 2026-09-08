package com.example.scm.dto.stock;

import lombok.Getter;

/** 재고 목록에서 계산해 표시하는 수준. DB에 저장하는 상태가 아니라 현재 수량에서 파생된다. */
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
