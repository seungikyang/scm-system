package com.example.scm.dto.item;

import com.example.scm.domain.Item;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 품목 등록/수정 폼 객체 (Web). th:object="${itemForm}".
 */
@Getter
@Setter
@NoArgsConstructor
public class ItemForm {

    @NotBlank(message = "품목코드는 필수입니다.")
    @Size(max = 50, message = "품목코드는 50자 이하여야 합니다.")
    private String itemCode;

    @NotBlank(message = "품목명은 필수입니다.")
    @Size(max = 150, message = "품목명은 150자 이하여야 합니다.")
    private String name;

    @NotNull(message = "카테고리는 필수입니다.")
    private Long categoryId;

    @NotBlank(message = "단위는 필수입니다.")
    @Size(max = 20, message = "단위는 20자 이하여야 합니다.")
    private String unit;

    @NotNull(message = "단가는 필수입니다.")
    @PositiveOrZero(message = "단가는 0 이상이어야 합니다.")
    private BigDecimal unitPrice;

    @NotNull(message = "안전재고는 필수입니다.")
    @PositiveOrZero(message = "안전재고는 0 이상이어야 합니다.")
    private Integer safetyStock;

    public static ItemForm from(Item item) {
        ItemForm form = new ItemForm();
        form.itemCode = item.getItemCode();
        form.name = item.getName();
        form.categoryId = item.getCategoryId();
        form.unit = item.getUnit();
        form.unitPrice = item.getUnitPrice();
        form.safetyStock = item.getSafetyStock();
        return form;
    }
}
