package com.example.scm.dto.partner;

import com.example.scm.domain.Partner;
import com.example.scm.domain.enums.PartnerStatus;
import com.example.scm.domain.enums.PartnerType;
import lombok.Builder;
import lombok.Getter;

/**
 * 거래처 등록·수정 후 API에 보내는 결과 양식입니다.
 * 새로 저장한 회사 번호와 이름, 유형, 상태를 알려 줍니다.
 *
 * 거래처 등록·수정 후 API에 반환할 필드만 모은 응답 DTO.
 * 학습 모듈 32에서 두 from 메서드가 서로 다른 원본을 같은 응답 모양으로 바꾸는 방식을 본다.
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
