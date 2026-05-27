// 실제 구현 위치 예: src/main/java/com/example/scm/controller/CategoryController.java
// 목표: 카테고리 REST API 5개를 채우세요. PRD 2.5.3, TRD 3.7.3 참고.
//
// 주의: 25-partner-controller 폴더 안에 PartnerController 와 CategoryController 를
//       함께 두었습니다. (24 폴더에 PartnerService + CategoryService 둔 것과 짝)

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // ============ 등록 (ADMIN) ============
    @PostMapping
    // TODO 01: ADMIN 만 등록 가능. 어떤 어노테이션?
    @PreAuthorize("hasRole('____')")
    public ResponseEntity<CategoryResponse> create(
            @RequestBody @Valid CategoryCreateRequest request) {
        return ResponseEntity
            .status(HttpStatus.____)
            .body(categoryService.create(request));
    }

    // ============ 목록 (로그인 사용자) ============
    @GetMapping
    public List<CategoryResponse> list() {
        // TODO 02: 카테고리는 보통 페이징 없이 전체. MVP 정책에 맞춰 List 반환.
        return categoryService.list();
    }

    // ============ 상세 (로그인 사용자, 소속 품목 포함) ============
    @GetMapping("/{categoryId}")
    public CategoryDetailResponse detail(
            @PathVariable Long categoryId,
            // TODO 03: 소속 품목 목록은 페이징을 적용한다.
            @____(size = 20, sort = "itemCode") Pageable itemPageable) {
        return categoryService.getDetail(categoryId, itemPageable);
    }

    // ============ 수정 (ADMIN) ============
    @PutMapping("/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryResponse update(
            @PathVariable Long categoryId,
            @RequestBody @Valid CategoryUpdateRequest request) {
        return categoryService.update(categoryId, request);
    }

    // ============ 삭제 (ADMIN) ============
    @DeleteMapping("/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long categoryId) {
        categoryService.delete(categoryId);
        // TODO 04: 삭제 성공 후 응답 status?
        return ResponseEntity.____().build();
    }
}

// 학습 질문:
// Q1. 카테고리 목록을 List 가 아닌 Page 로 반환한다면 어떤 경우에 적합한가?
//     A:
// Q2. 카테고리 상세에서 소속 품목까지 함께 페이징하는 nested response 의 장단점은?
//     A:
// Q3. 카테고리 삭제 시 소속 품목이 있으면 어떤 status 와 ErrorCode 가 응답되는가?
//     A:
// Q4. PUT 전체 갱신 vs PATCH 부분 갱신 — 카테고리에는 어느 쪽이 자연스러운가?
//     A:
// Q5. 카테고리 목록을 캐싱하려면 어떤 캐시 전략과 무효화 시점을 두어야 할까?
//     A:
