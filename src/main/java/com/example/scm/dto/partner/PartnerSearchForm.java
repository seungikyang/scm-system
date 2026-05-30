package com.example.scm.dto.partner;

import com.example.scm.domain.enums.PartnerStatus;
import com.example.scm.domain.enums.PartnerType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 거래처 목록 검색 조건 (Web + REST query). 모든 조건은 선택(nullable).
 */
@Getter
@Setter
@NoArgsConstructor
public class PartnerSearchForm {

    private String name;
    private String businessNumber;
    private PartnerType partnerType;
    private PartnerStatus status;
}
