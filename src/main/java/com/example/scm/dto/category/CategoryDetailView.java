package com.example.scm.dto.category;

import com.example.scm.domain.Category;
import com.example.scm.dto.item.ItemListView;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

/**
 * 카테고리 상세 View DTO (소속 품목 목록 포함). REST 상세 + Web 상세 공용.
 * 학습 모듈 32에서 단순 DTO 변환을 본 뒤, 품목 DTO 목록을 포함하는 중첩 응답으로 읽는다.
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
