package com.example.scm.dto.purchaseorder;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 발주 반려 요청 (REST Body + Web 반려 모달 폼). (02_contracts §1.8, §3.4 rejectForm)
 */
@Getter
@Setter
@NoArgsConstructor
public class PurchaseOrderRejectRequest {

    @NotBlank(message = "반려 사유는 필수입니다.")
    @Size(max = 500, message = "반려 사유는 500자 이하여야 합니다.")
    private String rejectReason;
}
