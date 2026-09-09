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
 * 거래처 수정 REST API의 JSON 본문을 받는 DTO.
 * 등록 후 고유 식별값으로 쓰는 사업자번호는 변경할 수 없어 필드에 포함하지 않는다.
 */
@Getter
@Setter
@NoArgsConstructor
public class PartnerUpdateRequest {

    @NotBlank(message = "거래처명은 필수입니다.")
    @Size(max = 150)
    private String name;

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
