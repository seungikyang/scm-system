package com.example.scm.dto.stock;

/**
 * 재고 화면과 대시보드에 표시할 세 가지 집계값.
 * 값만 운반하는 작은 불변 객체라 Java record를 사용한다.
 */
public record StockSummaryView(long activeItemCount, long totalQuantity, long lowStockCount) {
}
