package com.example.scm.dto.purchaseorder;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
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
 * REST JSON 본문과 Web 발주 폼이 함께 사용하는 발주 작성 DTO.
 *
 * <p>학습 모듈 17: 바깥 DTO의 필수값을 먼저 보고, {@code @Valid}가 중첩 LineRequest의
 * 제약까지 이어 주는 과정을 읽는다. 모듈 19에서 Controller 바인딩과 연결한다.</p>
 *
 * <p>총액 필드가 없는 이유는 클라이언트가 보낸 금액을 신뢰하지 않고 Service가 라인
 * 금액을 합산하기 때문이다. Web 폼에서는 {@code th:object="${purchaseOrderForm}"}로
 * 바인딩하며, 라인은 {@code lines[0].itemId} 같은 인덱스 이름으로 List에 들어온다.</p>
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

    @NotEmpty(message = "발주 라인은 최소 1건 이상이어야 합니다.")
    // @NotEmpty는 목록 크기만 검사하므로 [null]처럼 내용이 없는 행도 별도로 차단한다.
    // @Valid도 원소에 붙여 각 행 내부의 품목·수량·단가 제약까지 검사한다.
    private List<@NotNull(message = "발주 라인은 비어 있을 수 없습니다.") @Valid LineRequest> lines = new ArrayList<>();

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
        @Digits(integer = 13, fraction = 2, message = "단가는 정수 13자리, 소수 2자리 이하여야 합니다.")
        private BigDecimal unitPrice;                 // 생략하면 Service가 품목 표준단가를 적용
    }
}
