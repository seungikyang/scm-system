// 실제 구현 위치 예: src/main/java/com/example/scm/controller/ItemController.java
// 목표: 품목 REST API 와 권한/검증을 채우세요. TRD 3.7.4 참고.

// TODO 01: REST 컨트롤러임을 표시.
@____
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    // ============ 등록 (ADMIN) ============
    // TODO 02: 메서드/경로/권한을 채우세요.
    @____
    @PreAuthorize("hasRole('____')")
    public ResponseEntity<ItemResponse> create(@RequestBody @____ ItemCreateRequest request) {
        ItemResponse body = itemService.register(request);
        return ResponseEntity.status(HttpStatus.____).body(body);
    }

    // ============ 목록 / 검색 ============
    @GetMapping
    public Page<ItemResponse> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            // TODO 03: 기본 페이지 크기와 정렬을 지정.
            @____(size = 20, sort = "id") Pageable pageable
    ) {
        return itemService.search(keyword, categoryId, pageable);
    }

    // ============ 상세 ============
    @GetMapping("/{itemId}")
    public ItemResponse get(@PathVariable Long itemId) {
        return itemService.getDetail(itemId);
    }

    // ============ 수정 (ADMIN) ============
    @PutMapping("/{itemId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ItemResponse update(@PathVariable Long itemId,
                               @RequestBody @Valid ItemUpdateRequest request) {
        return itemService.update(itemId, request);
    }

    // ============ 단종 처리 (ADMIN) ============
    // TODO 04: 상태 전이를 동사형 경로로 표현.
    @____("/{itemId}/discontinue")
    @PreAuthorize("hasRole('ADMIN')")
    public ItemResponse discontinue(@PathVariable Long itemId) {
        return itemService.discontinue(itemId);
    }
}

// ===== Request DTO =====
// 17-dto-validation 의 ItemCreateRequest 와 짝이 되는 수정용 DTO.
public record ItemUpdateRequest(
        // TODO 05: 수정 요청은 부분 갱신 허용 — null 이면 변경 안 함.
        //          그래서 @NotBlank 가 아닌 @Size 로 길이만 검증.
        @____(max = 150) String name,
        // 카테고리 변경은 선택적이므로 null 허용.
        Long categoryId,
        @Size(max = 20) String unit,
        // TODO 06: 단가 변경 시 0 이상 검증.
        @____(value = "0.00") BigDecimal unitPrice,
        // TODO 07: 안전재고 변경 시 음수 금지.
        @____(value = 0) Integer safetyStock
) {}

// 학습 질문:
// Q1. @PathVariable, @RequestBody, @RequestParam 의 차이는?
//     A:
// Q2. @PreAuthorize 를 쓰려면 어떤 설정이 필요한가?
//     A:
// Q3. 단종 처리 경로를 DELETE 가 아니라 PATCH .../discontinue 로 표현한 이유는?
//     A:
// Q4. ItemUpdateRequest 의 모든 필드가 nullable 일 때 어떤 위험이 있는가?
//     A:
// Q5. PUT(전체 갱신) 시멘틱과 PATCH(부분 갱신) 시멘틱 중 이 DTO 는 어느 쪽에 가까운가?
//     A:
