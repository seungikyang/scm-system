// 실제 구현 위치 예: src/main/java/com/example/scm/controller/PurchaseOrderController.java
// 목표: 발주 일반/관리자 경로 분리, 상태 전이 API 를 채우세요. TRD 3.7.5 참고.

@RestController
@RequiredArgsConstructor
public class PurchaseOrderController {

    private final PurchaseOrderService purchaseOrderService;

    // ============ 사용자 영역: /api/purchase-orders ============

    @PostMapping("/api/purchase-orders")
    public ResponseEntity<PurchaseOrderResponse> create(
            // TODO 01: 로그인 사용자의 ID 를 어떻게 받을까요?
            @____ Long currentUserId,
            @RequestBody @Valid PurchaseOrderCreateRequest request
    ) {
        PurchaseOrderResponse body = purchaseOrderService.create(currentUserId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    // TODO 02: DRAFT → REQUESTED 동사형 경로를 채우세요.
    @____("/api/purchase-orders/{poId}/submit")
    public void submit(@CurrentUser Long currentUserId, @PathVariable Long poId) {
        purchaseOrderService.submit(currentUserId, poId);
    }

    @GetMapping("/api/purchase-orders/my")
    public Page<PurchaseOrderResponse> myList(
            @CurrentUser Long currentUserId,
            @PageableDefault(size = 20, sort = "id") Pageable pageable
    ) {
        return purchaseOrderService.myList(currentUserId, pageable);
    }

    @GetMapping("/api/purchase-orders/{poId}")
    public PurchaseOrderResponse detail(@CurrentUser Long currentUserId, @PathVariable Long poId) {
        return purchaseOrderService.getDetail(currentUserId, poId);
    }

    @PatchMapping("/api/purchase-orders/{poId}/cancel")
    public void cancel(@CurrentUser Long currentUserId, @PathVariable Long poId) {
        purchaseOrderService.cancel(currentUserId, poId);
    }

    // ============ 관리자 영역: /api/admin/purchase-orders ============

    // TODO 03: 관리자만 접근 가능하도록 보호.
    @GetMapping("/api/admin/purchase-orders")
    @PreAuthorize("hasRole('____')")
    public Page<PurchaseOrderResponse> adminList(
            @RequestParam(required = false) PurchaseOrderStatus status,
            @RequestParam(required = false) Long partnerId,
            @PageableDefault(size = 20, sort = "id") Pageable pageable
    ) {
        return purchaseOrderService.adminList(status, partnerId, pageable);
    }

    @PatchMapping("/api/admin/purchase-orders/{poId}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public PurchaseOrderResponse approve(@CurrentUser Long currentUserId, @PathVariable Long poId) {
        return purchaseOrderService.approve(currentUserId, poId);
    }

    @PatchMapping("/api/admin/purchase-orders/{poId}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public PurchaseOrderResponse reject(
            @CurrentUser Long currentUserId,
            @PathVariable Long poId,
            // TODO 04: 반려 사유는 어디서 받을까? (Body 권장)
            @RequestBody @Valid PurchaseOrderRejectRequest request
    ) {
        return purchaseOrderService.reject(currentUserId, poId, request.getReason());
    }

    @PatchMapping("/api/admin/purchase-orders/{poId}/receive")
    @PreAuthorize("hasRole('ADMIN')")
    public PurchaseOrderResponse receive(@CurrentUser Long currentUserId, @PathVariable Long poId) {
        return purchaseOrderService.receive(currentUserId, poId);
    }
}

// ===== Request DTO =====

public record PurchaseOrderCreateRequest(
        // TODO 05: 공급사 ID 는 null 이 아니어야 함.
        @____ Long partnerId,
        @NotNull LocalDate orderDate,
        LocalDate dueDate,
        // TODO 06: 라인 ≥ 1 검증을 D 계층에서 어떻게 표현할까?
        @____(min = 1, message = "발주 라인은 최소 1개 이상이어야 합니다.")
        @Valid
        List<LineDto> lines
) {
    public record LineDto(
            @NotNull Long itemId,
            // TODO 07: 수량은 1 이상이어야 함.
            @NotNull @____ Integer quantity,
            // TODO 08: 단가는 0 이상이어야 함.
            @NotNull @____(value = "0.00") BigDecimal unitPrice
    ) {}
}

@Getter
@NoArgsConstructor
public class PurchaseOrderRejectRequest {
    @NotBlank
    @Size(max = 500)
    private String reason;
}

// 학습 질문:
// Q1. /api/purchase-orders 와 /api/admin/purchase-orders 를 분리한 이유는?
//     A:
// Q2. PATCH .../approve 같은 동사형 경로의 장단점은?
//     A:
// Q3. @CurrentUser 의 동작 원리는? (Interceptor / ArgumentResolver)
//     A:
