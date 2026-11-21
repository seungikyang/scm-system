package com.example.scm.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.scm.common.auth.LoginUser;
import com.example.scm.common.auth.SessionConst;
import com.example.scm.domain.*;
import com.example.scm.domain.enums.*;
import com.example.scm.repository.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class OrderManagementIntegrationTest {
    @Autowired MockMvc mvc;
    @Autowired SalesOrderRepository sales;
    @Autowired PurchaseOrderRepository purchases;
    @Autowired PartnerRepository partners;
    @Autowired ItemRepository items;
    @Autowired UserRepository users;
    LoginUser writer, manager, admin;
    Partner customer;
    Item item;
    final LocalDate date = LocalDate.of(2026, 6, 15);

    @BeforeEach
    void setup() {
        sales.deleteAll(); purchases.deleteAll();
        writer = login("user@scm.com"); manager = login("manager@scm.com"); admin = login("admin@scm.com");
        customer = partners.findAll().stream().filter(Partner::canBuy).findFirst().orElseThrow();
        item = items.findByStatusOrderByItemCodeAsc(ItemStatus.ACTIVE).get(0);
    }

    @Test
    void managerFindsOtherWritersShippedOrdersAndCompletesThem() throws Exception {
        SalesOrder order = order("SO-20260615-0001", date, customer.getId());
        order.confirm(); order.ship(manager.id()); sales.saveAndFlush(order);
        mvc.perform(get("/api/admin/sales-orders").param("status", "SHIPPED")
                        .sessionAttr(SessionConst.LOGIN_USER, manager))
                .andExpect(status().isOk()).andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].writerId").value(writer.id()))
                .andExpect(jsonPath("$.content[0].lines").isEmpty());
        mvc.perform(get("/admin/sales-orders").param("status", "SHIPPED")
                        .sessionAttr(SessionConst.LOGIN_USER, manager))
                .andExpect(status().isOk()).andExpect(view().name("salesorder/admin-list"))
                .andExpect(content().string(containsString(order.getOrderNumber())));
        mvc.perform(get("/sales-orders/" + order.getId()).sessionAttr(SessionConst.LOGIN_USER, manager))
                .andExpect(status().isOk()).andExpect(content().string(containsString("/complete")));
        mvc.perform(post("/sales-orders/" + order.getId() + "/complete").with(csrf())
                        .sessionAttr(SessionConst.LOGIN_USER, manager))
                .andExpect(status().is3xxRedirection()).andExpect(flash().attributeExists("successMessage"));
        assertThat(sales.findById(order.getId()).orElseThrow().getStatus()).isEqualTo(SalesOrderStatus.COMPLETED);
    }

    @Test
    void combinedFiltersIncludeDateBoundariesAndPreservePagination() throws Exception {
        Partner other = partners.findAll().stream().filter(p -> !p.getId().equals(customer.getId())).findFirst().orElseThrow();
        SalesOrder first = order("SO-20260615-0001", date, customer.getId()); first.confirm(); sales.save(first);
        SalesOrder second = order("SO-20260615-0002", date.plusDays(1), customer.getId()); second.confirm(); sales.save(second);
        sales.save(order("SO-20260615-0003", date, customer.getId()));
        SalesOrder wrongCustomer = order("SO-20260615-0004", date, other.getId()); wrongCustomer.confirm(); sales.save(wrongCustomer);
        SalesOrder wrongDate = order("SO-20260615-0005", date.plusDays(2), customer.getId()); wrongDate.confirm(); sales.save(wrongDate);
        mvc.perform(get("/api/admin/sales-orders").param("status", "CONFIRMED")
                        .param("partnerId", customer.getId().toString()).param("from", date.toString())
                        .param("to", date.plusDays(1).toString()).param("size", "1").param("page", "1")
                        .sessionAttr(SessionConst.LOGIN_USER, admin))
                .andExpect(status().isOk()).andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.content[0].salesOrderId").value(first.getId()));
        mvc.perform(get("/admin/sales-orders").param("status", "CONFIRMED")
                        .param("partnerId", customer.getId().toString()).param("from", date.toString())
                        .param("to", date.plusDays(1).toString()).param("size", "1")
                        .sessionAttr(SessionConst.LOGIN_USER, manager))
                .andExpect(status().isOk()).andExpect(content().string(containsString("from=2026-06-15")))
                .andExpect(content().string(containsString("to=2026-06-16")))
                .andExpect(content().string(containsString("size=1")));
    }

    @Test
    void statisticsSumHeadersOnceIncludeEmptyStatusesAndUseInclusiveDates() throws Exception {
        SalesOrder confirmed = order("SO-20260615-0001", date, customer.getId());
        confirmed.addLine(SalesOrderLine.builder().itemId(item.getId()).quantity(2).unitPrice(new BigDecimal("1.10")).build());
        confirmed.confirm(); sales.save(confirmed);
        SalesOrder canceled = order("SO-20260615-0002", date, customer.getId()); canceled.cancel("취소"); sales.save(canceled);
        sales.save(order("SO-20260615-0003", date.minusDays(1), customer.getId()));
        PurchaseOrder purchase = PurchaseOrder.builder().orderNumber("PO-20260615-0001")
                .partnerId(customer.getId()).writerId(writer.id()).orderDate(date).totalAmount(new BigDecimal("30.75")).build();
        purchase.addLine(PurchaseOrderLine.builder().itemId(item.getId()).quantity(1).unitPrice(new BigDecimal("10.25")).build());
        purchase.addLine(PurchaseOrderLine.builder().itemId(item.getId()).quantity(2).unitPrice(new BigDecimal("10.25")).build());
        purchase.submit(); purchases.save(purchase);
        mvc.perform(get("/api/dashboard/order-statistics").param("from", date.toString()).param("to", date.toString())
                        .sessionAttr(SessionConst.LOGIN_USER, manager))
                .andExpect(status().isOk()).andExpect(jsonPath("$.salesOrders.length()").value(5))
                .andExpect(jsonPath("$.salesOrders[0].orderCount").value(0))
                .andExpect(jsonPath("$.salesOrders[1].status").value("CONFIRMED"))
                .andExpect(jsonPath("$.salesOrders[1].orderCount").value(1))
                .andExpect(jsonPath("$.salesOrders[1].totalAmount").value(14.54))
                .andExpect(jsonPath("$.salesOrders[4].orderCount").value(1))
                .andExpect(jsonPath("$.purchaseOrders[1].orderCount").value(1))
                .andExpect(jsonPath("$.purchaseOrders[1].totalAmount").value(30.75));
        mvc.perform(get("/").param("from", date.toString()).param("to", date.toString())
                        .sessionAttr(SessionConst.LOGIN_USER, admin))
                .andExpect(status().isOk()).andExpect(content().string(containsString("발주·수주 통계")))
                .andExpect(content().string(containsString("14.54")));
    }

    @Test
    void emptyStatisticsDefaultToThisMonthAndHideFromOrdinaryUsers() throws Exception {
        mvc.perform(get("/api/dashboard/order-statistics").sessionAttr(SessionConst.LOGIN_USER, admin))
                .andExpect(status().isOk()).andExpect(jsonPath("$.from").value(LocalDate.now().withDayOfMonth(1).toString()))
                .andExpect(jsonPath("$.to").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.salesOrders[0].totalAmount").value(0));
        mvc.perform(get("/").sessionAttr(SessionConst.LOGIN_USER, writer))
                .andExpect(status().isOk()).andExpect(content().string(not(containsString("발주·수주 통계"))));
    }

    @Test
    void permissionsAndInvalidDatesProduceConsistentErrors() throws Exception {
        for (String path : new String[]{"/api/admin/sales-orders", "/api/dashboard/order-statistics"}) {
            mvc.perform(get(path).servletPath(path)).andExpect(status().isUnauthorized());
            mvc.perform(get(path).sessionAttr(SessionConst.LOGIN_USER, writer)).andExpect(status().isForbidden());
            mvc.perform(get(path).param("from", "2026-06-16").param("to", "2026-06-15")
                            .sessionAttr(SessionConst.LOGIN_USER, manager))
                    .andExpect(status().isBadRequest()).andExpect(jsonPath("$.code").value("INVALID_DATE_RANGE"));
            mvc.perform(get(path).param("from", "invalid").sessionAttr(SessionConst.LOGIN_USER, manager))
                    .andExpect(status().isBadRequest()).andExpect(jsonPath("$.code").value("INVALID_INPUT"));
        }
        mvc.perform(get("/admin/sales-orders").sessionAttr(SessionConst.LOGIN_USER, writer))
                .andExpect(status().isForbidden());
        mvc.perform(get("/admin/sales-orders").param("status", "UNKNOWN").sessionAttr(SessionConst.LOGIN_USER, manager))
                .andExpect(status().isBadRequest());
    }

    private LoginUser login(String email) {
        User user = users.findByEmail(email).orElseThrow();
        return new LoginUser(user.getId(), user.getName(), user.getEmail(), user.getRole());
    }

    private SalesOrder order(String number, LocalDate day, Long partnerId) {
        SalesOrder order = new SalesOrder(number, partnerId, writer.id(), day, null);
        order.addLine(SalesOrderLine.builder().itemId(item.getId()).quantity(1).unitPrice(new BigDecimal("12.34")).build());
        return order;
    }
}
