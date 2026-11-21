package com.example.scm.dto.partner;

import com.example.scm.domain.Partner;
import com.example.scm.domain.enums.PartnerStatus;
import com.example.scm.domain.enums.PartnerType;
import lombok.Builder;
import lombok.Getter;

/**
 * 거래처 목록의 한 행에 표시할 회사명, 담당자, 연락처, 상태입니다.
 * 목록에서 필요한 값을 골라 담아 각 회사의 정보를 짧게 보여 줍니다.
 *
 * 거래처 목록 행 View DTO (Web 목록 + REST 목록 공용).
 */
@Getter
@Builder
public class PartnerListView {

    private final Long partnerId;
    private final String name;
    private final String businessNumber;
    private final PartnerType partnerType;
    private final String contactName;
    private final String phone;
    private final PartnerStatus status;

    public static PartnerListView from(Partner partner) {
        return PartnerListView.builder()
                .partnerId(partner.getId())
                .name(partner.getName())
                .businessNumber(partner.getBusinessNumber())
                .partnerType(partner.getPartnerType())
                .contactName(partner.getContactName())
                .phone(partner.getPhone())
                .status(partner.getStatus())
                .build();
    }
}
