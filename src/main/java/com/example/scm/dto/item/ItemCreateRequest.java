package com.example.scm.dto.item;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 품목 등록 REST API의 JSON 본문을 받는 DTO.
 * 애너테이션은 필수 여부·문자 길이·숫자 범위를 컨트롤러 진입 시점에 검증한다.
 * 학습 모듈 17에서 NotBlank → NotNull → 숫자 범위 순으로 제약을 읽고, 모듈 18에서
 * Controller의 {@code @Valid}와 연결한다.
 */
@Getter
@Setter
@NoArgsConstructor
public class ItemCreateRequest {

    @NotBlank(message = "품목코드는 필수입니다.")
    @Size(max = 50)
    private String itemCode;

    @NotBlank(message = "품목명은 필수입니다.")
    @Size(max = 150)
    private String name;

    @NotNull(message = "카테고리는 필수입니다.")
    private Long categoryId;

    @NotBlank(message = "단위는 필수입니다.")
    @Size(max = 20)
    private String unit;

    @NotNull(message = "단가는 필수입니다.")
    @PositiveOrZero(message = "단가는 0 이상이어야 합니다.")
    @Digits(integer = 13, fraction = 2, message = "단가는 정수 13자리, 소수 2자리 이하여야 합니다.")
    private BigDecimal unitPrice;

    @NotNull(message = "안전재고는 필수입니다.")
    @PositiveOrZero(message = "안전재고는 0 이상이어야 합니다.")
    private Integer safetyStock;
}
