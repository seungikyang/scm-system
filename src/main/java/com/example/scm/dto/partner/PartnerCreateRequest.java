package com.example.scm.dto.partner;

import com.example.scm.domain.enums.PartnerType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 거래처 등록 REST 요청 (PRD 3.7.2).
 */
@Getter
@Setter
@NoArgsConstructor
public class PartnerCreateRequest {

    @NotBlank(message = "거래처명은 필수입니다.")
    @Size(max = 150)
    private String name;

    @NotBlank(message = "사업자번호는 필수입니다.")
    @Size(max = 20)
    private String businessNumber;

    @NotNull(message = "거래처 유형은 필수입니다.")
    private PartnerType partnerType;

    @Size(max = 50)
    private String contactName;

    @Size(max = 30)
    private String phone;

    @Email
    @Size(max = 100)
    private String email;

    @Size(max = 255)
    private String address;
}
