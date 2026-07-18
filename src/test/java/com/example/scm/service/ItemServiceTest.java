package com.example.scm.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.example.scm.common.auth.LoginUser;
import com.example.scm.common.exception.BusinessException;
import com.example.scm.common.exception.ErrorCode;
import com.example.scm.domain.Item;
import com.example.scm.domain.enums.ItemStatus;
import com.example.scm.domain.enums.UserRole;
import com.example.scm.dto.item.ItemForm;
import com.example.scm.repository.CategoryRepository;
import com.example.scm.repository.ItemRepository;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
@DisplayName("ItemService 단위 테스트")
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ItemService service;

    private LoginUser admin;
    private LoginUser user;

    @BeforeEach
    void setUp() {
        admin = new LoginUser(1L, "관리자", "admin@scm.com", UserRole.ADMIN);
        user = new LoginUser(10L, "사용자", "user@scm.com", UserRole.USER);
    }

    @Test
    @DisplayName("ADMIN은 중복되지 않은 코드와 존재하는 카테고리로 활성 품목을 등록한다")
    void create_success() {
        given(itemRepository.existsByItemCode("ITM-100")).willReturn(false);
        given(categoryRepository.existsById(2L)).willReturn(true);
        given(itemRepository.save(any(Item.class))).willAnswer(invocation -> {
            Item item = invocation.getArgument(0);
            ReflectionTestUtils.setField(item, "id", 7L);
            return item;
        });

        Long itemId = service.create(form(), admin);

        assertThat(itemId).isEqualTo(7L);
    }

    @Test
    @DisplayName("중복 품목코드는 저장 전에 차단한다")
    void create_duplicateItemCode() {
        given(itemRepository.existsByItemCode("ITM-100")).willReturn(true);

        assertCode(() -> service.create(form(), admin), ErrorCode.DUPLICATE_ITEM_CODE);
        verify(itemRepository, never()).save(any());
    }

    @Test
    @DisplayName("존재하지 않는 카테고리로 품목을 등록할 수 없다")
    void create_categoryNotFound() {
        given(itemRepository.existsByItemCode("ITM-100")).willReturn(false);
        given(categoryRepository.existsById(2L)).willReturn(false);

        assertCode(() -> service.create(form(), admin), ErrorCode.CATEGORY_NOT_FOUND);
        verify(itemRepository, never()).save(any());
    }

    @Test
    @DisplayName("일반 사용자는 품목을 등록할 수 없다")
    void create_byUserDenied() {
        assertCode(() -> service.create(form(), user), ErrorCode.ACCESS_DENIED);
        verify(itemRepository, never()).existsByItemCode(any());
    }

    @Test
    @DisplayName("품목 수정 시 불변 코드와 상태를 보존하고 변경 가능 필드만 바꾼다")
    void update_preservesCodeAndStatus() {
        Item item = item(7L);
        ItemForm form = form();
        form.setName("변경 품목");
        form.setUnitPrice(new BigDecimal("2500.00"));
        given(categoryRepository.existsById(2L)).willReturn(true);
        given(itemRepository.findById(7L)).willReturn(Optional.of(item));

        service.update(7L, form, admin);

        assertThat(item.getItemCode()).isEqualTo("ITM-100");
        assertThat(item.getName()).isEqualTo("변경 품목");
        assertThat(item.getUnitPrice()).isEqualByComparingTo("2500.00");
        assertThat(item.getStatus()).isEqualTo(ItemStatus.ACTIVE);
    }

    @Test
    @DisplayName("품목 삭제 요청은 과거 발주 이력을 보존하는 단종 처리다")
    void discontinue_success() {
        Item item = item(7L);
        given(itemRepository.findById(7L)).willReturn(Optional.of(item));

        service.discontinue(7L, admin);

        assertThat(item.getStatus()).isEqualTo(ItemStatus.DISCONTINUED);
    }

    private ItemForm form() {
        ItemForm form = new ItemForm();
        form.setItemCode("ITM-100");
        form.setName("테스트 품목");
        form.setCategoryId(2L);
        form.setUnit("EA");
        form.setUnitPrice(new BigDecimal("1000.00"));
        form.setSafetyStock(10);
        return form;
    }

    private Item item(Long id) {
        Item item = Item.builder()
                .itemCode("ITM-100")
                .name("테스트 품목")
                .categoryId(2L)
                .unit("EA")
                .unitPrice(new BigDecimal("1000.00"))
                .safetyStock(10)
                .status(ItemStatus.ACTIVE)
                .build();
        ReflectionTestUtils.setField(item, "id", id);
        return item;
    }

    private void assertCode(org.junit.jupiter.api.function.Executable executable, ErrorCode expected) {
        assertThatThrownBy(executable::execute)
                .isInstanceOf(BusinessException.class)
                .extracting(error -> ((BusinessException) error).getErrorCode())
                .isEqualTo(expected);
    }
}
