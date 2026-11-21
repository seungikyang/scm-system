package com.example.scm.dto.purchaseorder;

import com.example.scm.domain.Partner;
import lombok.Builder;
import lombok.Getter;

/**
 * 주문 화면에서 회사를 고를 때 필요한 번호와 이름입니다.
 * 사람에게는 회사 이름을 보여 주고 서버에는 그 회사의 번호를 보냅니다.
 *
 * 발주 작성 폼과 관리자 필터의 공급사 선택 상자에 필요한 최소 필드 DTO.
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
