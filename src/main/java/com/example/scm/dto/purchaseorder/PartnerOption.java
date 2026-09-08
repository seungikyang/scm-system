package com.example.scm.dto.purchaseorder;

import com.example.scm.domain.Partner;
import lombok.Builder;
import lombok.Getter;

/** 발주 작성 폼과 관리자 필터의 공급사 선택 상자에 필요한 최소 필드 DTO. */
@Getter
@Builder
public class PartnerOption {

    private final Long id;
    private final String name;

    public static PartnerOption from(Partner partner) {
        return PartnerOption.builder()
                .id(partner.getId())
                .name(partner.getName())
                .build();
    }
}
