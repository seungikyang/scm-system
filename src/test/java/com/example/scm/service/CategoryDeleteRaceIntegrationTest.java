package com.example.scm.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.scm.common.auth.LoginUser;
import com.example.scm.common.exception.BusinessException;
import com.example.scm.common.exception.ErrorCode;
import com.example.scm.domain.Category;
import com.example.scm.domain.Item;
import com.example.scm.domain.enums.ItemStatus;
import com.example.scm.domain.enums.UserRole;
import com.example.scm.repository.CategoryRepository;
import com.example.scm.repository.ItemRepository;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * 품목을 분류에 저장하는 중에 다른 관리자가 그 분류를 삭제하는 상황을 검증한다.
 * 잠금이 없으면 삭제가 "소속 품목 0건"으로 판단해 분류를 지우고, 품목은 없는 분류를 가리키게 된다.
 */
@SpringBootTest
@DisplayName("분류 삭제와 품목 저장 경합 통합 테스트")
class CategoryDeleteRaceIntegrationTest {

    private static final LoginUser ADMIN = new LoginUser(1L, "관리자", "admin@scm.com", UserRole.ADMIN);

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private PlatformTransactionManager transactionManager;

    @Test
    @DisplayName("품목 저장이 진행 중인 분류의 삭제는 저장이 끝날 때까지 기다린 뒤 CATEGORY_HAS_ITEMS로 거부한다")
    void delete_waitsForInFlightItemSave() throws Exception {
        String suffix = UUID.randomUUID().toString().substring(0, 12);
        Long categoryId = categoryRepository.save(
                Category.builder().name("경합 분류 " + suffix).build()).getId();

        TransactionTemplate tx = new TransactionTemplate(transactionManager);
        CountDownLatch categoryLocked = new CountDownLatch(1);
        CountDownLatch finishItemSave = new CountDownLatch(1);
        ExecutorService pool = Executors.newFixedThreadPool(2);
        try {
            // 품목 저장 쪽: ItemService와 같은 방식으로 분류를 잠근 채 품목을 넣고, 신호가 올 때까지 끝내지 않는다.
            Future<?> itemSave = pool.submit(() -> tx.executeWithoutResult(status -> {
                categoryRepository.findByIdForShare(categoryId).orElseThrow();
                itemRepository.saveAndFlush(Item.builder()
                        .itemCode("RACE-" + suffix).name("경합 품목").categoryId(categoryId).unit("EA")
                        .unitPrice(new BigDecimal("1.00")).safetyStock(0).status(ItemStatus.ACTIVE).build());
                categoryLocked.countDown();
                await(finishItemSave);
            }));
            assertThat(categoryLocked.await(10, TimeUnit.SECONDS)).isTrue();

            // 삭제 쪽: 품목 저장이 아직 끝나지 않았으므로 분류 잠금을 기다려야 한다.
            Future<?> delete = pool.submit(() -> categoryService.delete(categoryId, ADMIN));
            Thread.sleep(500);
            assertThat(delete.isDone()).as("삭제는 품목 저장이 끝나기 전에 완료되면 안 된다").isFalse();

            finishItemSave.countDown();
            itemSave.get(10, TimeUnit.SECONDS);

            assertThatThrownBy(() -> delete.get(10, TimeUnit.SECONDS))
                    .cause()
                    .isInstanceOfSatisfying(BusinessException.class,
                            e -> assertThat(e.getErrorCode()).isEqualTo(ErrorCode.CATEGORY_HAS_ITEMS));
        } finally {
            finishItemSave.countDown();
            pool.shutdownNow();
        }

        // 분류와 품목이 모두 남아 있어야 한다(없는 분류를 가리키는 품목이 생기지 않는다).
        assertThat(categoryRepository.existsById(categoryId)).isTrue();
        assertThat(itemRepository.countByCategoryId(categoryId)).isEqualTo(1);
    }

    private void await(CountDownLatch latch) {
        try {
            latch.await(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
