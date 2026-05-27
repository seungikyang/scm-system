// 실제 구현 위치 예: src/main/java/com/example/scm/dto/ItemCreateRequest.java
// 목표: 품목 등록 요청 DTO 의 Validation 어노테이션을 채우세요. TRD 3.7.4 참고.

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ItemCreateRequest {

    // TODO 01: 품목코드는 공백 금지 + 길이 1~50.
    @____(message = "품목코드는 필수입니다.")
    @Size(min = ____, max = ____, message = "품목코드는 1~50자입니다.")
    private String itemCode;

    // TODO 02: 품목명은 공백 금지 + 길이 1~150.
    @NotBlank(message = "품목명은 필수입니다.")
    @Size(min = 1, max = ____)
    private String name;

    // TODO 03: 카테고리 ID 는 null 이 아니어야 합니다.
    @____(message = "카테고리 ID 는 필수입니다.")
    private Long categoryId;

    @NotBlank(message = "단위는 필수입니다.")
    private String unit;

    // TODO 04: 단가는 BigDecimal 이며 0 또는 양수여야 합니다. 어떤 어노테이션?
    @____(message = "단가는 필수입니다.")
    @DecimalMin(value = "0.00", message = "단가는 0 이상이어야 합니다.")
    private BigDecimal unitPrice;

    // TODO 05: 안전재고는 음수 금지.
    @____(message = "안전재고는 음수일 수 없습니다.")
    private Integer safetyStock;
}

// ===== 비교용: 응답 DTO =====
public record ItemResponse(
        Long itemId,
        String itemCode,
        String name,
        String categoryName,
        String unit,
        BigDecimal unitPrice,
        Integer safetyStock,
        String status
) {
    public static ItemResponse from(Item item) {
        return new ItemResponse(
            item.getId(),
            item.getItemCode(),
            item.getName(),
            item.getCategory().getName(),
            item.getUnit(),
            item.getUnitPrice(),
            item.getSafetyStock(),
            item.getStatus().name()
        );
    }
}

// 학습 질문:
// Q1. @NotNull / @NotBlank / @NotEmpty 의 차이는?
//     A:
// Q2. @Valid 가 동작하려면 어떤 의존성과 어디서 호출이 필요한가?
//     A:
// Q3. 요청 DTO 와 응답 DTO 를 같은 클래스로 두면 어떤 문제가 생기는가?
//     A:
