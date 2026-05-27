// 실제 구현 위치 예:
//   src/main/java/com/example/scm/domain/PurchaseOrder.java
//   src/main/java/com/example/scm/domain/PurchaseOrderLine.java
// 목표: 발주서(헤더) + 발주서 라인 + PurchaseOrderStatus enum 을 채우세요.
//       TRD 3.3.5, 3.3.6, 3.6.4 참고.

// ===== PurchaseOrderStatus enum =====
public enum PurchaseOrderStatus {
    // TODO 01: 발주 상태 전이 그래프를 보고 6가지 상태를 채우세요.
    //          DRAFT → REQUESTED → APPROVED → RECEIVED
    //                          └→ REJECTED
    //                          DRAFT/REQUESTED → CANCELED
    ____, ____, ____, ____, ____, ____
}

// ===== PurchaseOrder entity =====
@Entity
@Table(name = "purchase_orders",
       uniqueConstraints = @UniqueConstraint(name = "uk_po_order_number", columnNames = "____"))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 발주번호 (예: PO-20260526-0001)
    @Column(nullable = false, length = 30, unique = true)
    private String orderNumber;

    // TODO 02: 거래처(공급사) 와의 관계는?
    @____(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "partner_id", nullable = false)
    private Partner partner;

    @Column(name = "writer_id", nullable = false)
    private Long writerId;

    @Column(name = "approver_id")
    private Long approverId;

    @Column(nullable = false)
    private LocalDate orderDate;

    private LocalDate dueDate;

    // TODO 03: 총금액 타입은?
    @Column(nullable = false, precision = 15, scale = 2)
    private ____ totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private PurchaseOrderStatus status;

    @Column(length = 500)
    private String rejectReason;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private LocalDateTime approvedAt;
    private LocalDateTime receivedAt;

    // ===== 라인 컬렉션 =====
    // TODO 04: 헤더 저장만으로 라인까지 함께 저장/삭제되도록 하는 옵션 두 가지는?
    @OneToMany(mappedBy = "purchaseOrder",
               cascade = CascadeType.____,
               orphanRemoval = ____)
    private List<PurchaseOrderLine> lines = new ArrayList<>();

    // ===== 정적 팩토리 =====
    public static PurchaseOrder create(String orderNumber,
                                       Partner partner,
                                       Long writerId,
                                       LocalDate orderDate,
                                       LocalDate dueDate) {
        PurchaseOrder po = new PurchaseOrder();
        po.orderNumber = orderNumber;
        po.partner = partner;
        po.writerId = writerId;
        po.orderDate = orderDate;
        po.dueDate = dueDate;
        po.totalAmount = BigDecimal.ZERO;
        // TODO 05: 신규 발주서의 초기 상태는?
        po.status = PurchaseOrderStatus.____;
        return po;
    }

    // ===== 도메인 메서드 =====
    public void addLine(Item item, int quantity, BigDecimal unitPrice) {
        if (quantity <= 0) throw new IllegalArgumentException("수량은 1 이상이어야 합니다.");
        PurchaseOrderLine line = PurchaseOrderLine.create(this, item, quantity, unitPrice);
        this.lines.add(line);
        recalculateTotal();
    }

    public void recalculateTotal() {
        // TODO 06: 라인 합계를 BigDecimal 로 모으는 reduce 식을 채우세요.
        this.totalAmount = this.lines.stream()
            .map(PurchaseOrderLine::getLineAmount)
            .reduce(____, BigDecimal::add);
    }

    public void submit() {
        // TODO 07: DRAFT 가 아니면 어떤 예외?
        if (this.status != PurchaseOrderStatus.____) {
            throw new IllegalStateException("DRAFT 상태에서만 요청할 수 있습니다.");
        }
        if (this.lines.isEmpty()) {
            throw new IllegalStateException("____");
        }
        this.status = PurchaseOrderStatus.REQUESTED;
    }

    public void approve(Long approverUserId) {
        if (this.status != PurchaseOrderStatus.REQUESTED) {
            throw new IllegalStateException("REQUESTED 상태에서만 승인할 수 있습니다.");
        }
        this.approverId = approverUserId;
        this.status = PurchaseOrderStatus.APPROVED;
        // TODO 08: 승인 시각을 기록하세요.
        this.approvedAt = ____;
    }

    public void reject(Long approverUserId, String reason) {
        if (this.status != PurchaseOrderStatus.REQUESTED) {
            throw new IllegalStateException("REQUESTED 상태에서만 반려할 수 있습니다.");
        }
        // TODO 09: 반려 사유 blank 검증.
        if (reason == null || reason.____) {
            throw new IllegalArgumentException("반려 사유는 필수입니다.");
        }
        this.approverId = approverUserId;
        this.rejectReason = reason.trim();
        this.status = PurchaseOrderStatus.REJECTED;
    }

    public void receive() {
        // TODO 10: APPROVED 가 아니면 막는다.
        if (this.status != PurchaseOrderStatus.____) {
            throw new IllegalStateException("APPROVED 상태에서만 입고할 수 있습니다.");
        }
        this.status = PurchaseOrderStatus.RECEIVED;
        this.receivedAt = LocalDateTime.now();
    }

    public void cancelByOwner(Long currentUserId) {
        if (!this.writerId.equals(currentUserId)) {
            throw new AccessDeniedException("본인 발주서만 취소할 수 있습니다.");
        }
        // TODO 11: 취소 가능한 상태 두 가지?
        if (this.status != PurchaseOrderStatus.____ && this.status != PurchaseOrderStatus.____) {
            throw new IllegalStateException("이 상태에서는 취소할 수 없습니다.");
        }
        this.status = PurchaseOrderStatus.CANCELED;
    }
}

// ===== PurchaseOrderLine entity =====
@Entity
@Table(name = "purchase_order_lines")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PurchaseOrderLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "purchase_order_id", nullable = false)
    private PurchaseOrder purchaseOrder;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal unitPrice;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal lineAmount;

    public static PurchaseOrderLine create(PurchaseOrder po, Item item, int quantity, BigDecimal unitPrice) {
        PurchaseOrderLine line = new PurchaseOrderLine();
        line.purchaseOrder = po;
        line.item = item;
        line.quantity = quantity;
        line.unitPrice = unitPrice;
        // TODO 12: 라인 금액 = 수량 × 단가 계산식을 채우세요.
        line.lineAmount = unitPrice.multiply(BigDecimal.valueOf(____));
        return line;
    }
}

// 학습 질문:
// Q1. cascade = ALL + orphanRemoval = true 의 효과와 위험은?
//     A:
// Q2. 헤더 totalAmount 와 라인 합계가 어긋날 가능성을 어디서 막는가?
//     A:
// Q3. 발주번호(orderNumber) 채번의 동시성 문제는?
//     A:
