// 실제 구현 위치 예: src/main/java/com/example/scm/controller/SalesOrderController.java
// 목표: 수주 REST API + DTO 전체 셋. TRD 3.7.7 참고.

@RestController
@RequestMapping("/api/sales-orders")
@RequiredArgsConstructor
public class SalesOrderController {

    private final SalesOrderService salesOrderService;

    // TODO 01: 수주서 작성 (DRAFT). 일반 사용자도 가능합니다.
    @PostMapping
    public ResponseEntity<SalesOrderResponse> create(
            @CurrentUser Long currentUserId,
            @Valid @RequestBody SalesOrderCreateRequest request) {

        SalesOrderResponse response = salesOrderService.create(currentUserId, request);
        return ResponseEntity.status(HttpStatus.____).body(response);
    }

    // TODO 02: 수주 확정 (DRAFT → CONFIRMED). 상태 전이를 동사형 경로로.
    @PatchMapping("/{soId}/____")
    public SalesOrderResponse confirm(
            @CurrentUser Long currentUserId,
            @PathVariable Long soId) {
        return salesOrderService.confirm(currentUserId, soId);
    }

    // TODO 03: 내가 작성한 수주 목록.
    @GetMapping("/my")
    public Page<SalesOrderResponse> my(
            @CurrentUser Long currentUserId,
            @RequestParam(required = false) SalesOrderStatus status,
            @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return salesOrderService.findMy(currentUserId, status, pageable);
    }

    // TODO 04: 처리 대기 수주 목록 (CONFIRMED). 권한은 MANAGER 또는 ADMIN.
    @GetMapping("/pending")
    @PreAuthorize("hasAnyRole('____', '____')")
    public Page<SalesOrderResponse> pending(
            @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return salesOrderService.pendingList(pageable);
    }

    // TODO 05: 상세 조회 — 작성자 / 처리자 / ADMIN 만 허용.
    @GetMapping("/{soId}")
    public SalesOrderResponse detail(
            @CurrentUser Long currentUserId,
            @CurrentUserRole UserRole currentRole,
            @PathVariable Long soId) {
        return salesOrderService.detail(currentUserId, currentRole, soId);
    }

    // ============ 매니저/관리자 상태 전이 ============

    @PatchMapping("/{soId}/ship")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public SalesOrderResponse ship(
            @CurrentUser Long currentUserId,
            @PathVariable Long soId) {
        return salesOrderService.ship(currentUserId, soId);
    }

    @PatchMapping("/{soId}/complete")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public SalesOrderResponse complete(
            @CurrentUser Long currentUserId,
            @PathVariable Long soId) {
        return salesOrderService.complete(currentUserId, soId);
    }

    @PatchMapping("/{soId}/cancel")
    public SalesOrderResponse cancel(
            @CurrentUser Long currentUserId,
            @PathVariable Long soId,
            @Valid @RequestBody SalesOrderCancelRequest request) {
        return salesOrderService.cancel(currentUserId, soId, request.reason());
    }
}


// ===== Request DTO =====

public record SalesOrderCreateRequest(
        @NotNull Long partnerId,
        @NotNull LocalDate orderDate,
        LocalDate shipDate,
        // TODO 06: 라인 ≥ 1 검증을 D 계층에서 어떻게 표현할까?
        @____(min = 1, message = "수주 라인은 최소 1개 이상이어야 합니다.")
        @Valid
        List<LineDto> lines
) {
    public record LineDto(
            @NotNull Long itemId,
            @NotNull @Positive Integer quantity,
            @NotNull @DecimalMin("0.00") BigDecimal unitPrice
    ) {}
}

public record SalesOrderCancelRequest(
        // TODO 07: 취소 사유는 비어 있으면 안 되는 이유는?
        @NotBlank @Size(max = 500) String reason
) {}


// ===== Response DTO =====

public record SalesOrderResponse(
        Long salesOrderId,
        String orderNumber,
        Long partnerId,
        String partnerName,
        Long writerId,
        Long managerId,
        LocalDate orderDate,
        LocalDate shipDate,
        BigDecimal totalAmount,
        SalesOrderStatus status,
        String cancelReason,
        LocalDateTime createdAt,
        LocalDateTime confirmedAt,
        LocalDateTime shippedAt,
        LocalDateTime completedAt,
        List<SalesOrderLineResponse> lines
) {
    public static SalesOrderResponse from(SalesOrder so) {
        return new SalesOrderResponse(
                so.getId(),
                so.getOrderNumber(),
                so.getPartner().getId(),
                so.getPartner().getName(),
                so.getWriterId(),
                so.getManagerId(),
                so.getOrderDate(),
                so.getShipDate(),
                so.getTotalAmount(),
                so.getStatus(),
                so.getCancelReason(),
                so.getCreatedAt(),
                so.getConfirmedAt(),
                so.getShippedAt(),
                so.getCompletedAt(),
                so.getLines().stream().map(SalesOrderLineResponse::from).toList()
        );
    }
}

public record SalesOrderLineResponse(
        Long lineId,
        Long itemId,
        String itemCode,
        String itemName,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal lineAmount
) {
    public static SalesOrderLineResponse from(SalesOrderLine l) {
        return new SalesOrderLineResponse(
                l.getId(),
                l.getItem().getId(),
                l.getItem().getItemCode(),
                l.getItem().getName(),
                l.getQuantity(),
                l.getUnitPrice(),
                l.getLineAmount()
        );
    }
}


// 학습 질문:
// Q1. /my 와 /pending 의 권한 차이를 정리해 보세요.
//     A:
// Q2. 수주 작성 후 자동으로 confirm 까지 하지 않고 별도 호출로 둔 이유는?
//     A:
// Q3. 처리자(매니저)가 본인이 아닌 사람이 /pending 을 호출하면 어떻게 막혀야 하는가?
//     A:
// Q4. cancel 의 권한 검사를 Controller @PreAuthorize 가 아닌 Service 가드에 둔 이유는?
//     A:
// Q5. lines 가 null 이 아니라 빈 List 일 때도 막으려면 어떤 검증을 추가해야 할까?
//     A:
