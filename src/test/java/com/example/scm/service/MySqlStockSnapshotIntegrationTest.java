package com.example.scm.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.scm.common.auth.LoginUser;
import com.example.scm.domain.Category;
import com.example.scm.domain.Item;
import com.example.scm.domain.Partner;
import com.example.scm.domain.PurchaseOrder;
import com.example.scm.domain.PurchaseOrderLine;
import com.example.scm.domain.SalesOrder;
import com.example.scm.domain.SalesOrderLine;
import com.example.scm.domain.Stock;
import com.example.scm.domain.User;
import com.example.scm.domain.enums.PartnerType;
import com.example.scm.domain.enums.PurchaseOrderStatus;
import com.example.scm.domain.enums.SalesOrderStatus;
import com.example.scm.domain.enums.UserRole;
import com.example.scm.repository.CategoryRepository;
import com.example.scm.repository.ItemRepository;
import com.example.scm.repository.PartnerRepository;
import com.example.scm.repository.PurchaseOrderRepository;
import com.example.scm.repository.SalesOrderRepository;
import com.example.scm.repository.StockRepository;
import com.example.scm.repository.UserRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

@Tag("mysql")
@ActiveProfiles("mysql")
@SpringBootTest(properties = "scm.seed.enabled=false")
class MySqlStockSnapshotIntegrationTest {
    @Autowired PurchaseOrderService purchases;
    @Autowired SalesOrderService sales;
    @Autowired PurchaseOrderRepository purchaseOrders;
    @Autowired SalesOrderRepository salesOrders;
    @Autowired StockRepository stocks;
    @Autowired ItemRepository items;
    @Autowired CategoryRepository categories;
    @Autowired PartnerRepository partners;
    @Autowired UserRepository users;
    @Autowired PlatformTransactionManager transactions;

    LoginUser admin;
    Partner partner;
    Item item;

    @BeforeEach
    void setUp() {
        String key = UUID.randomUUID().toString();
        User user = users.saveAndFlush(User.builder().email(key + "@example.com")
                .password("unused-test-hash").name("재고 테스트").role(UserRole.ADMIN).build());
        admin = new LoginUser(user.getId(), user.getName(), user.getEmail(), user.getRole());
        partner = partners.saveAndFlush(Partner.builder().name("재고 테스트 거래처")
                .businessNumber(key.substring(0, 20)).partnerType(PartnerType.BOTH).build());
        Category category = categories.saveAndFlush(Category.builder().name(key).build());
        item = items.saveAndFlush(Item.builder().itemCode(key).name("재고 테스트 품목")
                .categoryId(category.getId()).unit("EA").unitPrice(BigDecimal.ONE).build());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 5})
    void receiptReadsStockCommittedAfterOrderSnapshot(int initialStock) {
        if (initialStock > 0) stocks.saveAndFlush(new Stock(item.getId(), initialStock));
        Long first = approvedPurchase(4);
        Long second = approvedPurchase(6);

        afterOtherReceipt(
                () -> purchaseOrders.findByIdWithLines(first).orElseThrow(), second,
                () -> purchases.receive(first, admin));

        assertThat(stocks.findByItemId(item.getId()).orElseThrow().getQuantity()).isEqualTo(initialStock + 10);
        assertThat(purchaseOrders.findById(first).orElseThrow().getStatus()).isEqualTo(PurchaseOrderStatus.RECEIVED);
        assertThat(purchaseOrders.findById(second).orElseThrow().getStatus()).isEqualTo(PurchaseOrderStatus.RECEIVED);
    }

    @Test
    void shipmentUsesReplenishmentCommittedAfterOrderSnapshot() {
        stocks.saveAndFlush(new Stock(item.getId(), 2));
        Long order = confirmedSalesOrder(4);
        Long receipt = approvedPurchase(6);

        afterOtherReceipt(() -> salesOrders.findById(order).orElseThrow().getLines().size(), receipt,
                () -> sales.ship(order, admin));

        assertThat(stocks.findByItemId(item.getId()).orElseThrow().getQuantity()).isEqualTo(4);
        assertThat(salesOrders.findById(order).orElseThrow().getStatus()).isEqualTo(SalesOrderStatus.SHIPPED);
    }

    @Test
    void cancellationRestoresStockIncludingReceiptCommittedAfterOrderSnapshot() {
        stocks.saveAndFlush(new Stock(item.getId(), 10));
        Long order = confirmedSalesOrder(4);
        sales.ship(order, admin);
        Long receipt = approvedPurchase(6);

        afterOtherReceipt(() -> salesOrders.findById(order).orElseThrow().getLines().size(), receipt,
                () -> sales.cancel(order, "고객 요청", admin));

        assertThat(stocks.findByItemId(item.getId()).orElseThrow().getQuantity()).isEqualTo(16);
        assertThat(salesOrders.findById(order).orElseThrow().getStatus()).isEqualTo(SalesOrderStatus.CANCELED);
    }

    private void afterOtherReceipt(Runnable loadOrder, Long receipt, Runnable changeStock) {
        TransactionTemplate first = new TransactionTemplate(transactions);
        first.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
        TransactionTemplate second = new TransactionTemplate(transactions);
        second.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        first.executeWithoutResult(tx -> {
            // 주문 조회로 만들어진 스냅샷 뒤에 다른 입고가 커밋되는 순서를 고정한다.
            loadOrder.run();
            second.executeWithoutResult(other -> purchases.receive(receipt, admin));
            changeStock.run();
        });
    }

    private Long approvedPurchase(int quantity) {
        PurchaseOrder order = PurchaseOrder.builder().orderNumber("PO-" + UUID.randomUUID().toString().substring(0, 20))
                .partnerId(partner.getId()).writerId(admin.id()).orderDate(LocalDate.now())
                .totalAmount(BigDecimal.valueOf(quantity)).build();
        order.addLine(PurchaseOrderLine.builder().itemId(item.getId()).quantity(quantity).unitPrice(BigDecimal.ONE).build());
        order.submit();
        order.approve(admin.id());
        return purchaseOrders.saveAndFlush(order).getId();
    }

    private Long confirmedSalesOrder(int quantity) {
        SalesOrder order = new SalesOrder("SO-" + UUID.randomUUID().toString().substring(0, 20),
                partner.getId(), admin.id(), LocalDate.now(), null);
        order.addLine(SalesOrderLine.builder().itemId(item.getId()).quantity(quantity).unitPrice(BigDecimal.ONE).build());
        order.confirm();
        return salesOrders.saveAndFlush(order).getId();
    }
}
