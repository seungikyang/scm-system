package com.example.scm.domain;

import com.example.scm.common.entity.BaseTimeEntity;
import com.example.scm.common.exception.BusinessException;
import com.example.scm.common.exception.ErrorCode;
import com.example.scm.domain.enums.SalesOrderStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 고객에게 팔 물건을 적은 수주서 한 장입니다.
 * 확정 전에 작성하고, 물건을 창고에서 보내면 출고 상태가 됩니다. 정해진 순서대로만 상태를 바꿉니다.
 */
@Entity
@Getter
@Table(name = "sales_orders", uniqueConstraints = @UniqueConstraint(name = "uk_so_order_number", columnNames = "order_number"),
       indexes = {@Index(name = "idx_so_writer", columnList = "writer_id"), @Index(name = "idx_so_status", columnList = "status")})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SalesOrder extends BaseTimeEntity {
    // @로 시작하는 표시는 도구가 읽는 설정입니다. @Id는 이 자료를 구별하는 번호 칸을 뜻합니다.
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "order_number", nullable = false, length = 30)
    private String orderNumber;
    @Column(name = "partner_id", nullable = false)
    private Long partnerId;
    @Column(name = "writer_id", nullable = false)
    private Long writerId;
    @Column(name = "manager_id")
    private Long managerId;
    @Column(name = "order_date", nullable = false)
    private LocalDate orderDate;
    @Column(name = "ship_date")
    private LocalDate shipDate;
    @Column(name = "total_amount", nullable = false, precision = 15, scale = 2)
    // 예: 2개 × 1,500원 + 3개 × 500원 = 4,500원. 계산한 각 줄의 금액을 더해 총액을 만듭니다.
    private BigDecimal totalAmount = BigDecimal.ZERO;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 30)
    private SalesOrderStatus status = SalesOrderStatus.DRAFT;
    @Column(name = "cancel_reason", length = 500)
    private String cancelReason;
    // version은 수정 횟수에 해당하는 표식입니다. 동시에 고친 오래된 값이 새 값을 덮어쓰지 않게 합니다.
    @Version @Column(nullable = false)
    private Long version;
    private LocalDateTime confirmedAt;
    private LocalDateTime shippedAt;
    private LocalDateTime completedAt;
    // 수주서 한 장에는 여러 주문 줄이 들어갑니다. cascade는 수주서와 그 줄들을 함께 저장하게 합니다.
    @OneToMany(mappedBy = "salesOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SalesOrderLine> lines = new ArrayList<>();

    public SalesOrder(String orderNumber, Long partnerId, Long writerId, LocalDate orderDate, LocalDate shipDate) {
        this.orderNumber = orderNumber; this.partnerId = partnerId; this.writerId = writerId;
        this.orderDate = orderDate; this.shipDate = shipDate;
    }
    public void addLine(SalesOrderLine line) {
        requireStatus(SalesOrderStatus.DRAFT);
        line.setSalesOrder(this); lines.add(line);
        // 새 물건 줄의 금액을 전체 주문 금액에 더합니다.
        totalAmount = totalAmount.add(line.getLineAmount());
    }
    public void confirm() {
        // 확정은 초안을 주문으로 정하는 단계입니다. 물건이 한 줄도 없으면 확정할 수 없습니다.
        requireStatus(SalesOrderStatus.DRAFT);
        if (lines.isEmpty()) throw new BusinessException(ErrorCode.EMPTY_ORDER_LINES);
        status = SalesOrderStatus.CONFIRMED; confirmedAt = LocalDateTime.now();
    }
    public void ship(Long managerId) {
        // 이 함수는 주문 상태만 바꿉니다. 실제 재고 빼기는 SalesOrderService가 같은 저장 묶음에서 합니다.
        requireStatus(SalesOrderStatus.CONFIRMED);
        this.managerId = managerId; status = SalesOrderStatus.SHIPPED; shippedAt = LocalDateTime.now();
    }
    public void complete(Long managerId) {
        // 먼저 출고한 주문만 완료할 수 있습니다. 단계 검사가 끝나야 처리자와 시간을 기록합니다.
        requireStatus(SalesOrderStatus.SHIPPED);
        this.managerId = managerId; status = SalesOrderStatus.COMPLETED; completedAt = LocalDateTime.now();
    }
    public void cancel(String reason) {
        // 이미 끝난 주문은 다시 취소하지 않습니다. 언제나 이유가 필요하고 앞뒤 공백은 제외합니다.
        if (status == SalesOrderStatus.COMPLETED || status == SalesOrderStatus.CANCELED)
            throw new BusinessException(ErrorCode.INVALID_STATUS);
        if (reason == null || reason.isBlank() || reason.trim().length() > 500)
            throw new BusinessException(ErrorCode.INVALID_INPUT, "취소 사유는 1~500자로 입력하세요.");
        cancelReason = reason.trim(); status = SalesOrderStatus.CANCELED;
    }
    // 진행 단계가 맞는지 확인하는 공통 검사입니다. 예: 확정 전 주문을 바로 출고하면 막습니다.
    private void requireStatus(SalesOrderStatus expected) {
        if (status != expected) throw new BusinessException(ErrorCode.INVALID_STATUS);
    }
}
