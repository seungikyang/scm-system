package com.example.scm.dto.purchaseorder;

import com.example.scm.domain.Item;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

/** 발주 라인의 품목 선택 상자에 쓰는 DTO. 표준단가는 입력 안내와 기본값 결정에 사용한다. */
@Getter
@Builder
public class ItemOption {

    private final Long id;
    private final String itemCode;
    private final String name;
    private final BigDecimal unitPrice;

    public static ItemOption from(Item item) {
        return ItemOption.builder()
                .id(item.getId())
                .itemCode(item.getItemCode())
                .name(item.getName())
                .unitPrice(item.getUnitPrice())
                .build();
    }
}
