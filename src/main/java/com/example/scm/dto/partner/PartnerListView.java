package com.example.scm.dto.partner;

import com.example.scm.domain.Partner;
import com.example.scm.domain.enums.PartnerStatus;
import com.example.scm.domain.enums.PartnerType;
import lombok.Builder;
import lombok.Getter;

/**
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
