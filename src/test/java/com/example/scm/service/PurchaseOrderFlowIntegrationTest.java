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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@DisplayName("발주 전체 흐름 통합 테스트")
class PurchaseOrderFlowIntegrationTest {

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
    @DisplayName("독립 저장 트랜잭션으로 작성하고 품목 잠금 후 입고 재고를 반영한다")
    void createSubmitApproveReceive() {
        User writerEntity = userRepository.findByEmail("user@scm.com").orElseThrow();
        User adminEntity = userRepository.findByEmail("admin@scm.com").orElseThrow();
        LoginUser writer = loginUser(writerEntity);
        LoginUser admin = loginUser(adminEntity);
        Partner supplier = partnerRepository.findAll().stream()
                .filter(Partner::canSupply)
                .filter(Partner::isActive)
                .findFirst()
                .orElseThrow();
        Item item = itemRepository.findByStatusOrderByItemCodeAsc(ItemStatus.ACTIVE).get(0);

        PurchaseOrderCreateRequest.LineRequest line = new PurchaseOrderCreateRequest.LineRequest();
        line.setItemId(item.getId());
        line.setQuantity(7);

        PurchaseOrderCreateRequest request = new PurchaseOrderCreateRequest();
        request.setPartnerId(supplier.getId());
        request.setOrderDate(LocalDate.now());
        request.setDueDate(LocalDate.now().plusDays(3));
        request.setLines(List.of(line));

        PurchaseOrderCreateResponse created = purchaseOrderService.create(request, writer);
        purchaseOrderService.submit(created.getPurchaseOrderId(), writer);
        purchaseOrderService.approve(created.getPurchaseOrderId(), admin);
        purchaseOrderService.receive(created.getPurchaseOrderId(), admin);

        assertThat(stockRepository.findByItemId(item.getId())).isPresent()
                .get()
                .extracting(stock -> stock.getQuantity())
                .isEqualTo(7);
    }

    private LoginUser loginUser(User user) {
        return new LoginUser(user.getId(), user.getName(), user.getEmail(), user.getRole());
    }
}
