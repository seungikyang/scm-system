package com.example.scm.dto.category;

import com.example.scm.domain.Category;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

/**
 * 카테고리 목록/상세 공용 View DTO (REST 응답 + Web 목록).
 * REST 응답 식별자 키는 categoryId.
 */
@Getter
@Builder
public class CategoryView {

    private final Long categoryId;
    private final String name;
    private final String description;
    private final long itemCount;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static CategoryView from(Category category, long itemCount) {
        return CategoryView.builder()
                .categoryId(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .itemCount(itemCount)
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }
}
