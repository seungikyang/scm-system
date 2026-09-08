package com.example.scm.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.example.scm.common.auth.LoginUser;
import com.example.scm.common.auth.SessionConst;
import com.example.scm.domain.enums.UserRole;
import com.example.scm.repository.PurchaseOrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("HTTP 요청 입력 검증 통합 테스트")
class RequestValidationIntegrationTest {

    private static final LoginUser USER = new LoginUser(3L, "사용자", "user@scm.com", UserRole.USER);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @ParameterizedTest
    @ValueSource(strings = {"/api/items/abc", "/api/purchase-orders/my?status=UNKNOWN",
            "/api/items?categoryId=abc"})
    @DisplayName("경로·검색 조건의 형식 오류를 400 INVALID_INPUT으로 응답한다")
    void invalidApiParameter_returnsBadRequest(String path) throws Exception {
        mockMvc.perform(get(path).sessionAttr(SessionConst.LOGIN_USER, USER))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_INPUT"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"{", "", "{\"email\":\"user@scm.com\",\"password\":{}}"})
    @DisplayName("깨진 JSON·누락 본문·잘못된 JSON 타입을 입력 오류로 응답한다")
    void unreadableJson_returnsBadRequest(String body) throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_INPUT"));
    }

    @Test
    @DisplayName("null 발주 행은 저장 전에 검증 오류로 거부한다")
    void nullOrderLine_doesNotPersistOrder() throws Exception {
        long countBefore = purchaseOrderRepository.count();

        mockMvc.perform(post("/api/purchase-orders")
                        .sessionAttr(SessionConst.LOGIN_USER, USER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"partnerId":1,"orderDate":"2026-09-06","lines":[null]}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_INPUT"));

        assertThat(purchaseOrderRepository.count()).isEqualTo(countBefore);
    }

    @Test
    @DisplayName("발주 행 내부의 필수 품목도 중첩 검증으로 거부한다")
    void missingItemInOrderLine_returnsFieldError() throws Exception {
        mockMvc.perform(post("/api/purchase-orders")
                        .sessionAttr(SessionConst.LOGIN_USER, USER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"partnerId":1,"orderDate":"2026-09-06","lines":[{"quantity":1}]}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_INPUT"))
                .andExpect(jsonPath("$.message", containsString("lines[0].itemId")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"/items/abc", "/purchase-orders/my?status=UNKNOWN",
            "/items?categoryId=abc"})
    @DisplayName("화면의 경로·검색 조건 오류는 HTTP 400 오류 화면으로 표시한다")
    void invalidWebParameter_returnsBadRequestView(String path) throws Exception {
        mockMvc.perform(get(path).sessionAttr(SessionConst.LOGIN_USER, USER))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("error"))
                .andExpect(model().attribute("code", "INVALID_INPUT"));
    }
}
