package com.example.scm.dto.purchaseorder;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 발주서 작성 요청 (REST Body + Web 폼 객체). totalAmount 없음(서버 계산, OQ-1). (02_contracts §1.1, §3.1)
 * Web 폼에서는 th:object="${purchaseOrderForm}" 로 바인딩. 라인은 lines[*].itemId/quantity/unitPrice 인덱스 바인딩.
 */
@Getter
@Setter
@NoArgsConstructor
public class PurchaseOrderCreateRequest {

    @NotNull(message = "공급사는 필수입니다.")
    private Long partnerId;

    @NotNull(message = "발주일은 필수입니다.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate orderDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dueDate;                        // nullable

    @Valid
    @NotEmpty(message = "발주 라인은 최소 1건 이상이어야 합니다.")
    private List<LineRequest> lines = new ArrayList<>();

    @Getter
    @Setter
    @NoArgsConstructor
    public static class LineRequest {

        @NotNull(message = "품목은 필수입니다.")
        private Long itemId;

        @NotNull(message = "수량은 필수입니다.")
        @Positive(message = "수량은 0보다 커야 합니다.")
        private Integer quantity;

        @PositiveOrZero(message = "단가는 0 이상이어야 합니다.")
        private BigDecimal unitPrice;                 // nullable → item.unitPrice 적용 (OQ-13)
    }
}
