package com.example.scm.dto.item;

import com.example.scm.domain.Item;
import com.example.scm.domain.enums.ItemStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

/**
 * 품목 상세 View DTO. categoryName 은 Service 가 채운다(OSIV off).
 */
@Getter
@Builder
public class ItemDetailView {

    private final Long itemId;
    private final String itemCode;
    private final String name;
    private final Long categoryId;
    private final String categoryName;
    private final String unit;
    private final BigDecimal unitPrice;
    private final Integer safetyStock;
    private final ItemStatus status;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static ItemDetailView from(Item item, String categoryName) {
        return ItemDetailView.builder()
                .itemId(item.getId())
                .itemCode(item.getItemCode())
                .name(item.getName())
                .categoryId(item.getCategoryId())
                .categoryName(categoryName)
                .unit(item.getUnit())
                .unitPrice(item.getUnitPrice())
                .safetyStock(item.getSafetyStock())
                .status(item.getStatus())
                .createdAt(item.getCreatedAt())
                .updatedAt(item.getUpdatedAt())
                .build();
    }
}
