package com.example.scm.dto.partner;

import com.example.scm.domain.Partner;
import com.example.scm.domain.enums.PartnerStatus;
import com.example.scm.domain.enums.PartnerType;
import lombok.Builder;
import lombok.Getter;

/**
 * 거래처 등록/수정 REST 응답 (PRD 3.7.2 shape).
 */
@Getter
@Builder
public class PartnerResponse {

    private final Long partnerId;
    private final String name;
    private final String businessNumber;
    private final PartnerType partnerType;
    private final PartnerStatus status;

    public static PartnerResponse from(Partner partner) {
        return PartnerResponse.builder()
                .partnerId(partner.getId())
                .name(partner.getName())
                .businessNumber(partner.getBusinessNumber())
                .partnerType(partner.getPartnerType())
                .status(partner.getStatus())
                .build();
    }

    public static PartnerResponse from(PartnerDetailView detail) {
        return PartnerResponse.builder()
                .partnerId(detail.getPartnerId())
                .name(detail.getName())
                .businessNumber(detail.getBusinessNumber())
                .partnerType(detail.getPartnerType())
                .status(detail.getStatus())
                .build();
    }
}
