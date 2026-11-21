package com.example.scm.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.scm.common.exception.BusinessException;
import com.example.scm.common.exception.ErrorCode;
import com.example.scm.domain.Category;
import com.example.scm.domain.Item;
import com.example.scm.domain.Partner;
import com.example.scm.domain.PurchaseOrder;
import com.example.scm.domain.PurchaseOrderLine;
import com.example.scm.domain.SalesOrder;
import com.example.scm.domain.SalesOrderLine;
import com.example.scm.domain.enums.ItemStatus;
import com.example.scm.domain.enums.PartnerStatus;
import com.example.scm.domain.enums.PartnerType;
import com.example.scm.repository.CategoryRepository;
import com.example.scm.repository.ItemRepository;
import com.example.scm.repository.PartnerRepository;
import com.example.scm.repository.PurchaseOrderRepository;
import com.example.scm.repository.SalesOrderRepository;
import com.example.scm.repository.UserRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 주문 작성 검사를 통과한 뒤 저장하기 전에 거래처·품목 상태가 바뀐 상황을 검증한다.
 * 검사와 저장이 서로 다른 트랜잭션이므로, 저장 트랜잭션이 상태를 다시 확인하지 않으면
 * 비활성 거래처나 단종 품목으로 주문이 만들어진다.
 */
@SpringBootTest
@DisplayName("주문 저장 직전 거래처·품목 재검증 통합 테스트")
class OrderSaveRecheckIntegrationTest {

    @Autowired
    private PurchaseOrderPersistenceService purchaseOrderPersistenceService;
    @Autowired
    private SalesOrderPersistenceService salesOrderPersistenceService;
    @Autowired
    private PartnerRepository partnerRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;
    @Autowired
    private SalesOrderRepository salesOrderRepository;

    private Long writerId;
    private Long categoryId;

    @BeforeEach
    void setUp() {
        writerId = userRepository.findByEmail("user@scm.com").orElseThrow().getId();
        categoryId = categoryRepository.findAll().get(0).getId();
    }

    @Test
    @DisplayName("검사 뒤 비활성화된 공급사로는 발주를 저장하지 않는다")
    void purchaseOrder_partnerDeactivatedAfterValidation() {
        // 검사를 통과한 직후 다른 관리자가 거래처를 비활성화한 상황을 만든다.
        Partner partner = partner(PartnerType.SUPPLIER, PartnerStatus.INACTIVE);
        Item item = item(ItemStatus.ACTIVE);
        long before = purchaseOrderRepository.count();

        assertCode(() -> purchaseOrderPersistenceService.saveAndFlush(
                purchaseOrder(partner.getId(), item.getId())), ErrorCode.INVALID_STATUS);

        assertThat(purchaseOrderRepository.count()).isEqualTo(before);
    }

    @Test
    @DisplayName("검사 뒤 단종된 품목이 있으면 발주를 저장하지 않는다")
    void purchaseOrder_itemDiscontinuedAfterValidation() {
        Partner partner = partner(PartnerType.SUPPLIER, PartnerStatus.ACTIVE);
        Item item = item(ItemStatus.DISCONTINUED);
        long before = purchaseOrderRepository.count();

        assertCode(() -> purchaseOrderPersistenceService.saveAndFlush(
                purchaseOrder(partner.getId(), item.getId())), ErrorCode.ITEM_DISCONTINUED);

        assertThat(purchaseOrderRepository.count()).isEqualTo(before);
    }

    @Test
    @DisplayName("검사 뒤 공급사 전용으로 바뀐 거래처로는 수주를 저장하지 않는다")
    void salesOrder_partnerTypeChangedAfterValidation() {
        Partner partner = partner(PartnerType.SUPPLIER, PartnerStatus.ACTIVE);
        Item item = item(ItemStatus.ACTIVE);
        long before = salesOrderRepository.count();

        assertCode(() -> salesOrderPersistenceService.saveAndFlush(
                salesOrder(partner.getId(), item.getId())), ErrorCode.PARTNER_TYPE_MISMATCH);

        assertThat(salesOrderRepository.count()).isEqualTo(before);
    }

    @Test
    @DisplayName("검사 뒤 단종된 품목이 있으면 수주를 저장하지 않는다")
    void salesOrder_itemDiscontinuedAfterValidation() {
        Partner partner = partner(PartnerType.CUSTOMER, PartnerStatus.ACTIVE);
        Item item = item(ItemStatus.DISCONTINUED);
        long before = salesOrderRepository.count();

        assertCode(() -> salesOrderPersistenceService.saveAndFlush(
                salesOrder(partner.getId(), item.getId())), ErrorCode.ITEM_DISCONTINUED);

        assertThat(salesOrderRepository.count()).isEqualTo(before);
    }

    @Test
    @DisplayName("거래처와 품목이 그대로 사용 가능하면 발주와 수주를 정상 저장한다")
    void activePartnerAndItem_areSaved() {
        Partner partner = partner(PartnerType.BOTH, PartnerStatus.ACTIVE);
        Item item = item(ItemStatus.ACTIVE);

        assertThat(purchaseOrderPersistenceService.saveAndFlush(
                purchaseOrder(partner.getId(), item.getId())).getId()).isNotNull();
        assertThat(salesOrderPersistenceService.saveAndFlush(
                salesOrder(partner.getId(), item.getId())).getId()).isNotNull();
    }

    // ===== 헬퍼 =====

    private void assertCode(org.assertj.core.api.ThrowableAssert.ThrowingCallable call, ErrorCode expected) {
        assertThatThrownBy(call)
                .isInstanceOfSatisfying(BusinessException.class,
                        e -> assertThat(e.getErrorCode()).isEqualTo(expected));
    }

    // 다른 테스트의 자료와 겹치지 않도록 사업자번호·품목코드·주문번호에 무작위 값을 붙인다.
    private String unique(String prefix) {
        return prefix + UUID.randomUUID().toString().substring(0, 12);
    }

    private Partner partner(PartnerType type, PartnerStatus status) {
        return partnerRepository.save(Partner.builder()
                .name("재검증 거래처").businessNumber(unique("R-"))
                .partnerType(type).status(status).build());
    }

    private Item item(ItemStatus status) {
        return itemRepository.save(Item.builder()
                .itemCode(unique("RC-")).name("재검증 품목").categoryId(categoryId).unit("EA")
                .unitPrice(new BigDecimal("100.00")).safetyStock(0).status(status).build());
    }

    private PurchaseOrder purchaseOrder(Long partnerId, Long itemId) {
        PurchaseOrder order = PurchaseOrder.builder()
                .orderNumber(unique("PO-T-")).partnerId(partnerId).writerId(writerId)
                .orderDate(LocalDate.now()).totalAmount(new BigDecimal("100.00")).build();
        order.addLine(PurchaseOrderLine.builder()
                .itemId(itemId).quantity(1).unitPrice(new BigDecimal("100.00")).build());
        return order;
    }

    private SalesOrder salesOrder(Long partnerId, Long itemId) {
        SalesOrder order = new SalesOrder(unique("SO-T-"), partnerId, writerId, LocalDate.now(), null);
        order.addLine(SalesOrderLine.builder()
                .itemId(itemId).quantity(1).unitPrice(new BigDecimal("100.00")).build());
        return order;
    }
}
