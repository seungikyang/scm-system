package com.example.scm.dto.purchaseorder;

import com.example.scm.domain.Item;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

/**
 * 라인 품목 셀렉트 옵션 (작성 폼). unitPrice 는 단가 기본값 표시용. (02_contracts §3.1 items)
 */
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
