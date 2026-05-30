package com.example.scm.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.example.scm.common.auth.LoginUser;
import com.example.scm.common.exception.BusinessException;
import com.example.scm.common.exception.ErrorCode;
import com.example.scm.domain.Item;
import com.example.scm.domain.Partner;
import com.example.scm.domain.PurchaseOrder;
import com.example.scm.domain.PurchaseOrderLine;
import com.example.scm.domain.Stock;
import com.example.scm.domain.enums.ItemStatus;
import com.example.scm.domain.enums.PartnerStatus;
import com.example.scm.domain.enums.PartnerType;
import com.example.scm.domain.enums.PurchaseOrderStatus;
import com.example.scm.domain.enums.UserRole;
import com.example.scm.dto.purchaseorder.PurchaseOrderCreateRequest;
import com.example.scm.dto.purchaseorder.PurchaseOrderCreateResponse;
import com.example.scm.dto.purchaseorder.PurchaseOrderStatusResponse;
import com.example.scm.repository.ItemRepository;
import com.example.scm.repository.PartnerRepository;
import com.example.scm.repository.PurchaseOrderRepository;
import com.example.scm.repository.StockRepository;
import com.example.scm.repository.UserRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * 발주(PurchaseOrder) 서비스 단위 테스트 — 상태머신(T1~T8) / 작성 검증 / 입고-재고 / 권한.
 * 진실원: 01_analyst_requirements §3·§4·§5(AC), 02_architect_contracts §1·§4·§5, datamodel §5.
 * 7.1 확정(OQ-1 서버계산, OQ-3 APPROVED 취소, OQ-4 입고 재고증가, OQ-6 ADMIN+MANAGER, OQ-12 INACTIVE→INVALID_STATUS, OQ-13 단가기본값) 반영.
 *
 * NOTE(환경): 검증 머신엔 JDK8 + gradle-wrapper.jar 미포함 → 본 테스트는 작성/컴파일 검증 대상이며
 *             실행은 JDK17 환경에서 `./gradlew test` 로 수행 권장.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("PurchaseOrderService 단위 테스트")
class PurchaseOrderServiceTest {

    @Mock
    private PurchaseOrderRepository purchaseOrderRepository;
    @Mock
    private StockRepository stockRepository;
    @Mock
    private PartnerRepository partnerRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private OrderNumberGenerator orderNumberGenerator;

    @InjectMocks
    private PurchaseOrderService service;

    private LoginUser writer;       // 작성자(USER), id=10
    private LoginUser otherUser;    // 타인(USER), id=11
    private LoginUser admin;        // ADMIN, id=1
    private LoginUser manager;      // MANAGER, id=2

    @BeforeEach
    void setUp() {
        writer = new LoginUser(10L, "작성자", "writer@scm.com", UserRole.USER);
        otherUser = new LoginUser(11L, "타인", "other@scm.com", UserRole.USER);
        admin = new LoginUser(1L, "관리자", "admin@scm.com", UserRole.ADMIN);
        manager = new LoginUser(2L, "매니저", "manager@scm.com", UserRole.MANAGER);
    }

    // ============================================================
    // 작성 (T1, REQ-PO-001, AC-001~011)
    // ============================================================

    @Nested
    @DisplayName("발주서 작성")
    class Create {

