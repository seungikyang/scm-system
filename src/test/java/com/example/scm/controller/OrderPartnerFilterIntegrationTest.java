package com.example.scm.controller;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.scm.common.auth.LoginUser;
import com.example.scm.common.auth.SessionConst;
import com.example.scm.domain.*;
import com.example.scm.domain.enums.*;
import com.example.scm.repository.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class OrderPartnerFilterIntegrationTest {
    @Autowired MockMvc mvc;
    @Autowired PartnerRepository partners;
    @Autowired PurchaseOrderRepository purchases;
    @Autowired SalesOrderRepository sales;
    @Autowired ItemRepository items;
    @Autowired UserRepository users;
    LoginUser admin;
    Item item;

    @BeforeEach
    void setup() {
        User user = users.findByEmail("admin@scm.com").orElseThrow();
        admin = new LoginUser(user.getId(), user.getName(), user.getEmail(), user.getRole());
        item = items.findByStatusOrderByItemCodeAsc(ItemStatus.ACTIVE).get(0);
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void purchaseHistoryKeepsPartnerFilterAfterDeactivationOrTypeChange(boolean changeType) throws Exception {
        Partner partner = partner(PartnerType.SUPPLIER);
        PurchaseOrder order = PurchaseOrder.builder().orderNumber("PO-20261021-9881")
                .partnerId(partner.getId()).writerId(admin.id()).orderDate(LocalDate.now())
                .totalAmount(BigDecimal.TEN).build();
        order.addLine(PurchaseOrderLine.builder().itemId(item.getId()).quantity(1)
                .unitPrice(BigDecimal.TEN).build());
        purchases.saveAndFlush(order);
        changePartner(partner, changeType, PartnerType.CUSTOMER);

        assertHistoricalFilter("/admin/purchase-orders", partner);
        assertNotAvailableForNewOrder("/purchase-orders/new", partner);
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void salesHistoryKeepsPartnerFilterAfterDeactivationOrTypeChange(boolean changeType) throws Exception {
        Partner partner = partner(PartnerType.CUSTOMER);
        SalesOrder order = new SalesOrder("SO-20261021-9881", partner.getId(), admin.id(), LocalDate.now(), null);
        order.addLine(SalesOrderLine.builder().itemId(item.getId()).quantity(1)
                .unitPrice(BigDecimal.TEN).build());
        sales.saveAndFlush(order);
        changePartner(partner, changeType, PartnerType.SUPPLIER);

        assertHistoricalFilter("/admin/sales-orders", partner);
        assertNotAvailableForNewOrder("/sales-orders/new", partner);
    }

    @ParameterizedTest
    @CsvSource({"/admin/purchase-orders,CUSTOMER", "/admin/sales-orders,SUPPLIER"})
    void filtersExcludeUnrelatedPartnersWithoutOrderHistory(String path, PartnerType type) throws Exception {
        Partner partner = partner(type);
        mvc.perform(get(path).sessionAttr(SessionConst.LOGIN_USER, admin))
                .andExpect(status().isOk())
                .andExpect(model().attribute("partners", not(hasItem(hasProperty("id", is(partner.getId()))))));
    }

    private Partner partner(PartnerType type) {
        return partners.saveAndFlush(Partner.builder().name("과거 주문 검색 거래처")
                .businessNumber("987-65-43210").partnerType(type).build());
    }

    private void changePartner(Partner partner, boolean changeType, PartnerType newType) {
        if (changeType) partner.update(partner.getName(), newType, null, null, null, null);
        else partner.deactivate();
        partners.flush();
    }

    private void assertHistoricalFilter(String path, Partner partner) throws Exception {
        mvc.perform(get(path).param("partnerId", partner.getId().toString())
                        .sessionAttr(SessionConst.LOGIN_USER, admin))
                .andExpect(status().isOk())
                .andExpect(model().attribute("partners", hasItem(hasProperty("id", is(partner.getId())))))
                .andExpect(model().attribute("selectedPartnerId", partner.getId()))
                .andExpect(model().attribute("orders", hasProperty("totalElements", is(1L))))
                .andExpect(content().string(containsString("<option value=\"" + partner.getId()
                        + "\" selected=\"selected\">")));
    }

    private void assertNotAvailableForNewOrder(String path, Partner partner) throws Exception {
        mvc.perform(get(path).sessionAttr(SessionConst.LOGIN_USER, admin))
                .andExpect(status().isOk())
                .andExpect(model().attribute("partners", not(hasItem(hasProperty("id", is(partner.getId()))))));
    }
}
