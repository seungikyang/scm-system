package com.example.scm.dto.purchaseorder;

import com.example.scm.domain.Item;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

/**
 * 주문 화면의 물건 선택 목록에 넣을 코드, 이름, 표준단가입니다.
 * 표준단가는 따로 가격을 입력하지 않았을 때 사용할 기본 가격입니다.
 *
 * 발주 라인의 품목 선택 상자에 쓰는 DTO. 표준단가는 입력 안내와 기본값 결정에 사용한다.
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