        @Test
        @DisplayName("AC-001 정상: status=DRAFT, orderNumber 발급, totalAmount=Σ라인금액(서버계산)")
        void create_success() {
            Partner supplier = partner(1L, PartnerType.SUPPLIER, PartnerStatus.ACTIVE);
            Item item10 = item(10L, ItemStatus.ACTIVE, new BigDecimal("5000"));
            Item item11 = item(11L, ItemStatus.ACTIVE, new BigDecimal("12000"));
            given(partnerRepository.findById(1L)).willReturn(Optional.of(supplier));
            given(itemRepository.findById(10L)).willReturn(Optional.of(item10));
            given(itemRepository.findById(11L)).willReturn(Optional.of(item11));
            given(orderNumberGenerator.generate(any(LocalDate.class))).willReturn("PO-20260601-0001");
            given(purchaseOrderRepository.saveAndFlush(any(PurchaseOrder.class)))
                    .willAnswer(inv -> {
                        PurchaseOrder po = inv.getArgument(0);
                        ReflectionTestUtils.setField(po, "id", 100L);
                        return po;
                    });

            PurchaseOrderCreateRequest req = createRequest(1L, LocalDate.parse("2026-06-01"),
                    LocalDate.parse("2026-06-15"),
                    line(10L, 100, new BigDecimal("5000")),   // 500,000
                    line(11L, 50, new BigDecimal("12000")));  // 600,000

            PurchaseOrderCreateResponse res = service.create(req, writer);

            assertThat(res.getStatus()).isEqualTo(PurchaseOrderStatus.DRAFT);
            assertThat(res.getOrderNumber()).isEqualTo("PO-20260601-0001");
            // OQ-1: totalAmount 서버 계산 = 500,000 + 600,000
            assertThat(res.getTotalAmount()).isEqualByComparingTo("1100000");
        }

        @Test
        @DisplayName("AC-002 빈 라인 → EMPTY_ORDER_LINES")
        void create_emptyLines() {
            PurchaseOrderCreateRequest req = createRequest(1L, LocalDate.now(), null);
            assertCode(() -> service.create(req, writer), ErrorCode.EMPTY_ORDER_LINES);
            verify(purchaseOrderRepository, never()).saveAndFlush(any());
        }

        @Test
        @DisplayName("AC-003 CUSTOMER 거래처 → PARTNER_TYPE_MISMATCH")
        void create_customerPartner() {
            given(partnerRepository.findById(1L))
                    .willReturn(Optional.of(partner(1L, PartnerType.CUSTOMER, PartnerStatus.ACTIVE)));
            PurchaseOrderCreateRequest req = createRequest(1L, LocalDate.now(), null,
                    line(10L, 1, new BigDecimal("100")));
            assertCode(() -> service.create(req, writer), ErrorCode.PARTNER_TYPE_MISMATCH);
        }

        @Test
        @DisplayName("AC-004 BOTH 거래처 → 정상 허용")
        void create_bothPartner() {
            given(partnerRepository.findById(1L))
                    .willReturn(Optional.of(partner(1L, PartnerType.BOTH, PartnerStatus.ACTIVE)));
            given(itemRepository.findById(10L))
                    .willReturn(Optional.of(item(10L, ItemStatus.ACTIVE, new BigDecimal("100"))));
            given(orderNumberGenerator.generate(any(LocalDate.class))).willReturn("PO-20260601-0001");
            given(purchaseOrderRepository.saveAndFlush(any(PurchaseOrder.class)))
                    .willAnswer(inv -> inv.getArgument(0));

            PurchaseOrderCreateRequest req = createRequest(1L, LocalDate.now(), null,
                    line(10L, 2, new BigDecimal("100")));
            PurchaseOrderCreateResponse res = service.create(req, writer);
            assertThat(res.getStatus()).isEqualTo(PurchaseOrderStatus.DRAFT);
        }

        @Test
        @DisplayName("AC-005 INACTIVE 거래처 → INVALID_STATUS (OQ-12)")
        void create_inactivePartner() {
            given(partnerRepository.findById(1L))
                    .willReturn(Optional.of(partner(1L, PartnerType.SUPPLIER, PartnerStatus.INACTIVE)));
            PurchaseOrderCreateRequest req = createRequest(1L, LocalDate.now(), null,
                    line(10L, 1, new BigDecimal("100")));
            assertCode(() -> service.create(req, writer), ErrorCode.INVALID_STATUS);
        }

