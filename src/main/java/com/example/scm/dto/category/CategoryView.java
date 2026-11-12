package com.example.scm.dto.category;

import com.example.scm.domain.Category;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

/**
 * 분류 이름과 소속 물건 수 등을 목록에 보여 주는 양식입니다.
 * from 함수는 저장용 자료에서 보여 줄 값만 골라 이 양식에 옮깁니다.
 *
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
