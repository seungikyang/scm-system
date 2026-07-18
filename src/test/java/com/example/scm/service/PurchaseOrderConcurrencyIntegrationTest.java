package com.example.scm.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.scm.common.auth.LoginUser;
import com.example.scm.domain.Item;
import com.example.scm.domain.Partner;
import com.example.scm.domain.User;
import com.example.scm.domain.enums.ItemStatus;
import com.example.scm.dto.purchaseorder.PurchaseOrderCreateRequest;
import com.example.scm.dto.purchaseorder.PurchaseOrderCreateResponse;
import com.example.scm.repository.ItemRepository;
import com.example.scm.repository.PartnerRepository;
import com.example.scm.repository.StockRepository;
import com.example.scm.repository.UserRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@DisplayName("발주 동시성 통합 테스트")
class PurchaseOrderConcurrencyIntegrationTest {

    @Autowired
    private PurchaseOrderService purchaseOrderService;
    @Autowired
    private PartnerRepository partnerRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StockRepository stockRepository;

    @Test
    @DisplayName("같은 날 두 발주를 동시에 작성해도 서로 다른 번호가 저장된다")
    void concurrentCreateProducesUniqueOrderNumbers() throws Exception {
        LoginUser writer = loginUser("user@scm.com");
        Partner supplier = activeSupplier();
        Item item = activeItem();

        List<PurchaseOrderCreateResponse> responses = runConcurrently(
                () -> purchaseOrderService.create(request(supplier, item, 2), writer),
                () -> purchaseOrderService.create(request(supplier, item, 3), writer));

        assertThat(responses)
                .extracting(PurchaseOrderCreateResponse::getOrderNumber)
                .doesNotHaveDuplicates()
                .allMatch(number -> number.matches("PO-\\d{8}-\\d{4}"));
    }

    @Test
    @DisplayName("같은 품목을 두 발주에서 동시에 입고해도 최초 재고는 한 행이고 수량은 합산된다")
    void concurrentReceiveCreatesOneStockAndAddsAllQuantities() throws Exception {
        LoginUser writer = loginUser("user@scm.com");
        LoginUser admin = loginUser("admin@scm.com");
        Partner supplier = activeSupplier();
        Item item = activeItem();

        PurchaseOrderCreateResponse first = purchaseOrderService.create(
                request(supplier, item, 4), writer);
        PurchaseOrderCreateResponse second = purchaseOrderService.create(
                request(supplier, item, 6), writer);
        purchaseOrderService.submit(first.getPurchaseOrderId(), writer);
        purchaseOrderService.submit(second.getPurchaseOrderId(), writer);
        purchaseOrderService.approve(first.getPurchaseOrderId(), admin);
        purchaseOrderService.approve(second.getPurchaseOrderId(), admin);

        runConcurrently(
                () -> purchaseOrderService.receive(first.getPurchaseOrderId(), admin),
                () -> purchaseOrderService.receive(second.getPurchaseOrderId(), admin));

        assertThat(stockRepository.findAll())
                .filteredOn(stock -> stock.getItemId().equals(item.getId()))
                .singleElement()
                .extracting(stock -> stock.getQuantity())
                .isEqualTo(10);
    }

    private PurchaseOrderCreateRequest request(Partner supplier, Item item, int quantity) {
        PurchaseOrderCreateRequest.LineRequest line = new PurchaseOrderCreateRequest.LineRequest();
        line.setItemId(item.getId());
        line.setQuantity(quantity);

        PurchaseOrderCreateRequest request = new PurchaseOrderCreateRequest();
        request.setPartnerId(supplier.getId());
        request.setOrderDate(LocalDate.now());
        request.setDueDate(LocalDate.now().plusDays(3));
        request.setLines(List.of(line));
        return request;
    }

    private Partner activeSupplier() {
        return partnerRepository.findAll().stream()
                .filter(Partner::canSupply)
                .filter(Partner::isActive)
                .findFirst()
                .orElseThrow();
    }

    private Item activeItem() {
        return itemRepository.findByStatusOrderByItemCodeAsc(ItemStatus.ACTIVE).get(0);
    }

    private LoginUser loginUser(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        return new LoginUser(user.getId(), user.getName(), user.getEmail(), user.getRole());
    }

    private <T> List<T> runConcurrently(Callable<T> first, Callable<T> second) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        CountDownLatch ready = new CountDownLatch(2);
        CountDownLatch start = new CountDownLatch(1);
        try {
            Future<T> firstFuture = executor.submit(awaitStart(ready, start, first));
            Future<T> secondFuture = executor.submit(awaitStart(ready, start, second));
            assertThat(ready.await(5, TimeUnit.SECONDS)).isTrue();
            start.countDown();
            return List.of(
                    firstFuture.get(15, TimeUnit.SECONDS),
                    secondFuture.get(15, TimeUnit.SECONDS));
        } finally {
            executor.shutdownNow();
        }
    }

    private <T> Callable<T> awaitStart(CountDownLatch ready, CountDownLatch start,
                                       Callable<T> operation) {
        return () -> {
            ready.countDown();
            if (!start.await(5, TimeUnit.SECONDS)) {
                throw new IllegalStateException("동시 실행 시작 신호를 기다리지 못했습니다.");
            }
            return operation.call();
        };
    }
}