        @Test
        @DisplayName("AC-006 단종 품목 → ITEM_DISCONTINUED")
        void create_discontinuedItem() {
            given(partnerRepository.findById(1L))
                    .willReturn(Optional.of(partner(1L, PartnerType.SUPPLIER, PartnerStatus.ACTIVE)));
            given(itemRepository.findById(10L))
                    .willReturn(Optional.of(item(10L, ItemStatus.DISCONTINUED, new BigDecimal("100"))));
            PurchaseOrderCreateRequest req = createRequest(1L, LocalDate.now(), null,
                    line(10L, 1, new BigDecimal("100")));
            assertCode(() -> service.create(req, writer), ErrorCode.ITEM_DISCONTINUED);
        }

        @Test
        @DisplayName("AC-007 미존재 품목 → ITEM_NOT_FOUND")
        void create_itemNotFound() {
            given(partnerRepository.findById(1L))
                    .willReturn(Optional.of(partner(1L, PartnerType.SUPPLIER, PartnerStatus.ACTIVE)));
            given(itemRepository.findById(99L)).willReturn(Optional.empty());
            PurchaseOrderCreateRequest req = createRequest(1L, LocalDate.now(), null,
                    line(99L, 1, new BigDecimal("100")));
            assertCode(() -> service.create(req, writer), ErrorCode.ITEM_NOT_FOUND);
        }

        @Test
        @DisplayName("AC-008 발주일>납기일 → INVALID_DATE_RANGE")
        void create_invalidDateRange() {
            given(partnerRepository.findById(1L))
                    .willReturn(Optional.of(partner(1L, PartnerType.SUPPLIER, PartnerStatus.ACTIVE)));
            PurchaseOrderCreateRequest req = createRequest(1L,
                    LocalDate.parse("2026-06-15"), LocalDate.parse("2026-06-01"),
                    line(10L, 1, new BigDecimal("100")));
            assertCode(() -> service.create(req, writer), ErrorCode.INVALID_DATE_RANGE);
        }

        @Test
        @DisplayName("AC-004 미존재 거래처 → PARTNER_NOT_FOUND")
        void create_partnerNotFound() {
            given(partnerRepository.findById(1L)).willReturn(Optional.empty());
            PurchaseOrderCreateRequest req = createRequest(1L, LocalDate.now(), null,
                    line(10L, 1, new BigDecimal("100")));
            assertCode(() -> service.create(req, writer), ErrorCode.PARTNER_NOT_FOUND);
        }

        @Test
        @DisplayName("OQ-13 단가 누락 → 품목 표준단가 적용 + 서버 라인금액 계산")
        void create_unitPriceFallback() {
            given(partnerRepository.findById(1L))
                    .willReturn(Optional.of(partner(1L, PartnerType.SUPPLIER, PartnerStatus.ACTIVE)));
            given(itemRepository.findById(10L))
                    .willReturn(Optional.of(item(10L, ItemStatus.ACTIVE, new BigDecimal("7000"))));
            given(orderNumberGenerator.generate(any(LocalDate.class))).willReturn("PO-20260601-0001");
            given(purchaseOrderRepository.saveAndFlush(any(PurchaseOrder.class)))
                    .willAnswer(inv -> inv.getArgument(0));

            PurchaseOrderCreateRequest req = createRequest(1L, LocalDate.now(), null,
                    line(10L, 3, null)); // unitPrice 누락 → 7000 적용
            PurchaseOrderCreateResponse res = service.create(req, writer);
            assertThat(res.getTotalAmount()).isEqualByComparingTo("21000"); // 3 × 7000
        }

        @Test
        @DisplayName("수량 0 이하 → INVALID_INPUT (BR-10)")
        void create_nonPositiveQuantity() {
            given(partnerRepository.findById(1L))
                    .willReturn(Optional.of(partner(1L, PartnerType.SUPPLIER, PartnerStatus.ACTIVE)));
            PurchaseOrderCreateRequest req = createRequest(1L, LocalDate.now(), null,
                    line(10L, 0, new BigDecimal("100")));
            assertCode(() -> service.create(req, writer), ErrorCode.INVALID_INPUT);
        }

