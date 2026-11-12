package com.example.scm.dto.salesorder;

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
 * 고객 주문을 만들 때 고객사, 주문일, 출고 예정일, 물건 목록을 받습니다.
 * 물건은 최소 한 줄 있어야 하며 총금액은 서버에서 수량 × 단가를 더해 계산합니다.
 *
 * API와 수주 작성 화면에서 공유하는 입력값. 총금액은 서버에서 계산한다.
 */
@Getter
@Setter
@NoArgsConstructor
public class SalesOrderCreateRequest {
    // @Getter/@Setter는 Lombok이 값을 읽고 쓰는 함수를 만들어 주는 표시입니다.
    // Spring은 그 함수를 이용해 입력칸의 값을 이 양식에 담습니다.

    @NotNull(message = "고객사는 필수입니다.")
    private Long partnerId;

    @NotNull(message = "수주일은 필수입니다.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate orderDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate shipDate;                        // 출고 예정일은 선택 입력이므로 비워 둘 수 있습니다.

    @NotEmpty(message = "수주 라인은 최소 1건 이상이어야 합니다.")
    // @NotEmpty는 목록 크기만 검사하므로 [null]처럼 내용이 없는 행도 별도로 차단한다.
    // @Valid도 원소에 붙여 각 행 내부의 품목·수량·단가 제약까지 검사한다.
    private List<@NotNull(message = "수주 라인은 비어 있을 수 없습니다.") @Valid LineRequest> lines = new ArrayList<>();

    @Getter
    @Setter
    @NoArgsConstructor
    public static class LineRequest {
        // @Valid가 이 양식까지 들어와 검사합니다. @Positive는 0을 제외한 양수만 허용합니다.

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
