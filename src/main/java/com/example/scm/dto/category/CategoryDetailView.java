package com.example.scm.dto.category;

import com.example.scm.domain.Category;
import com.example.scm.dto.item.ItemListView;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

/**
 * 카테고리 상세 View DTO (소속 품목 목록 포함). REST 상세 + Web 상세 공용.
 */
@Getter
@Builder
public class CategoryDetailView {

    private final Long categoryId;
    private final String name;
    private final String description;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final List<ItemListView> items;

    public static CategoryDetailView from(Category category, List<ItemListView> items) {
        return CategoryDetailView.builder()
                .categoryId(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .items(items)
                .build();
    }
}