        @Test
        @DisplayName("AC-011 비로그인 작성 → AUTHENTICATION_REQUIRED")
        void create_notLoggedIn() {
            PurchaseOrderCreateRequest req = createRequest(1L, LocalDate.now(), null,
                    line(10L, 1, new BigDecimal("100")));
            assertCode(() -> service.create(req, null), ErrorCode.AUTHENTICATION_REQUIRED);
        }
    }

    // ============================================================
    // submit (T2, REQ-PO-002, AC-012~014)
    // ============================================================

    @Nested
    @DisplayName("발주 요청(submit)")
    class Submit {

        @Test
        @DisplayName("AC-012 정상: DRAFT→REQUESTED (작성자 본인)")
        void submit_success() {
            PurchaseOrder po = po(100L, writer.id(), PurchaseOrderStatus.DRAFT);
            given(purchaseOrderRepository.findById(100L)).willReturn(Optional.of(po));
            PurchaseOrderStatusResponse res = service.submit(100L, writer);
            assertThat(res.getStatus()).isEqualTo(PurchaseOrderStatus.REQUESTED);
        }

        @Test
        @DisplayName("AC-013 DRAFT 아님(REQUESTED) → INVALID_STATUS")
        void submit_invalidStatus() {
            PurchaseOrder po = po(100L, writer.id(), PurchaseOrderStatus.REQUESTED);
            given(purchaseOrderRepository.findById(100L)).willReturn(Optional.of(po));
            assertCode(() -> service.submit(100L, writer), ErrorCode.INVALID_STATUS);
        }

        @Test
        @DisplayName("AC-014 타인 발주 submit → ACCESS_DENIED")
        void submit_notWriter() {
            PurchaseOrder po = po(100L, writer.id(), PurchaseOrderStatus.DRAFT);
            given(purchaseOrderRepository.findById(100L)).willReturn(Optional.of(po));
            assertCode(() -> service.submit(100L, otherUser), ErrorCode.ACCESS_DENIED);
        }

        @Test
        @DisplayName("미존재 발주 submit → PURCHASE_ORDER_NOT_FOUND")
        void submit_notFound() {
            given(purchaseOrderRepository.findById(404L)).willReturn(Optional.empty());
            assertCode(() -> service.submit(404L, writer), ErrorCode.PURCHASE_ORDER_NOT_FOUND);
        }
    }

    // ============================================================
    // approve / reject (T3/T4, REQ-PO-006/007, AC-015~021)
    // ============================================================

    @Nested
    @DisplayName("승인/반려(approve/reject)")
    class ApproveReject {

        @Test
        @DisplayName("AC-015 정상: REQUESTED→APPROVED, approverId/approvedAt 기록 (ADMIN)")
        void approve_success() {
            PurchaseOrder po = po(100L, writer.id(), PurchaseOrderStatus.REQUESTED);
            given(purchaseOrderRepository.findById(100L)).willReturn(Optional.of(po));
            PurchaseOrderStatusResponse res = service.approve(100L, admin);
            assertThat(res.getStatus()).isEqualTo(PurchaseOrderStatus.APPROVED);
            assertThat(po.getApproverId()).isEqualTo(admin.id());
            assertThat(po.getApprovedAt()).isNotNull();
        }

        @Test
        @DisplayName("OQ-6 MANAGER 도 승인 가능: REQUESTED→APPROVED")
        void approve_byManager() {
            PurchaseOrder po = po(100L, writer.id(), PurchaseOrderStatus.REQUESTED);
            given(purchaseOrderRepository.findById(100L)).willReturn(Optional.of(po));
            PurchaseOrderStatusResponse res = service.approve(100L, manager);
            assertThat(res.getStatus()).isEqualTo(PurchaseOrderStatus.APPROVED);
            assertThat(po.getApproverId()).isEqualTo(manager.id());
        }

        @Test
        @DisplayName("AC-016 비REQUESTED(DRAFT) 승인 → INVALID_STATUS")
        void approve_invalidStatus() {
            PurchaseOrder po = po(100L, writer.id(), PurchaseOrderStatus.DRAFT);
            given(purchaseOrderRepository.findById(100L)).willReturn(Optional.of(po));
            assertCode(() -> service.approve(100L, admin), ErrorCode.INVALID_STATUS);
        }

