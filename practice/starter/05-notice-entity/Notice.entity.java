// 실제 구현 위치 예: src/main/java/com/example/scm/domain/Notice.java
// 목표: 공지사항 엔티티를 채우세요. TRD 3.3.7, 3.5.7 참고.

@Entity
@Table(name = "notices")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    // TODO 01: 긴 본문을 저장하려면? (두 가지 표현 중 하나 선택)
    @Column(nullable = false, columnDefinition = "____")
    private String content;

    @Column(name = "writer_id", nullable = false)
    private Long writerId;

    // TODO 02: 중요 공지 여부를 boolean 으로 표현하세요.
    @Column(nullable = false)
    private ____ important;

    @Column(nullable = false)
    private Long viewCount;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    // ===== 정적 팩토리 =====
    public static Notice create(Long writerId, String title, String content, Boolean important) {
        Notice n = new Notice();
        n.writerId = writerId;
        n.title = title.trim();
        n.content = content;
        // TODO 03: important 가 null 이면 기본값을 false 로.
        n.important = (important == null) ? ____ : important;
        // TODO 04: 신규 공지의 조회수 초기값은?
        n.viewCount = ____L;
        return n;
    }

    public void update(String title, String content, Boolean important) {
        if (title != null && !title.isBlank()) this.title = title.trim();
        if (content != null) this.content = content;
        if (important != null) this.important = important;
    }

    public void increaseViewCount() {
        // TODO 05: 단순 +1 의 동시성 문제를 한 줄로 적어 보세요.
        // A:
        this.viewCount = this.viewCount + 1;
    }
}

// 학습 질문:
// Q1. 중요 공지를 위로 올리는 정렬 SQL/JPQL 은?
//     A:
// Q2. viewCount 를 안전하게 증가시키는 원자적 쿼리는?
//     A:
