package com.example.scm.dto.partner;

import com.example.scm.domain.Partner;
import com.example.scm.domain.enums.PartnerStatus;
import com.example.scm.domain.enums.PartnerType;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

/**
 * 거래처 상세 View DTO (Web 상세 + REST 상세 공용).
 */
@Getter
@Builder
public class PartnerDetailView {

    private final Long partnerId;
    private final String name;
    private final String businessNumber;
    private final PartnerType partnerType;
    private final String contactName;
    private final String phone;
    private final String email;
    private final String address;
    private final PartnerStatus status;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static PartnerDetailView from(Partner partner) {
        return PartnerDetailView.builder()
                .partnerId(partner.getId())
                .name(partner.getName())
                .businessNumber(partner.getBusinessNumber())
                .partnerType(partner.getPartnerType())
                .contactName(partner.getContactName())
                .phone(partner.getPhone())
                .email(partner.getEmail())
                .address(partner.getAddress())
                .status(partner.getStatus())
                .createdAt(partner.getCreatedAt())
                .updatedAt(partner.getUpdatedAt())
                .build();
    }
}