        @Test
        @DisplayName("AC-017 USER 승인 → ACCESS_DENIED")
        void approve_byUser() {
            // 권한 검사가 PO 조회보다 먼저 → repository 스텁 불필요
            assertCode(() -> service.approve(100L, writer), ErrorCode.ACCESS_DENIED);
            verify(purchaseOrderRepository, never()).findById(anyLong());
        }

        @Test
        @DisplayName("AC-019 정상 반려: REQUESTED→REJECTED, rejectReason/approverId 기록 (OQ-11)")
        void reject_success() {
            PurchaseOrder po = po(100L, writer.id(), PurchaseOrderStatus.REQUESTED);
            given(purchaseOrderRepository.findById(100L)).willReturn(Optional.of(po));
            PurchaseOrderStatusResponse res = service.reject(100L, "예산 초과", admin);
            assertThat(res.getStatus()).isEqualTo(PurchaseOrderStatus.REJECTED);
            assertThat(po.getRejectReason()).isEqualTo("예산 초과");
            assertThat(po.getApproverId()).isEqualTo(admin.id());
        }

        @Test
        @DisplayName("AC-020 반려 사유 공란 → INVALID_INPUT")
        void reject_blankReason() {
            assertCode(() -> service.reject(100L, "  ", admin), ErrorCode.INVALID_INPUT);
            // 사유 검증이 PO 조회보다 먼저 → 조회 안 함
            verify(purchaseOrderRepository, never()).findById(anyLong());
        }

        @Test
        @DisplayName("AC-021 APPROVED 발주 반려 → INVALID_STATUS")
        void reject_approvedOrder() {
            PurchaseOrder po = po(100L, writer.id(), PurchaseOrderStatus.APPROVED);
            given(purchaseOrderRepository.findById(100L)).willReturn(Optional.of(po));
            assertCode(() -> service.reject(100L, "사유", admin), ErrorCode.INVALID_STATUS);
        }
    }

    // ============================================================
    // receive + 재고 (T5, REQ-PO-008, OQ-4, AC-022~024)
    // ============================================================

    @Nested
    @DisplayName("입고(receive) + 재고 증가")
    class Receive {

        @Test
        @DisplayName("AC-022 정상: APPROVED→RECEIVED + 라인별 재고 증가(없으면 생성)")
        void receive_success_createsStock() {
            PurchaseOrder po = po(100L, writer.id(), PurchaseOrderStatus.APPROVED);
            po.addLine(PurchaseOrderLine.builder().itemId(10L).quantity(30).unitPrice(new BigDecimal("100")).build());
            po.addLine(PurchaseOrderLine.builder().itemId(11L).quantity(20).unitPrice(new BigDecimal("200")).build());
            given(purchaseOrderRepository.findByIdWithLines(100L)).willReturn(Optional.of(po));
            // 10번 품목 재고 없음 → 생성, 11번 품목 재고 있음(5) → 증가
            given(stockRepository.findByItemId(10L)).willReturn(Optional.empty());
            Stock existing = new Stock(11L, 5);
            given(stockRepository.findByItemId(11L)).willReturn(Optional.of(existing));
            given(stockRepository.save(any(Stock.class))).willAnswer(inv -> inv.getArgument(0));

            PurchaseOrderStatusResponse res = service.receive(100L, admin);

            assertThat(res.getStatus()).isEqualTo(PurchaseOrderStatus.RECEIVED);
            assertThat(po.getReceivedAt()).isNotNull();
            // 없던 10번 품목 → 생성 후 +30
            verify(stockRepository).save(any(Stock.class));
            // 기존 11번 품목 → 5 + 20 = 25 (동일 트랜잭션, dirty checking)
            assertThat(existing.getQuantity()).isEqualTo(25);
        }

