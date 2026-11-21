package com.example.scm.dto.stock;

import lombok.Getter;

/**
 * 창고 수량을 보고 재고 없음, 부족, 정상으로 붙이는 이름표입니다.
 * 계산은 StockListView에서 하고, 이 목록은 결과의 이름과 화면에 보여 줄 문구를 정합니다.
 *
 * 재고 목록에서 계산해 표시하는 수준. DB에 저장하는 상태가 아니라 현재 수량에서 파생된다.
 */
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
