package com.example.scm.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.example.scm.common.auth.LoginUser;
import com.example.scm.common.auth.SessionConst;
import com.example.scm.domain.enums.UserRole;
import com.example.scm.repository.PurchaseOrderRepository;
import com.example.scm.repository.SalesOrderRepository;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class RequestBoundaryIntegrationTest {

    private static final LoginUser USER = new LoginUser(3L, "사용자", "user@scm.com", UserRole.USER);

    @Autowired private MockMvc mvc;
    @Autowired private PurchaseOrderRepository purchases;
    @Autowired private SalesOrderRepository sales;

    @ParameterizedTest
    @CsvSource({
            "/api/auth/login, application/x-www-form-urlencoded",
            "/api/auth/login, text/plain",
            "/api/auth/login, application/xml",
            "/api/items, application/x-www-form-urlencoded",
            "/api/items, text/plain",
            "/api/items, application/xml"
    })
    void unsupportedRequestContentType_returns415(String path, String contentType) throws Exception {
        mvc.perform(post(path).sessionAttr(SessionConst.LOGIN_USER, USER)
                        .contentType(contentType).content("x=y"))
                .andExpect(status().isUnsupportedMediaType())
                .andExpect(jsonPath("$.code").value("UNSUPPORTED_MEDIA_TYPE"))
                .andExpect(jsonPath("$.status").value(415));
    }

    @ParameterizedTest
    @CsvSource({
            "/purchase-orders, -1", "/purchase-orders, abc", "/purchase-orders, 256",
            "/sales-orders, -1", "/sales-orders, abc", "/sales-orders, 256"
    })
    void malformedOrderLineIndex_returns400WithoutSaving(String path, String index) throws Exception {
        long purchaseCount = purchases.count();
        long salesCount = sales.count();

        mvc.perform(post(path).with(csrf()).sessionAttr(SessionConst.LOGIN_USER, USER)
                        .param("partnerId", "1").param("orderDate", "2026-10-23")
                        .param("lines[" + index + "].itemId", "1")
                        .param("lines[" + index + "].quantity", "1"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("error"))
                .andExpect(model().attribute("code", "INVALID_INPUT"));

        assertThat(purchases.count()).isEqualTo(purchaseCount);
        assertThat(sales.count()).isEqualTo(salesCount);
    }
}
