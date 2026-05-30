package com.example.scm.dto.item;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 품목 수정 REST 요청 (PRD 3.7.4). 품목코드는 변경 불가(unique 키).
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
    private BigDecimal unitPrice;

    @NotNull(message = "안전재고는 필수입니다.")
    @PositiveOrZero(message = "안전재고는 0 이상이어야 합니다.")
    private Integer safetyStock;
}
