package com.example.scm.controller;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.scm.common.auth.*;
import com.example.scm.common.exception.*;
import com.example.scm.domain.*;
import com.example.scm.domain.enums.*;
import com.example.scm.dto.salesorder.*;
import com.example.scm.dto.notice.*;
import com.example.scm.repository.*;
import com.example.scm.service.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class SalesAndNoticeIntegrationTest {
    @Autowired MockMvc mvc;
    @Autowired SalesOrderService sales;
    @Autowired NoticeService notices;
    @Autowired SalesOrderRepository orders;
    @Autowired NoticeRepository noticeRepository;
    @Autowired StockRepository stocks;
    @Autowired ItemRepository items;
    @Autowired PartnerRepository partners;
    @Autowired UserRepository users;
    @Autowired org.springframework.transaction.PlatformTransactionManager transactionManager;
    LoginUser writer, admin, manager;
    Partner customer;
    Item item;

    @BeforeEach
    void setup() {
        orders.deleteAll(); noticeRepository.deleteAll(); stocks.deleteAll();
        writer = login("user@scm.com"); admin = login("admin@scm.com"); manager = login("manager@scm.com");
        customer = partners.findAll().stream().filter(Partner::canBuy).findFirst().orElseThrow();
        item = items.findByStatusOrderByItemCodeAsc(ItemStatus.ACTIVE).get(0);
    }
    @Test
    void salesFlowCalculatesMoneyAndShipsOnce() {
        stocks.save(new Stock(item.getId(), 10));
        var request = request(3); request.getLines().get(0).setUnitPrice(new BigDecimal("12.34"));
        var draft = sales.create(request, writer);
        assertThat(draft.orderNumber()).matches("SO-\\d{8}-\\d{4}");
        assertThat(draft.totalAmount()).isEqualByComparingTo("37.02");
        assertThat(sales.confirm(draft.salesOrderId(), writer).status()).isEqualTo(SalesOrderStatus.CONFIRMED);
        assertThat(sales.pending(manager, PageRequest.of(0, 20)).getTotalElements()).isEqualTo(1);
        sales.ship(draft.salesOrderId(), manager);
        assertThat(stocks.findByItemId(item.getId()).orElseThrow().getQuantity()).isEqualTo(7);
        assertError(() -> sales.ship(draft.salesOrderId(), admin), ErrorCode.INVALID_STATUS);
        var complete = sales.complete(draft.salesOrderId(), manager);
        assertThat(complete.completedAt()).isNotNull(); assertThat(complete.managerId()).isEqualTo(manager.id());
        assertError(() -> sales.cancel(draft.salesOrderId(), "취소", writer), ErrorCode.INVALID_STATUS);
    }
    @Test
    void insufficientStockRollsBackEveryLineAndStatus() {
        Item second = items.findByStatusOrderByItemCodeAsc(ItemStatus.ACTIVE).get(1);
        stocks.save(new Stock(item.getId(), 5)); stocks.save(new Stock(second.getId(), 1));
        var request = request(3);
        var line = new SalesOrderCreateRequest.LineRequest(); line.setItemId(second.getId()); line.setQuantity(2);
        request.getLines().add(line);
        Long id = sales.create(request, writer).salesOrderId(); sales.confirm(id, writer);
        assertError(() -> sales.ship(id, manager), ErrorCode.INSUFFICIENT_STOCK);
        assertThat(stocks.findByItemId(item.getId()).orElseThrow().getQuantity()).isEqualTo(5);
        assertThat(sales.detail(id, writer).status()).isEqualTo(SalesOrderStatus.CONFIRMED);
        assertThat(sales.detail(id, writer).shippedAt()).isNull();
    }
    @Test
    void repeatedItemLinesCannotOverdrawStock() {
        stocks.save(new Stock(item.getId(), 5));
        var request = request(3); request.getLines().add(request.getLines().get(0));
        Long id = sales.create(request, writer).salesOrderId(); sales.confirm(id, writer);
        assertError(() -> sales.ship(id, manager), ErrorCode.INSUFFICIENT_STOCK);
        assertThat(stocks.findByItemId(item.getId()).orElseThrow().getQuantity()).isEqualTo(5);
    }
    @Test
    void cancelAfterShippingRestoresStockExactlyOnce() {
        stocks.save(new Stock(item.getId(), 10));
        Long id = sales.create(request(4), writer).salesOrderId(); sales.confirm(id, writer); sales.ship(id, manager);
        // 출고 후 취소는 재고를 되돌리므로 일반 작성자는 할 수 없고 재고도 그대로여야 한다.
        assertError(() -> sales.cancel(id, "고객 요청", writer), ErrorCode.ACCESS_DENIED);
        assertThat(stocks.findByItemId(item.getId()).orElseThrow().getQuantity()).isEqualTo(6);
        assertError(() -> sales.cancel(id, "  ", manager), ErrorCode.INVALID_INPUT);
        assertThat(sales.cancel(id, "  고객 요청  ", manager).cancelReason()).isEqualTo("고객 요청");
        assertThat(stocks.findByItemId(item.getId()).orElseThrow().getQuantity()).isEqualTo(10);
        assertError(() -> sales.cancel(id, "중복", manager), ErrorCode.INVALID_STATUS);
    }
    @Test
    void salesEnforcesWriterAndManagerPermissions() {
        Long id = sales.create(request(1), writer).salesOrderId();
        LoginUser other = new LoginUser(999L, "다른 사용자", "other@example.com", UserRole.USER);
        assertError(() -> sales.detail(id, other), ErrorCode.ACCESS_DENIED);
        assertError(() -> sales.confirm(id, manager), ErrorCode.ACCESS_DENIED);
        assertError(() -> sales.cancel(id, "취소", other), ErrorCode.ACCESS_DENIED);
        assertError(() -> sales.pending(writer, PageRequest.of(0, 20)), ErrorCode.ACCESS_DENIED);
        assertError(() -> sales.ship(id, writer), ErrorCode.ACCESS_DENIED);
        assertError(() -> sales.complete(id, manager), ErrorCode.INVALID_STATUS);
        assertThat(sales.myOrders(other, null, PageRequest.of(0, 20))).isEmpty();
        sales.cancel(id, "관리자 취소", admin);
        assertError(() -> sales.confirm(id, writer), ErrorCode.INVALID_STATUS);
    }
    @Test
    void salesRejectsInvalidPartnerDatesItemsAndAmounts() {
        var req = request(1);
        req.setPartnerId(partners.findAll().stream().filter(p -> !p.canBuy()).findFirst().orElseThrow().getId());
        assertError(() -> sales.create(req, writer), ErrorCode.PARTNER_TYPE_MISMATCH);
        req.setPartnerId(customer.getId()); req.setShipDate(LocalDate.now().minusDays(1));
        assertError(() -> sales.create(req, writer), ErrorCode.INVALID_DATE_RANGE);
        req.setShipDate(null); req.getLines().get(0).setQuantity(0);
        assertError(() -> sales.create(req, writer), ErrorCode.INVALID_INPUT);
        req.getLines().get(0).setQuantity(1); req.getLines().get(0).setItemId(items.findByStatusOrderByItemCodeAsc(ItemStatus.DISCONTINUED).get(0).getId());
        assertError(() -> sales.create(req, writer), ErrorCode.ITEM_DISCONTINUED);
        req.getLines().get(0).setItemId(item.getId()); req.getLines().get(0).setQuantity(2);
        req.getLines().get(0).setUnitPrice(new BigDecimal("9999999999999.99"));
        assertError(() -> sales.create(req, writer), ErrorCode.INVALID_INPUT);
        req.setLines(Collections.singletonList(null)); assertError(() -> sales.create(req, writer), ErrorCode.INVALID_INPUT);
        req.setLines(List.of()); assertError(() -> sales.create(req, writer), ErrorCode.EMPTY_ORDER_LINES);
        assertThat(orders.count()).isZero();
    }
    @Test
    void concurrentCreateProducesUniqueSalesNumbers() throws Exception {
        var results = concurrent(() -> sales.create(request(1), writer).orderNumber(),
                () -> sales.create(request(2), writer).orderNumber());
        assertThat(results).doesNotHaveDuplicates(); assertThat(orders.count()).isEqualTo(2);
    }
    @Test
    void concurrentOrdersCannotShipMoreThanAvailableStock() throws Exception {
        stocks.save(new Stock(item.getId(), 5));
        Long first = sales.create(request(4), writer).salesOrderId(), second = sales.create(request(4), writer).salesOrderId();
        sales.confirm(first, writer); sales.confirm(second, writer);
        var results = concurrent(() -> tryShip(first), () -> tryShip(second));
        assertThat(results).containsExactlyInAnyOrder("SHIPPED", "INSUFFICIENT_STOCK");
        assertThat(stocks.findByItemId(item.getId()).orElseThrow().getQuantity()).isEqualTo(1);
    }
    @Test
    void concurrentDuplicateShipmentAndCancellationChangeStockOnce() throws Exception {
        stocks.save(new Stock(item.getId(), 10));
        Long id = sales.create(request(4), writer).salesOrderId(); sales.confirm(id, writer);
        assertThat(concurrent(() -> tryShip(id), () -> tryShip(id))).containsExactlyInAnyOrder("SHIPPED", "INVALID_STATUS");
        assertThat(stocks.findByItemId(item.getId()).orElseThrow().getQuantity()).isEqualTo(6);
        assertThat(concurrent(() -> tryCancel(id), () -> tryCancel(id))).containsExactlyInAnyOrder("CANCELED", "INVALID_STATUS");
        assertThat(stocks.findByItemId(item.getId()).orElseThrow().getQuantity()).isEqualTo(10);
    }
    @Test
    void noticesSortImportantFirstAndSupportCrudSearchAndViews() {
        Long important = notices.create(notice("중요", true), admin).id();
        notices.create(notice("일반", false), admin);
        var page = notices.list(null, PageRequest.of(0, 1), writer);
        assertThat(page.getTotalElements()).isEqualTo(2); assertThat(page.getContent().get(0).id()).isEqualTo(important);
        assertThat(notices.detail(important, writer).viewCount()).isEqualTo(1);
        assertThat(notices.detail(important, manager).viewCount()).isEqualTo(2);
        notices.update(important, notice("변경", false), admin);
        assertThat(notices.list("변경", PageRequest.of(0, 20), writer).getTotalElements()).isEqualTo(1);
        assertThat(notices.editDetail(important, admin).viewCount()).isEqualTo(2);
        notices.delete(important, admin); assertError(() -> notices.detail(important, writer), ErrorCode.NOTICE_NOT_FOUND);
    }
    @Test
    void concurrentNoticeReadsDoNotLoseViews() throws Exception {
        Long id = notices.create(notice("동시 조회", false), admin).id();
        concurrent(() -> notices.detail(id, writer), () -> notices.detail(id, writer));
        assertThat(notices.editDetail(id, admin).viewCount()).isEqualTo(2);
    }
    @Test
    void noticeEditCannotOverwriteConcurrentViewIncrement() throws Exception {
        Long id = notices.create(notice("수정 중 조회", false), admin).id();
        var loaded = new CountDownLatch(1); var readDone = new CountDownLatch(1);
        var executor = Executors.newSingleThreadExecutor();
        try {
            Future<?> edit = executor.submit(() -> new org.springframework.transaction.support.TransactionTemplate(transactionManager)
                    .executeWithoutResult(tx -> {
                        Notice notice = noticeRepository.findById(id).orElseThrow();
                        loaded.countDown();
                        try {
                            if (!readDone.await(5, TimeUnit.SECONDS)) throw new IllegalStateException("조회 대기 실패");
                        } catch (InterruptedException e) { Thread.currentThread().interrupt(); throw new IllegalStateException(e); }
                        notice.update("수정 완료", "변경 내용", true);
                    }));
            assertThat(loaded.await(5, TimeUnit.SECONDS)).isTrue();
            notices.detail(id, writer); readDone.countDown(); edit.get(10, TimeUnit.SECONDS);
            var saved = notices.editDetail(id, admin);
            assertThat(saved.title()).isEqualTo("수정 완료"); assertThat(saved.viewCount()).isEqualTo(1);
        } finally { readDone.countDown(); executor.shutdownNow(); }
    }
    @Test
    void salesApiCancellationRequiresReasonAndPreservesTerminalState() throws Exception {
        Long id = sales.create(request(2), writer).salesOrderId();
        mvc.perform(patch("/api/sales-orders/" + id + "/cancel").with(csrf()).sessionAttr(SessionConst.LOGIN_USER, writer)
                .contentType(MediaType.APPLICATION_JSON).content("{\"cancelReason\":\" \"}"))
                .andExpect(status().isBadRequest());
        mvc.perform(patch("/api/sales-orders/" + id + "/cancel").with(csrf()).sessionAttr(SessionConst.LOGIN_USER, writer)
                .contentType(MediaType.APPLICATION_JSON).content("{\"cancelReason\":\"고객 요청\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.status").value("CANCELED"));
        mvc.perform(patch("/api/sales-orders/" + id + "/confirm").with(csrf()).sessionAttr(SessionConst.LOGIN_USER, writer))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.code").value("INVALID_STATUS"));
    }
    @Test
    void apiRejectsUnauthenticatedForbiddenAndInvalidRequests() throws Exception {
        mvc.perform(get("/api/notices").servletPath("/api/notices")).andExpect(status().isUnauthorized());
        mvc.perform(post("/api/notices").with(csrf()).sessionAttr(SessionConst.LOGIN_USER, manager)
                .contentType(MediaType.APPLICATION_JSON).content("{\"title\":\"제목\",\"content\":\"내용\"}"))
                .andExpect(status().isForbidden());
        mvc.perform(post("/api/notices").with(csrf()).sessionAttr(SessionConst.LOGIN_USER, admin)
                .contentType(MediaType.APPLICATION_JSON).content("{\"title\":\" \",\"content\":\"\"}"))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.code").value("INVALID_INPUT"));
        mvc.perform(post("/api/sales-orders").with(csrf()).sessionAttr(SessionConst.LOGIN_USER, writer)
                .contentType(MediaType.APPLICATION_JSON).content("{\"partnerId\":3,\"orderDate\":\"2026-10-03\",\"lines\":[null]}"))
                .andExpect(status().isBadRequest());
        mvc.perform(get("/api/sales-orders/pending").sessionAttr(SessionConst.LOGIN_USER, writer)).andExpect(status().isForbidden());
        mvc.perform(get("/api/sales-orders/999999").sessionAttr(SessionConst.LOGIN_USER, writer))
                .andExpect(status().isNotFound()).andExpect(jsonPath("$.code").value("SALES_ORDER_NOT_FOUND"));
        mvc.perform(post("/notices").sessionAttr(SessionConst.LOGIN_USER, admin)).andExpect(status().isForbidden());
    }
    @Test
    void salesApiSupportsCreateConfirmShipCompleteAndLists() throws Exception {
        stocks.save(new Stock(item.getId(), 10));
        String json = "{\"partnerId\":" + customer.getId() + ",\"orderDate\":\"2026-10-03\",\"lines\":[{\"itemId\":" + item.getId() + ",\"quantity\":2,\"unitPrice\":12.34}]}";
        mvc.perform(post("/api/sales-orders").with(csrf()).sessionAttr(SessionConst.LOGIN_USER, writer)
                .contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isCreated())
                .andExpect(jsonPath("$.totalAmount").value(24.68)).andExpect(jsonPath("$.status").value("DRAFT"));
        Long id = orders.findAll().get(0).getId();
        mvc.perform(patch("/api/sales-orders/" + id + "/confirm").with(csrf()).sessionAttr(SessionConst.LOGIN_USER, writer)).andExpect(status().isOk());
        mvc.perform(get("/api/sales-orders/my").sessionAttr(SessionConst.LOGIN_USER, writer).param("status", "CONFIRMED"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.totalElements").value(1));
        mvc.perform(patch("/api/sales-orders/" + id + "/ship").with(csrf()).sessionAttr(SessionConst.LOGIN_USER, manager))
                .andExpect(status().isOk()).andExpect(jsonPath("$.status").value("SHIPPED"));
        mvc.perform(patch("/api/sales-orders/" + id + "/complete").with(csrf()).sessionAttr(SessionConst.LOGIN_USER, manager))
                .andExpect(status().isOk()).andExpect(jsonPath("$.status").value("COMPLETED"));
    }
    @Test
    void webScreensRenderFormsDetailsListsAndPreserveValidationErrors() throws Exception {
        Long id = sales.create(request(2), writer).salesOrderId();
        Long noticeId = notices.create(notice("공지 화면", true), admin).id();
        for (String url : List.of("/", "/sales-orders/new", "/sales-orders/my", "/sales-orders/" + id, "/notices", "/notices/" + noticeId))
            mvc.perform(get(url).sessionAttr(SessionConst.LOGIN_USER, writer)).andExpect(status().isOk());
        mvc.perform(get("/sales-orders/pending").sessionAttr(SessionConst.LOGIN_USER, manager)).andExpect(status().isOk());
        mvc.perform(get("/notices/new").sessionAttr(SessionConst.LOGIN_USER, admin)).andExpect(status().isOk());
        mvc.perform(get("/notices/" + noticeId + "/edit").sessionAttr(SessionConst.LOGIN_USER, admin)).andExpect(status().isOk());
        mvc.perform(get("/notices/new").sessionAttr(SessionConst.LOGIN_USER, writer)).andExpect(status().isForbidden());
        mvc.perform(post("/notices").with(csrf()).sessionAttr(SessionConst.LOGIN_USER, admin).param("title", "").param("content", ""))
                .andExpect(status().isOk()).andExpect(view().name("notice/form")).andExpect(content().string(containsString("제목은 필수")));
        mvc.perform(post("/sales-orders").with(csrf()).sessionAttr(SessionConst.LOGIN_USER, writer))
                .andExpect(status().isOk()).andExpect(view().name("salesorder/form"));
        mvc.perform(post("/sales-orders/" + id + "/confirm").with(csrf()).sessionAttr(SessionConst.LOGIN_USER, writer))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/sales-orders/" + id));
        mvc.perform(post("/sales-orders/" + id + "/ship").with(csrf()).sessionAttr(SessionConst.LOGIN_USER, manager))
                .andExpect(status().is3xxRedirection()).andExpect(flash().attribute("errorMessage", ErrorCode.INSUFFICIENT_STOCK.getMessage()));
    }
    @Test
    void noticeApiCrudEscapesUserContentAndChecksPermissions() throws Exception {
        mvc.perform(post("/api/notices").with(csrf()).sessionAttr(SessionConst.LOGIN_USER, admin)
                .contentType(MediaType.APPLICATION_JSON).content("{\"title\":\"API 공지\",\"content\":\"<script>alert(1)</script>\",\"important\":true}"))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.important").value(true));
        Long id = noticeRepository.findAll().get(0).getId();
        mvc.perform(get("/notices/" + id).sessionAttr(SessionConst.LOGIN_USER, writer))
                .andExpect(status().isOk()).andExpect(content().string(containsString("&lt;script&gt;")));
        mvc.perform(put("/api/notices/" + id).with(csrf()).sessionAttr(SessionConst.LOGIN_USER, admin)
                .contentType(MediaType.APPLICATION_JSON).content("{\"title\":\"수정 공지\",\"content\":\"새 내용\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.title").value("수정 공지"));
        mvc.perform(delete("/api/notices/" + id).with(csrf()).sessionAttr(SessionConst.LOGIN_USER, writer)).andExpect(status().isForbidden());
        mvc.perform(delete("/api/notices/" + id).with(csrf()).sessionAttr(SessionConst.LOGIN_USER, admin)).andExpect(status().isNoContent());
        mvc.perform(get("/api/notices/" + id).sessionAttr(SessionConst.LOGIN_USER, writer)).andExpect(status().isNotFound());
    }
    @ParameterizedTest
    @ValueSource(strings = {"", "/scm"})
    void noticeFormsKeepContextPath(String contextPath) throws Exception {
        Long id = notices.create(notice("수정 전", false), admin).id();
        for (String path : List.of("/notices/new", "/notices/" + id + "/edit")) {
            String action = contextPath + (path.endsWith("/new") ? "/notices" : "/notices/" + id);
            mvc.perform(get(contextPath + path).contextPath(contextPath).servletPath(path)
                            .sessionAttr(SessionConst.LOGIN_USER, admin))
                    .andExpect(status().isOk())
                    .andExpect(content().string(containsString("<form action=\"" + action + "\" method=\"post\">")));
        }
        mvc.perform(post(contextPath + "/notices").contextPath(contextPath).servletPath("/notices")
                        .with(csrf()).sessionAttr(SessionConst.LOGIN_USER, admin)
                        .param("title", "새 공지").param("content", "등록 내용"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern(contextPath + "/notices/*"));
        mvc.perform(post(contextPath + "/notices/" + id).contextPath(contextPath).servletPath("/notices/" + id)
                        .with(csrf()).sessionAttr(SessionConst.LOGIN_USER, admin)
                        .param("title", "수정 후").param("content", "수정 내용"))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl(contextPath + "/notices/" + id));
        assertThat(notices.editDetail(id, admin).title()).isEqualTo("수정 후");
        assertThat(noticeRepository.count()).isEqualTo(2);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "/scm"})
    void salesPaginationKeepsContextPathAndPageSize(String contextPath) throws Exception {
        for (int i = 0; i < 2; i++) sales.confirm(sales.create(request(1), writer).salesOrderId(), writer);
        for (String path : List.of("/sales-orders/my", "/sales-orders/pending")) {
            boolean myOrders = path.endsWith("/my");
            String nextPage = contextPath + path + (myOrders ? "?status=CONFIRMED&size=1&page=1" : "?size=1&page=1");
            mvc.perform(get(contextPath + path).contextPath(contextPath).servletPath(path)
                            .sessionAttr(SessionConst.LOGIN_USER, myOrders ? writer : manager)
                            .param("status", "CONFIRMED").param("size", "1"))
                    .andExpect(status().isOk())
                    .andExpect(content().string(containsString("href=\"" + nextPage.replace("&", "&amp;") + "\">2</a>")));
            mvc.perform(get(nextPage).contextPath(contextPath).servletPath(path)
                            .sessionAttr(SessionConst.LOGIN_USER, myOrders ? writer : manager))
                    .andExpect(status().isOk())
                    .andExpect(content().string(containsString("<span class=\"current\">2</span>")))
                    .andExpect(result -> {
                        var page = (org.springframework.data.domain.Page<?>) result.getModelAndView().getModel().get("orders");
                        assertThat(page.getContent()).hasSize(1);
                        assertThat(page.getNumber()).isEqualTo(1);
                    });
        }
    }

    private LoginUser login(String email) {
        User u = users.findByEmail(email).orElseThrow(); return new LoginUser(u.getId(), u.getName(), u.getEmail(), u.getRole());
    }
    private SalesOrderCreateRequest request(int quantity) {
        var line = new SalesOrderCreateRequest.LineRequest(); line.setItemId(item.getId()); line.setQuantity(quantity);
        var r = new SalesOrderCreateRequest(); r.setPartnerId(customer.getId()); r.setOrderDate(LocalDate.now());
        r.setLines(new ArrayList<>(List.of(line))); return r;
    }
    private NoticeRequest notice(String title, boolean important) {
        var r = new NoticeRequest(); r.setTitle(title); r.setContent("공지 내용"); r.setImportant(important); return r;
    }
    private void assertError(Runnable action, ErrorCode code) {
        assertThatThrownBy(action::run).isInstanceOfSatisfying(BusinessException.class, e -> assertThat(e.getErrorCode()).isEqualTo(code));
    }
    private String tryShip(Long id) {
        try { return sales.ship(id, manager).status().name(); } catch (BusinessException e) { return e.getErrorCode().name(); }
    }
    private String tryCancel(Long id) {
        try { return sales.cancel(id, "취소", manager).status().name(); } catch (BusinessException e) { return e.getErrorCode().name(); }
    }
    private <T> List<T> concurrent(Callable<T> first, Callable<T> second) throws Exception {
        var executor = Executors.newFixedThreadPool(2); var ready = new CountDownLatch(2); var start = new CountDownLatch(1);
        try {
            List<Future<T>> futures = new ArrayList<>();
            for (Callable<T> task : List.of(first, second)) futures.add(executor.submit(() -> {
                ready.countDown(); if (!start.await(5, TimeUnit.SECONDS)) throw new IllegalStateException("시작 대기 실패"); return task.call();
            }));
            assertThat(ready.await(5, TimeUnit.SECONDS)).isTrue(); start.countDown();
            return List.of(futures.get(0).get(15, TimeUnit.SECONDS), futures.get(1).get(15, TimeUnit.SECONDS));
        } finally { executor.shutdownNow(); }
    }
}
