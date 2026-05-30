package com.example.scm.dto.purchaseorder;

import com.example.scm.domain.Partner;
import lombok.Builder;
import lombok.Getter;

/**
 * 공급사 셀렉트 옵션 (작성 폼 / 관리자 목록 거래처 필터). (02_contracts §3.1 partners, §3.4 partners)
 */
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
