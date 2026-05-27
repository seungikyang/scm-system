// 실제 구현 위치 예: src/main/java/com/example/scm/domain/Item.java
// 목표: 품목 엔티티와 ItemStatus enum 을 채우세요. TRD 3.3.4, 3.5.4, 3.6.3 참고.

// ===== ItemStatus enum =====
public enum ItemStatus {
    // TODO 01: 활성 / 단종 두 가지 상태를 채우세요.
    ____, ____
}

// ===== Item entity =====
@Entity
@Table(name = "items",
       uniqueConstraints = {
           // TODO 02: 품목코드는 유일 식별자입니다. 어떤 제약을 추가할까요?
           @UniqueConstraint(name = "uk_item_code", columnNames = "____")
       })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO 03: 품목코드 컬럼. 길이 50.
    @Column(nullable = false, length = ____, unique = true)
    private String itemCode;

    @Column(nullable = false, length = 150)
    private String name;

    // TODO 04: Category 와의 관계는? (N:1)
    @____(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false, length = 20)
    private String unit;     // EA, BOX, KG 등

    // TODO 05: 단가/금액은 double 대신 어떤 타입이 안전할까요?
    @Column(nullable = false, precision = 15, scale = 2)
    private ____ unitPrice;

    @Column(nullable = false)
    private Integer safetyStock;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ItemStatus status;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    // ===== 정적 팩토리 =====
    public static Item create(Category category,
                              String itemCode,
                              String name,
                              String unit,
                              BigDecimal unitPrice,
                              Integer safetyStock) {
        Item i = new Item();
        i.category = category;
        i.itemCode = itemCode.trim();
        i.name = name.trim();
        i.unit = unit;
        i.unitPrice = unitPrice;
        i.safetyStock = (safetyStock == null) ? 0 : safetyStock;
        // TODO 06: 신규 품목의 초기 상태는?
        i.status = ItemStatus.____;
        return i;
    }

    // ===== 도메인 메서드 =====
    public void changeCategory(Category newCategory) {
        // TODO 07: null 카테고리를 거부하는 가드를 넣어 보세요.
        if (newCategory == null) {
            throw new IllegalArgumentException("____");
        }
        this.category = newCategory;
    }

    public void updateProfile(String name, String unit, BigDecimal unitPrice, Integer safetyStock) {
        if (name != null && !name.isBlank()) this.name = name.trim();
        if (unit != null && !unit.isBlank()) this.unit = unit;
        if (unitPrice != null) this.unitPrice = unitPrice;
        if (safetyStock != null) this.safetyStock = safetyStock;
    }

    public void discontinue() {
        // TODO 08: 이미 단종된 품목을 다시 단종 처리하면 어떻게 할까요?
        if (this.status == ItemStatus.____) {
            throw new IllegalStateException("이미 단종된 품목입니다.");
        }
        this.status = ItemStatus.DISCONTINUED;
    }
}

// 학습 질문:
// Q1. fetch = LAZY 가 기본이어야 하는 이유는?
//     A:
// Q2. soft delete(DISCONTINUED) 가 hard delete 보다 안전한 이유는?
//     A:
// Q3. 단가를 BigDecimal 로 두는 이유 (double 의 문제)?
//     A:
