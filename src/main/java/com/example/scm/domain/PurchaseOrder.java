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
 * 한 건의 발주에서 공통으로 쓰는 정보를 담는 발주 헤더 엔티티.
 *
 * <p>학습 모듈 04: PurchaseOrderStatus → PurchaseOrderLine → 이 클래스 순서로 읽는다.
 * 여기서는 헤더·라인 구조와 상태 메서드만 보고, 실제 실행 순서는 모듈 10~11의
 * PurchaseOrderService에서 확인한다.</p>
 *
 * <p>헤더에는 발주번호·공급사·작성자·날짜·총액·상태가 있고, 개별 품목은
 * {@link PurchaseOrderLine} 목록에 둔다. 헤더와 라인은 한 덩어리로 저장되지만 Partner,
 * User, Item 같은 외부 기준 정보는 연관 객체 대신 ID만 보관해 모듈 경계를 단순하게 한다.</p>
 *
 * <p>상태 필드를 아무 곳에서나 바꾸지 않고 submit/approve/reject/receive/cancel 메서드를
 * 통해서만 바꾼다. 따라서 잘못된 순서의 상태 변경은 엔티티 스스로 거부할 수 있다.</p>
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
    private Long approverId;                         // 아직 결재 전이면 null, 승인/반려 시 기록

    @Column(name = "order_date", nullable = false)
    private LocalDate orderDate;

    @Column(name = "due_date")
    private LocalDate dueDate;                        // 납기 미정이면 null 가능

    @Column(name = "total_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalAmount;                   // 요청값을 믿지 않고 라인 금액 합계로 계산

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private PurchaseOrderStatus status = PurchaseOrderStatus.DRAFT;

    @Column(name = "reject_reason", length = 500)
    private String rejectReason;                      // nullable

    @Version
    @Column(name = "version", nullable = false)
    private Long version;                             // 동시에 같은 발주를 수정하면 충돌을 감지하는 번호

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

    /** 양쪽 객체를 함께 연결해 헤더 저장 시 라인도 cascade로 저장되게 한다. */
    public void addLine(PurchaseOrderLine line) {
        this.lines.add(line);
        line.setPurchaseOrder(this);
    }

    // ===== 상태 전이 도메인 메서드: 잘못된 순서면 INVALID_STATUS 예외 =====

    /** 임시 저장(DRAFT) 발주를 결재 요청(REQUESTED)으로 바꾼다. 작성자 확인은 Service 책임이다. */
    public void submit() {
        requireStatus(PurchaseOrderStatus.DRAFT);
        this.status = PurchaseOrderStatus.REQUESTED;
    }

    /** 결재 요청을 승인하고 승인자와 승인 시각을 기록한다. */
    public void approve(Long approverId) {
        requireStatus(PurchaseOrderStatus.REQUESTED);
        this.status = PurchaseOrderStatus.APPROVED;
        this.approverId = approverId;
        this.approvedAt = LocalDateTime.now();
    }

    /** 결재 요청을 반려하고 결재자와 반려 사유를 기록한다. */
    public void reject(Long approverId, String reason) {
        requireStatus(PurchaseOrderStatus.REQUESTED);
        this.status = PurchaseOrderStatus.REJECTED;
        this.approverId = approverId;
        this.rejectReason = reason;
    }

    /** 승인된 발주를 입고 완료로 바꾼다. 라인별 재고 증가는 Service가 같은 트랜잭션에서 처리한다. */
    public void receive() {
        requireStatus(PurchaseOrderStatus.APPROVED);
        this.status = PurchaseOrderStatus.RECEIVED;
        this.receivedAt = LocalDateTime.now();
    }

    /** 진행 중인 발주를 취소한다. 이미 입고됐거나 종료된 상태는 취소할 수 없다. */
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