        @Test
        @DisplayName("AC-023 비APPROVED(REQUESTED) 입고 → INVALID_STATUS, 재고 미변경")
        void receive_invalidStatus() {
            PurchaseOrder po = po(100L, writer.id(), PurchaseOrderStatus.REQUESTED);
            given(purchaseOrderRepository.findByIdWithLines(100L)).willReturn(Optional.of(po));
            assertCode(() -> service.receive(100L, admin), ErrorCode.INVALID_STATUS);
            verify(stockRepository, never()).findByItemId(anyLong());
            verify(stockRepository, never()).save(any());
        }

        @Test
        @DisplayName("재입고 차단: RECEIVED 상태에서 receive → INVALID_STATUS")
        void receive_alreadyReceived() {
            PurchaseOrder po = po(100L, writer.id(), PurchaseOrderStatus.RECEIVED);
            given(purchaseOrderRepository.findByIdWithLines(100L)).willReturn(Optional.of(po));
            assertCode(() -> service.receive(100L, admin), ErrorCode.INVALID_STATUS);
            verify(stockRepository, never()).save(any());
        }

        @Test
        @DisplayName("AC-024 USER 입고 → ACCESS_DENIED")
        void receive_byUser() {
            assertCode(() -> service.receive(100L, writer), ErrorCode.ACCESS_DENIED);
            verify(purchaseOrderRepository, never()).findByIdWithLines(anyLong());
        }
    }

    // ============================================================
    // cancel (T6~T8, REQ-PO-005, OQ-3, AC-025~028)
    // ============================================================

    @Nested
    @DisplayName("취소(cancel) — OQ-3: DRAFT/REQUESTED/APPROVED 가능, RECEIVED 불가")
    class Cancel {

        @Test
        @DisplayName("AC-025 정상(DRAFT)→CANCELED")
        void cancel_draft() {
            PurchaseOrder po = po(100L, writer.id(), PurchaseOrderStatus.DRAFT);
            given(purchaseOrderRepository.findById(100L)).willReturn(Optional.of(po));
            assertThat(service.cancel(100L, writer).getStatus()).isEqualTo(PurchaseOrderStatus.CANCELED);
        }

        @Test
        @DisplayName("AC-026 정상(REQUESTED)→CANCELED")
        void cancel_requested() {
            PurchaseOrder po = po(100L, writer.id(), PurchaseOrderStatus.REQUESTED);
            given(purchaseOrderRepository.findById(100L)).willReturn(Optional.of(po));
            assertThat(service.cancel(100L, writer).getStatus()).isEqualTo(PurchaseOrderStatus.CANCELED);
        }

        @Test
        @DisplayName("AC-027 변경(OQ-3): APPROVED 취소 = 정상(CANCELED)")
        void cancel_approved_isAllowed() {
            PurchaseOrder po = po(100L, writer.id(), PurchaseOrderStatus.APPROVED);
            given(purchaseOrderRepository.findById(100L)).willReturn(Optional.of(po));
            assertThat(service.cancel(100L, writer).getStatus()).isEqualTo(PurchaseOrderStatus.CANCELED);
        }

        @Test
        @DisplayName("RECEIVED 취소 → INVALID_STATUS (OQ-3 취소 불가 경계)")
        void cancel_received_blocked() {
            PurchaseOrder po = po(100L, writer.id(), PurchaseOrderStatus.RECEIVED);
            given(purchaseOrderRepository.findById(100L)).willReturn(Optional.of(po));
            assertCode(() -> service.cancel(100L, writer), ErrorCode.INVALID_STATUS);
        }

        @Test
        @DisplayName("AC-028 타인 발주 취소 → ACCESS_DENIED")
        void cancel_notWriter() {
            PurchaseOrder po = po(100L, writer.id(), PurchaseOrderStatus.DRAFT);
            given(purchaseOrderRepository.findById(100L)).willReturn(Optional.of(po));
            assertCode(() -> service.cancel(100L, otherUser), ErrorCode.ACCESS_DENIED);
        }
    }

