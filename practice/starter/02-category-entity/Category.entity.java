// 실제 구현 위치 예: src/main/java/com/example/scm/domain/Category.java
// 목표: 품목 카테고리 엔티티를 채우세요. TRD 3.3.3, 3.5.3 참고.

@Entity
// TODO 01: 테이블명을 명시하세요. (복수형)
@Table(name = "____")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO 02: 카테고리명은 사용자 식별성이 높아 unique 가 필요합니다.
    @Column(nullable = false, length = 100, unique = ____)
    private String name;

    // 카테고리 설명 (선택)
    @Column(length = 255)
    private String description;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    // ===== 정적 팩토리 =====
    public static Category create(String name, String description) {
        Category c = new Category();
        // TODO 03: name 은 trim 한 뒤 저장하는 편이 안전합니다.
        c.name = name.____;
        c.description = description;
        return c;
    }

    // ===== 도메인 메서드 =====
    public void update(String name, String description) {
        // TODO 04: 빈 문자열 갱신을 막을 가드를 넣어 보세요.
        if (name != null && !name.isBlank()) {
            this.name = name.trim();
        }
        this.description = description;
    }
}

// 학습 질문:
// Q1. 카테고리 → 품목 방향으로 @OneToMany 를 매핑할 때의 장단점은?
//     A:
// Q2. 같은 이름의 두 카테고리가 동시에 등록되는 race 가 생기면 어디서 막아야 하는가?
//     A:
