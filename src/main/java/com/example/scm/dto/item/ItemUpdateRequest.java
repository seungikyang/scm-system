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
 * 품목 수정 REST API의 JSON 본문을 받는 DTO.
 * 등록 후 식별 기준으로 쓰이는 품목코드는 변경할 수 없으므로 이 DTO에 포함하지 않는다.
 */
@Getter
@Setter
@NoArgsConstructor
public class ItemUpdateRequest {

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
