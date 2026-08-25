package com.example.scm.controller;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.example.scm.common.auth.LoginUser;
import com.example.scm.common.auth.SessionConst;
import com.example.scm.domain.enums.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@DisplayName("재고 조회 수직 슬라이스 통합 테스트")
class StockFeatureIntegrationTest {

    private static final LoginUser USER =
            new LoginUser(3L, "홍길동", "user@scm.com", UserRole.USER);

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("일반 사용자도 재고 화면에서 운영 품목과 부족 상태를 확인한다")
    void webListShowsActiveStockRows() throws Exception {
        mockMvc.perform(get("/stocks").sessionAttr(SessionConst.LOGIN_USER, USER))
                .andExpect(status().isOk())
                .andExpect(view().name("stock/list"))
                .andExpect(content().string(containsString("재고 현황")))
                .andExpect(content().string(containsString("ITM-001")))
                .andExpect(content().string(containsString("재고 없음")));
    }

    @Test
    @DisplayName("API는 입고 이력이 없는 품목도 현재고 0과 부족수량으로 반환한다")
    void apiListIncludesZeroStockItems() throws Exception {
        mockMvc.perform(get("/api/stocks")
                        .param("keyword", "ITM-001")
                        .param("lowOnly", "true")
                        .sessionAttr(SessionConst.LOGIN_USER, USER))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].itemCode").value("ITM-001"))
                .andExpect(jsonPath("$.content[0].quantity").value(0))
                .andExpect(jsonPath("$.content[0].safetyStock").value(100))
                .andExpect(jsonPath("$.content[0].shortageQuantity").value(100))
                .andExpect(jsonPath("$.content[0].level").value("OUT_OF_STOCK"));
    }

    @Test
    @DisplayName("요약 API는 운영 품목 전체를 기준으로 현재고와 부족 품목을 집계한다")
    void summaryCountsActiveItemsOnly() throws Exception {
        mockMvc.perform(get("/api/stocks/summary")
                        .sessionAttr(SessionConst.LOGIN_USER, USER))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activeItemCount").value(7))
                .andExpect(jsonPath("$.totalQuantity").value(0))
                .andExpect(jsonPath("$.lowStockCount").value(7));
    }
}
