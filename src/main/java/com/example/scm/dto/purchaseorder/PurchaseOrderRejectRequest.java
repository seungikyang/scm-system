package com.example.scm.dto.purchaseorder;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** REST JSON 본문과 Web 반려 모달이 함께 사용하는 반려 사유 입력 DTO. */
@Getter
@Setter
@NoArgsConstructor
public class PurchaseOrderRejectRequest {

    @NotBlank(message = "반려 사유는 필수입니다.")
    @Size(max = 500, message = "반려 사유는 500자 이하여야 합니다.")
    private String rejectReason;
}
