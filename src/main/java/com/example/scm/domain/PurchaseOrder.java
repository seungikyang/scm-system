package com.example.scm.domain;

import com.example.scm.common.entity.BaseTimeEntity;
import com.example.scm.common.exception.BusinessException;
import com.example.scm.common.exception.ErrorCode;
import com.example.scm.domain.enums.PurchaseOrderStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 발주(구매) 헤더. (02_architect_datamodel §2.1)
 * 외부 마스터(Partner/User/Item)는 ID(Long)로만 참조. 모듈 내부 라인은 @OneToMany 연관.
 * 상태 전이는 도메인 메서드(submit/approve/reject/receive/cancel)로 캡슐화한다. (datamodel §5.4)
 */
@Entity
@Getter
@Table(name = "purchase_orders",
        uniqueConstraints = @UniqueConstraint(name = "uk_po_order_number", columnNames = "order_number"),
        indexes = {
                @Index(name = "idx_po_writer", columnList = "writer_id"),
                @Index(name = "idx_po_status", columnList = "status"),
                @Index(name = "idx_po_partner", columnList = "partner_id"),
                @Index(name = "idx_po_created_at", columnList = "created_at")
        })
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PurchaseOrder extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_number", nullable = false, unique = true, length = 30)
    private String orderNumber;

    @Column(name = "partner_id", nullable = false)
    private Long partnerId;

    @Column(name = "writer_id", nullable = false)
    private Long writerId;

    @Column(name = "approver_id")
    private Long approverId;                         // nullable — 승인/반려 시 기록 (OQ-11)

    @Column(name = "order_date", nullable = false)
    private LocalDate orderDate;

    @Column(name = "due_date")
    private LocalDate dueDate;                        // nullable (AC-009)

    @Column(name = "total_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalAmount;                   // 서버 재계산 (OQ-1)

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private PurchaseOrderStatus status = PurchaseOrderStatus.DRAFT;

    @Column(name = "reject_reason", length = 500)
    private String rejectReason;                      // nullable

    @Version
    @Column(name = "version", nullable = false)
    private Long version;                             // 낙관적 락 (OQ-5)

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "received_at")
    private LocalDateTime receivedAt;

    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseOrderLine> lines = new ArrayList<>();

    @Builder
    public PurchaseOrder(String orderNumber, Long partnerId, Long writerId,
                         LocalDate orderDate, LocalDate dueDate, BigDecimal totalAmount) {
        this.orderNumber = orderNumber;
        this.partnerId = partnerId;
        this.writerId = writerId;
        this.orderDate = orderDate;
        this.dueDate = dueDate;
        this.totalAmount = totalAmount;
        this.status = PurchaseOrderStatus.DRAFT;
    }

    /** 연관 편의 메서드(양방향 동기화) — 헤더-라인 cascade 동시 저장 보장. */
    public void addLine(PurchaseOrderLine line) {
        this.lines.add(line);
        line.setPurchaseOrder(this);
    }

    // ===== 상태 전이 도메인 메서드 (datamodel §5.4) — 잘못된 전이는 INVALID_STATUS =====

    /** T2: DRAFT → REQUESTED. (작성자 본인 확인은 Service에서) */
    public void submit() {
        requireStatus(PurchaseOrderStatus.DRAFT);
        this.status = PurchaseOrderStatus.REQUESTED;
    }

    /** T3: REQUESTED → APPROVED. approverId/approvedAt 기록. */
    public void approve(Long approverId) {
        requireStatus(PurchaseOrderStatus.REQUESTED);
        this.status = PurchaseOrderStatus.APPROVED;
        this.approverId = approverId;
        this.approvedAt = LocalDateTime.now();
    }

    /** T4: REQUESTED → REJECTED. rejectReason/approverId 기록 (OQ-11). */
    public void reject(Long approverId, String reason) {
        requireStatus(PurchaseOrderStatus.REQUESTED);
        this.status = PurchaseOrderStatus.REJECTED;
        this.approverId = approverId;
        this.rejectReason = reason;
    }

    /** T5: APPROVED → RECEIVED. receivedAt 기록. (라인별 재고 증가는 Service에서 동일 트랜잭션) */
    public void receive() {
        requireStatus(PurchaseOrderStatus.APPROVED);
        this.status = PurchaseOrderStatus.RECEIVED;
        this.receivedAt = LocalDateTime.now();
    }

    /** T6~T8: {DRAFT, REQUESTED, APPROVED} → CANCELED (OQ-3). RECEIVED/종료상태는 불가. */
    public void cancel() {
        requireCancelable();
        this.status = PurchaseOrderStatus.CANCELED;
    }

    public boolean isWriter(Long userId) {
        return this.writerId != null && this.writerId.equals(userId);
    }

    // ===== 내부 가드 =====

    private void requireStatus(PurchaseOrderStatus expected) {
        if (this.status != expected) {
            throw new BusinessException(ErrorCode.INVALID_STATUS);
        }
    }

    private void requireCancelable() {
        if (this.status != PurchaseOrderStatus.DRAFT
                && this.status != PurchaseOrderStatus.REQUESTED
                && this.status != PurchaseOrderStatus.APPROVED) {
            throw new BusinessException(ErrorCode.INVALID_STATUS);
        }
    }
}
