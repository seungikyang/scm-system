package com.example.scm.dto.partner;

import com.example.scm.domain.Partner;
import com.example.scm.domain.enums.PartnerType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 거래처 등록/수정 폼 객체 (Web). th:object="${partnerForm}".
 */
@Getter
@Setter
@NoArgsConstructor
public class PartnerForm {

    @NotBlank(message = "거래처명은 필수입니다.")
    @Size(max = 150, message = "거래처명은 150자 이하여야 합니다.")
    private String name;

    @NotBlank(message = "사업자번호는 필수입니다.")
    @Size(max = 20, message = "사업자번호는 20자 이하여야 합니다.")
    private String businessNumber;

    @NotNull(message = "거래처 유형은 필수입니다.")
    private PartnerType partnerType;

    @Size(max = 50, message = "담당자명은 50자 이하여야 합니다.")
    private String contactName;

    @Size(max = 30, message = "연락처는 30자 이하여야 합니다.")
    private String phone;

    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @Size(max = 100, message = "이메일은 100자 이하여야 합니다.")
    private String email;

    @Size(max = 255, message = "주소는 255자 이하여야 합니다.")
    private String address;

    public static PartnerForm from(Partner partner) {
        PartnerForm form = new PartnerForm();
        form.name = partner.getName();
        form.businessNumber = partner.getBusinessNumber();
        form.partnerType = partner.getPartnerType();
        form.contactName = partner.getContactName();
        form.phone = partner.getPhone();
        form.email = partner.getEmail();
        form.address = partner.getAddress();
        return form;
    }
}
