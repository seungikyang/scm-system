package com.example.scm.dto.stock;

/**
 * 운영 중인 물건 종류 수, 전체 재고 수량, 안전재고 이하 품목 수입니다.
 * 품목 수는 물건의 종류를 세는 것이고, 전체 수량은 각 물건의 개수를 모두 더한 값입니다.
 *
 * 재고 화면과 대시보드에 표시할 세 가지 집계값.
 * 값만 운반하는 작은 불변 객체라 Java record를 사용한다.
 */
public record StockSummaryView(long activeItemCount, long totalQuantity, long lowStockCount) {
}
