package com.example.scm.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 카테고리 수정 REST 요청 (PRD 3.7.3).
 */
@Getter
@Setter
@NoArgsConstructor
public class CategoryUpdateRequest {

    @NotBlank(message = "카테고리명은 필수입니다.")
    @Size(max = 100)
    private String name;

    @Size(max = 255)
    private String description;
}
