// 실제 구현 위치 예: src/main/java/com/example/scm/service/CategoryService.java
// 목표: 카테고리 CRUD + 삭제 정책을 채우세요. PRD 2.5.3, TRD 3.7.3 참고.
//
// 주의: 24 폴더 안에 PartnerService 와 CategoryService 를 함께 두었습니다.
//       Partner Service 와 패턴이 같아 비교하며 학습하기 좋습니다.

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ItemRepository itemRepository;

    // ============ 등록 ============
    @Transactional
    public CategoryResponse create(CategoryCreateRequest request) {
        String name = request.getName().trim();

        // TODO 01: 카테고리명 중복 검사. 어떤 ErrorCode?
        if (categoryRepository.existsByName(name)) {
            throw new BusinessException(ErrorCode.____);
        }

        Category category = Category.create(name, request.getDescription());
        return CategoryResponse.from(categoryRepository.save(category));
    }

    // ============ 목록 ============
    @Transactional(readOnly = true)
    public List<CategoryResponse> list() {
        // TODO 02: 카테고리는 수가 적어 MVP 에서는 전체 조회로 충분합니다.
        //          하지만 1000개를 넘어가면 어떤 대안을 두어야 할까요?
        // A:
        return categoryRepository.findAll(Sort.by("name").____())
            .stream()
            .map(CategoryResponse::from)
            .toList();
    }

    // ============ 상세 (소속 품목 포함) ============
    @Transactional(readOnly = true)
    public CategoryDetailResponse getDetail(Long categoryId, Pageable itemPageable) {
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new BusinessException(ErrorCode.____));

        // TODO 03: 소속 품목은 N+1 을 피하기 위해 어떻게 조회할까?
        //          (a) Category 양방향 매핑 + fetch join
        //          (b) 별도 Repository 쿼리 ← MVP 권장
        Page<ItemResponse> items = itemRepository.findByCategory_Id(categoryId, itemPageable)
            .map(ItemResponse::from);

        return new CategoryDetailResponse(
            category.getId(),
            category.getName(),
            category.getDescription(),
            items
        );
    }

    // ============ 수정 ============
    @Transactional
    public CategoryResponse update(Long categoryId, CategoryUpdateRequest request) {
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));

        // TODO 04: 이름이 바뀌었을 때만 중복 재검사.
        if (request.getName() != null
                && !request.getName().equals(category.getName())
                && categoryRepository.existsByName(request.getName())) {
            throw new BusinessException(ErrorCode.____);
        }

        category.update(request.getName(), request.getDescription());
        return CategoryResponse.from(category);
    }

    // ============ 삭제 (소속 품목 정책) ============
    @Transactional
    public void delete(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));

        // TODO 05: 소속 품목이 있으면 어떤 에러?
        long itemCount = itemRepository.countByCategory_Id(categoryId);
        if (itemCount > 0) {
            throw new BusinessException(ErrorCode.____);
        }

        categoryRepository.delete(category);
    }
}

// ===== DTO =====
@Getter @NoArgsConstructor
public class CategoryCreateRequest {
    @NotBlank @Size(max = 100)
    private String name;
    @Size(max = 255)
    private String description;
}

@Getter @NoArgsConstructor
public class CategoryUpdateRequest {
    @Size(max = 100)
    private String name;
    @Size(max = 255)
    private String description;
}

public record CategoryResponse(
        Long categoryId,
        String name,
        String description
) {
    public static CategoryResponse from(Category c) {
        return new CategoryResponse(c.getId(), c.getName(), c.getDescription());
    }
}

public record CategoryDetailResponse(
        Long categoryId,
        String name,
        String description,
        Page<ItemResponse> items
) {}

// 학습 질문:
// Q1. 카테고리 삭제를 거부하는 정책 vs 품목을 "기본 카테고리" 로 이관하는 정책의 트레이드오프는?
//     A:
// Q2. 카테고리명 unique 를 DB unique + Service exists 둘 다 두는 이유는?
//     A:
// Q3. 카테고리 상세에 소속 품목까지 넣으면 응답 페이로드가 커진다. 어떻게 분리할까?
//     A:
// Q4. 카테고리 목록 응답을 캐싱한다면 어떤 무효화(invalidation) 전략이 필요한가?
//     A:
