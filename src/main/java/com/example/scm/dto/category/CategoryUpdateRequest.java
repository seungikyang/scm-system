package com.example.scm.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 기존 분류를 고칠 때 받는 이름과 설명입니다.
 * 등록 요청과 구분해 수정에 필요한 값만 받도록 만든 양식입니다.
 *
 * 카테고리 수정 REST API의 JSON 본문과 입력 검증 규칙을 담는 DTO.
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
