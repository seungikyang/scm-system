// 실제 구현 위치 예: src/main/java/com/example/scm/controller/PartnerController.java
// 목표: 거래처 REST 컨트롤러를 채우세요. TRD 3.7.2 참고.

@RestController
@RequestMapping("/api/partners")
@RequiredArgsConstructor
public class PartnerController {

    private final PartnerService partnerService;

    @PostMapping
    // TODO 01: 관리자만.
    @PreAuthorize("hasRole('____')")
    public ResponseEntity<PartnerResponse> create(@RequestBody @Valid PartnerCreateRequest request) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(partnerService.register(request));
    }

    @GetMapping
    public Page<PartnerResponse> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) PartnerType type,
            @PageableDefault(size = 20, sort = "id") Pageable pageable
    ) {
        return partnerService.search(keyword, type, pageable);
    }

    @GetMapping("/{partnerId}")
    public PartnerResponse detail(@PathVariable Long partnerId) {
        return partnerService.getDetail(partnerId);
    }

    @PutMapping("/{partnerId}")
    @PreAuthorize("hasRole('ADMIN')")
    public PartnerResponse update(@PathVariable Long partnerId,
                                  @RequestBody @Valid PartnerUpdateRequest request) {
        return partnerService.update(partnerId, request);
    }

    @DeleteMapping("/{partnerId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long partnerId) {
        partnerService.deactivate(partnerId);
        // TODO 02: 삭제 또는 비활성 후 응답 status?
        return ResponseEntity.____().build();
    }
}

// ===== DTO =====
@Getter @NoArgsConstructor
public class PartnerCreateRequest {
    @NotBlank @Size(max = 150)
    private String name;

    // TODO 03: 사업자번호 패턴 검증 (예: 000-00-00000).
    @NotBlank
    @Pattern(regexp = "____", message = "사업자번호 형식이 올바르지 않습니다.")
    private String businessNumber;

    @NotNull
    private PartnerType partnerType;

    private String contactName;
    private String phone;
    @Email
    private String email;
    private String address;
}

// ===== Update DTO =====
public record PartnerUpdateRequest(
        // TODO 04: 부분 갱신 정책 — null 이면 변경 안 함. @NotBlank 가 아닌 @Size 만.
        @____(max = 150) String name,
        // 사업자번호 변경 시에도 패턴 검증.
        @Pattern(regexp = "____", message = "사업자번호 형식이 올바르지 않습니다.") String businessNumber,
        PartnerType partnerType,
        String contactName,
        String phone,
        @Email String email,
        String address
) {}

public record PartnerResponse(
        Long partnerId,
        String name,
        String businessNumber,
        String partnerType,
        String contactName,
        String phone,
        String email,
        String address,
        String status
) {
    public static PartnerResponse from(Partner p) {
        return new PartnerResponse(
            p.getId(),
            p.getName(),
            p.getBusinessNumber(),
            p.getPartnerType().name(),
            p.getContactName(),
            p.getPhone(),
            p.getEmail(),
            p.getAddress(),
            p.getStatus().name()
        );
    }
}

// 학습 질문:
// Q1. 부분 갱신을 위해 PUT 대신 PATCH 를 쓰면 어떤 점이 달라지는가?
//     A:
// Q2. @PreAuthorize 와 Service requireRole 을 둘 다 두는 다층 방어의 의미는?
//     A:
