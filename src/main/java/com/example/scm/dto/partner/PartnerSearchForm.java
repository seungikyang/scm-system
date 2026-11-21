package com.example.scm.dto.partner;

import com.example.scm.domain.enums.PartnerStatus;
import com.example.scm.domain.enums.PartnerType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 거래처를 찾을 때 쓰는 회사명, 사업자번호, 유형, 상태 조건입니다.
 * 비어 있는 조건은 쓰지 않고 선택한 조건을 함께 적용합니다.
 *
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
