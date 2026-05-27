// 실제 구현 위치 예:
//   src/main/java/com/example/scm/domain/SalesOrder.java
//   src/main/java/com/example/scm/domain/SalesOrderLine.java
// 목표: 수주서(헤더) + 수주서 라인 + SalesOrderStatus enum 을 채우세요.
//       TRD 3.3.8, 3.3.9, 3.6.5 참고.

// ===== SalesOrderStatus enum =====
public enum SalesOrderStatus {
    // TODO 01: 수주 상태 전이를 보고 5가지를 채우세요.
    //          DRAFT → CONFIRMED → SHIPPED → COMPLETED
    //          위 중 어디서든 CANCELED 로
    ____, ____, ____, ____, ____
}

// ===== SalesOrder entity =====
@Entity
@Table(name = "sales_orders",
       uniqueConstraints = @UniqueConstraint(name = "uk_so_order_number", columnNames = "order_number"))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class SalesOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30, unique = true)
    private String orderNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "partner_id", nullable = false)
    private Partner partner;

    @Column(name = "writer_id", nullable = false)
    private Long writerId;

    @Column(name = "manager_id")
    private Long managerId;

    @Column(nullable = false)
    private LocalDate orderDate;
    private LocalDate shipDate;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private SalesOrderStatus status;

    @Column(length = 500)
    private String cancelReason;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime confirmedAt;
    private LocalDateTime shippedAt;
    private LocalDateTime completedAt;

    @OneToMany(mappedBy = "salesOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SalesOrderLine> lines = new ArrayList<>();

    // ===== 정적 팩토리 =====
    public static SalesOrder create(String orderNumber,
                                    Partner partner,
                                    Long writerId,
                                    LocalDate orderDate,
                                    LocalDate shipDate) {
        SalesOrder so = new SalesOrder();
        so.orderNumber = orderNumber;
        so.partner = partner;
        so.writerId = writerId;
        so.orderDate = orderDate;
        so.shipDate = shipDate;
        so.totalAmount = BigDecimal.ZERO;
        // TODO 02: 신규 수주서의 초기 상태?
        so.status = SalesOrderStatus.____;
        return so;
    }

    public void addLine(Item item, int quantity, BigDecimal unitPrice) {
        if (quantity <= 0) throw new IllegalArgumentException("수량은 1 이상이어야 합니다.");
        SalesOrderLine line = SalesOrderLine.create(this, item, quantity, unitPrice);
        this.lines.add(line);
        recalculateTotal();
    }

    public void recalculateTotal() {
        this.totalAmount = this.lines.stream()
            .map(SalesOrderLine::getLineAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // ===== 상태 전이 도메인 메서드 =====
    public void confirm(Long currentUserId) {
        if (!this.writerId.equals(currentUserId)) {
            throw new AccessDeniedException("작성자만 확정할 수 있습니다.");
        }
        // TODO 03: DRAFT 가 아니면 막는다.
        if (this.status != SalesOrderStatus.____) {
            throw new IllegalStateException("DRAFT 상태에서만 확정할 수 있습니다.");
        }
        if (this.lines.isEmpty()) throw new IllegalStateException("라인이 비어 있습니다.");
        this.status = SalesOrderStatus.CONFIRMED;
        this.confirmedAt = LocalDateTime.now();
    }

    public void ship(Long managerUserId) {
        if (this.status != SalesOrderStatus.CONFIRMED) {
            throw new IllegalStateException("CONFIRMED 상태에서만 출고할 수 있습니다.");
        }
        this.managerId = managerUserId;
        this.status = SalesOrderStatus.SHIPPED;
        // TODO 04: 출고 시각 세팅.
        this.shippedAt = ____;
    }

    public void complete() {
        // TODO 05: SHIPPED 가 아니면 막는다.
        if (this.status != SalesOrderStatus.____) {
            throw new IllegalStateException("SHIPPED 상태에서만 완료할 수 있습니다.");
        }
        this.status = SalesOrderStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
    }

    public void cancel(String reason) {
        // TODO 06: 완료 또는 이미 취소된 수주는 다시 취소할 수 없다.
        if (this.status == SalesOrderStatus.COMPLETED || this.status == SalesOrderStatus.____) {
            throw new IllegalStateException("이 상태에서는 취소할 수 없습니다.");
        }
        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("취소 사유는 필수입니다.");
        }
        this.cancelReason = reason.trim();
        this.status = SalesOrderStatus.CANCELED;
    }
}

// ===== SalesOrderLine entity =====
@Entity
@Table(name = "sales_order_lines")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SalesOrderLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sales_order_id", nullable = false)
    private SalesOrder salesOrder;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal unitPrice;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal lineAmount;

    public static SalesOrderLine create(SalesOrder so, Item item, int quantity, BigDecimal unitPrice) {
        SalesOrderLine l = new SalesOrderLine();
        l.salesOrder = so;
        l.item = item;
        l.quantity = quantity;
        l.unitPrice = unitPrice;
        l.lineAmount = unitPrice.multiply(BigDecimal.valueOf(quantity));
        return l;
    }
}

// 학습 질문:
// Q1. CONFIRMED 가 아닌 상태에서 ship() 호출 시 어떤 예외?
//     A:
// Q2. 작성자와 처리자가 같을 수 있는가? 도메인 정책으로 적어 보세요.
//     A:
