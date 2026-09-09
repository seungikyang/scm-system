package com.example.scm.controller;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.scm.common.auth.LoginUser;
import com.example.scm.common.auth.SessionConst;
import com.example.scm.domain.Item;
import com.example.scm.domain.PurchaseOrder;
import com.example.scm.domain.PurchaseOrderLine;
import com.example.scm.domain.enums.UserRole;
import com.example.scm.repository.ItemRepository;
import com.example.scm.repository.PurchaseOrderRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DisplayName("소수 금액 화면 표시 통합 테스트")
class MoneyViewIntegrationTest {

    private static final LoginUser ADMIN = new LoginUser(1L, "관리자", "admin@scm.com", UserRole.ADMIN);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @ParameterizedTest
    @ValueSource(strings = {"0.25", "1234.56"})
    @DisplayName("품목·카테고리·발주 화면은 저장된 소수 금액을 반올림 없이 표시한다")
    void moneyViews_preserveTwoDecimalPlaces(String priceText) throws Exception {
        BigDecimal price = new BigDecimal(priceText);
        BigDecimal total = price.multiply(BigDecimal.valueOf(3));
        Item item = itemRepository.saveAndFlush(Item.builder()
                .itemCode("MONEY-VIEW-TEST").name("소수 단가 검증 품목")
                .categoryId(1L).unit("EA").unitPrice(price).safetyStock(0).build());
        PurchaseOrder order = PurchaseOrder.builder()
                .orderNumber("PO-MONEY-VIEW-TEST").partnerId(1L).writerId(ADMIN.id())
                .orderDate(LocalDate.of(2026, 9, 6)).totalAmount(total).build();
        order.addLine(PurchaseOrderLine.builder()
                .itemId(item.getId()).quantity(3).unitPrice(price).build());
        purchaseOrderRepository.saveAndFlush(order);

        for (String path : List.of("/items/" + item.getId(), "/items?keyword=MONEY-VIEW-TEST",
                "/categories/1", "/purchase-orders/" + order.getId())) {
            expectDisplayedAmount(path, price);
        }
        for (String path : List.of("/purchase-orders/" + order.getId(),
                "/purchase-orders/my", "/admin/purchase-orders")) {
            expectDisplayedAmount(path, total);
        }
    }

    private void expectDisplayedAmount(String path, BigDecimal amount) throws Exception {
        mockMvc.perform(get(path).sessionAttr(SessionConst.LOGIN_USER, ADMIN))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(
                        ">" + String.format(Locale.US, "%,.2f", amount) + "<")));
    }
}
