package com.example.scm.dto.category;

import com.example.scm.domain.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 카테고리 등록/수정 폼 객체 (Web). th:object="${categoryForm}".
 */
@Getter
@Setter
@NoArgsConstructor
public class CategoryForm {

    @NotBlank(message = "카테고리명은 필수입니다.")
    @Size(max = 100, message = "카테고리명은 100자 이하여야 합니다.")
    private String name;

    @Size(max = 255, message = "설명은 255자 이하여야 합니다.")
    private String description;

    public static CategoryForm from(Category category) {
        CategoryForm form = new CategoryForm();
        form.name = category.getName();
        form.description = category.getDescription();
        return form;
    }
}
