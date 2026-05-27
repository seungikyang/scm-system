// 실제 구현 위치 예: src/main/java/com/example/scm/controller/NoticeController.java
// 목표: 공지 REST 컨트롤러 + 응답 DTO 를 채우세요. TRD 3.7.6 참고.

@RestController
@RequestMapping("/api/notices")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NoticeResponse> create(
            @CurrentUser Long currentUserId,
            @RequestBody @Valid NoticeCreateRequest request
    ) {
        return ResponseEntity
            .status(HttpStatus.____)
            .body(noticeService.create(currentUserId, request));
    }

    @GetMapping
    public Page<NoticeListResponse> list(
            // TODO 01: 기본 페이지 크기/정렬을 지정. 중요 공지 정렬은 Service 안에서 처리.
            @PageableDefault(size = ____, sort = "createdAt") Pageable pageable
    ) {
        return noticeService.list(pageable).map(NoticeListResponse::from);
    }

    @GetMapping("/{noticeId}")
    public NoticeResponse detail(@PathVariable Long noticeId) {
        return noticeService.get(noticeId);
    }

    @PutMapping("/{noticeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public NoticeResponse update(
            @CurrentUser Long currentUserId,
            @PathVariable Long noticeId,
            @RequestBody @Valid NoticeUpdateRequest request
    ) {
        return noticeService.update(currentUserId, noticeId, request);
    }

    @DeleteMapping("/{noticeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@CurrentUser Long currentUserId, @PathVariable Long noticeId) {
        noticeService.delete(currentUserId, noticeId);
        return ResponseEntity.noContent().build();
    }
}

// ===== DTO =====
@Getter @NoArgsConstructor
public class NoticeCreateRequest {
    @NotBlank @Size(max = 200)
    private String title;
    @NotBlank
    private String content;
    private Boolean important;
}

// ===== Update DTO =====
public record NoticeUpdateRequest(
        // TODO 03: 부분 갱신 — null 이면 변경 안 함. 변경 시에는 blank 금지.
        @____(max = 200) String title,
        // 본문은 길이 제한 없음 (TEXT 컬럼).
        String content,
        // 중요 공지 플래그 변경.
        Boolean important
) {}

public record NoticeResponse(
        Long noticeId,
        String title,
        String content,
        Long writerId,
        Boolean important,
        Long viewCount,
        LocalDateTime createdAt
) {
    public static NoticeResponse from(Notice n) {
        return new NoticeResponse(
            n.getId(), n.getTitle(), n.getContent(),
            n.getWriterId(), n.getImportant(), n.getViewCount(),
            n.getCreatedAt()
        );
    }
}

// TODO 02: 목록 응답에는 긴 content 대신 미리보기만 넣어 보세요.
public record NoticeListResponse(
        Long noticeId,
        String title,
        String contentPreview,
        Boolean important,
        Long viewCount,
        LocalDateTime createdAt
) {
    public static NoticeListResponse from(NoticeResponse r) {
        String preview = (r.content() == null) ? "" :
            r.content().length() > ____ ? r.content().substring(0, ____) + "..." : r.content();
        return new NoticeListResponse(
            r.noticeId(), r.title(), preview,
            r.important(), r.viewCount(), r.createdAt()
        );
    }
}

// 학습 질문:
// Q1. 목록 API 에 content 전체를 담으면 어떤 부작용이 있는가?
//     A:
// Q2. GET 상세에서 viewCount 를 증가시키지 않는다면 어떤 대안이 있는가?
//     A:
