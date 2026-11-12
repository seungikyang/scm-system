package com.example.scm.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 새 분류를 만들 때 받는 이름과 설명입니다.
 * 입력칸 위의 @ 표시들은 필수 여부와 글자 수 같은 입력 규칙을 정합니다.
 *
 * 카테고리 등록 REST API의 JSON 본문과 입력 검증 규칙을 담는 DTO.
 */
@Getter
@Setter
@NoArgsConstructor
public class CategoryCreateRequest {

    @NotBlank(message = "카테고리명은 필수입니다.")
    @Size(max = 100)
    private String name;

    @Size(max = 255)
    private String description;
}