    // ============================================================
    // 상세 접근 권한 (REQ-PO-004, AC-030)
    // ============================================================

    @Nested
    @DisplayName("상세 접근 권한")
    class DetailAccess {

        @Test
        @DisplayName("AC-030 타인+비ADMIN/MANAGER 상세 → ACCESS_DENIED")
        void detail_otherUserDenied() {
            PurchaseOrder po = po(100L, writer.id(), PurchaseOrderStatus.DRAFT);
            given(purchaseOrderRepository.findByIdWithLines(100L)).willReturn(Optional.of(po));
            assertCode(() -> service.getDetail(100L, otherUser), ErrorCode.ACCESS_DENIED);
        }

        @Test
        @DisplayName("OQ-6 MANAGER 는 타인 발주 상세 접근 가능")
        void detail_managerAllowed() {
            PurchaseOrder po = po(100L, writer.id(), PurchaseOrderStatus.DRAFT);
            given(purchaseOrderRepository.findByIdWithLines(100L)).willReturn(Optional.of(po));
            given(partnerRepository.findById(anyLong())).willReturn(Optional.empty());
            given(userRepository.findById(anyLong())).willReturn(Optional.empty());
            given(itemRepository.findAllById(any())).willReturn(List.of());
            // 예외 없이 응답 반환되면 통과
            assertThat(service.getDetail(100L, manager)).isNotNull();
        }
    }

    // ============================================================
    // 헬퍼 / 픽스처
    // ============================================================

    private void assertCode(org.junit.jupiter.api.function.Executable exec, ErrorCode expected) {
        assertThatThrownBy(exec::execute)
                .isInstanceOf(BusinessException.class)
                .extracting(e -> ((BusinessException) e).getErrorCode())
                .isEqualTo(expected);
    }

    private Partner partner(Long id, PartnerType type, PartnerStatus status) {
        Partner p = Partner.builder()
                .name("공급사")
                .businessNumber("000-00-0000" + id)
                .partnerType(type)
                .status(status)
                .build();
        ReflectionTestUtils.setField(p, "id", id);
        return p;
    }

    private Item item(Long id, ItemStatus status, BigDecimal unitPrice) {
        Item it = Item.builder()
                .itemCode("ITM-00" + id)
                .name("품목" + id)
                .categoryId(1L)
                .unit("EA")
                .unitPrice(unitPrice)
                .safetyStock(0)
                .status(status)
                .build();
        ReflectionTestUtils.setField(it, "id", id);
        return it;
    }

    private PurchaseOrder po(Long id, Long writerId, PurchaseOrderStatus status) {
        PurchaseOrder po = PurchaseOrder.builder()
                .orderNumber("PO-20260601-0001")
                .partnerId(1L)
                .writerId(writerId)
                .orderDate(LocalDate.parse("2026-06-01"))
                .dueDate(LocalDate.parse("2026-06-15"))
                .totalAmount(new BigDecimal("1000"))
                .build();
        ReflectionTestUtils.setField(po, "id", id);
        ReflectionTestUtils.setField(po, "status", status);
        return po;
    }

    private PurchaseOrderCreateRequest createRequest(Long partnerId, LocalDate orderDate,
                                                     LocalDate dueDate,
                                                     PurchaseOrderCreateRequest.LineRequest... lines) {
        PurchaseOrderCreateRequest req = new PurchaseOrderCreateRequest();
        req.setPartnerId(partnerId);
        req.setOrderDate(orderDate);
        req.setDueDate(dueDate);
        req.setLines(new ArrayList<>(List.of(lines)));
        return req;
    }

    private PurchaseOrderCreateRequest.LineRequest line(Long itemId, Integer quantity, BigDecimal unitPrice) {
        PurchaseOrderCreateRequest.LineRequest lr = new PurchaseOrderCreateRequest.LineRequest();
        lr.setItemId(itemId);
        lr.setQuantity(quantity);
        lr.setUnitPrice(unitPrice);
        return lr;
    }
}
