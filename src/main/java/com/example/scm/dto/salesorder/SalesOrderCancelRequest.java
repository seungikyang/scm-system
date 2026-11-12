package com.example.scm.dto.salesorder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
/**
 * 고객 주문을 취소할 때 적는 이유입니다.
 * 빈 이유를 허용하지 않아 나중에 주문을 취소한 까닭을 확인할 수 있게 합니다.
 */
@Getter @Setter
public class SalesOrderCancelRequest {
    @NotBlank(message = "취소 사유는 필수입니다.") @Size(max = 500)
    private String cancelReason;
}
