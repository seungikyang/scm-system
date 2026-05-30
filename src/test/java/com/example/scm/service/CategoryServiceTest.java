package com.example.scm.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.example.scm.common.auth.LoginUser;
import com.example.scm.common.exception.BusinessException;
import com.example.scm.common.exception.ErrorCode;
import com.example.scm.domain.Category;
import com.example.scm.domain.enums.UserRole;
import com.example.scm.dto.category.CategoryForm;
import com.example.scm.repository.CategoryRepository;
import com.example.scm.repository.ItemRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * 마스터(Category) 핵심 규칙 단위 테스트 — 중복명/CATEGORY_HAS_ITEMS/ADMIN 권한.
 * 진실원: 03_foundation_conventions §4.3, PRD 3.8(마스터 규칙).
 * 실행은 JDK17 환경 권장(환경 제약 동일).
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CategoryService 단위 테스트")
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private CategoryService service;

    private LoginUser admin;
    private LoginUser user;

    @BeforeEach
    void setUp() {
        admin = new LoginUser(1L, "관리자", "admin@scm.com", UserRole.ADMIN);
        user = new LoginUser(10L, "일반", "user@scm.com", UserRole.USER);
    }

    @Test
    @DisplayName("정상 등록: ADMIN, 미중복 → id 반환")
    void create_success() {
        given(categoryRepository.existsByName("전자부품")).willReturn(false);
        given(categoryRepository.save(any(Category.class))).willAnswer(inv -> {
            Category c = inv.getArgument(0);
            ReflectionTestUtils.setField(c, "id", 5L);
            return c;
        });
        CategoryForm form = form("전자부품", "설명");
        Long id = service.create(form, admin);
        org.assertj.core.api.Assertions.assertThat(id).isEqualTo(5L);
    }

    @Test
    @DisplayName("카테고리명 중복 → DUPLICATE_CATEGORY_NAME")
    void create_duplicateName() {
        given(categoryRepository.existsByName("전자부품")).willReturn(true);
        assertCode(() -> service.create(form("전자부품", null), admin),
                ErrorCode.DUPLICATE_CATEGORY_NAME);
        verify(categoryRepository, never()).save(any());
    }

    @Test
    @DisplayName("USER 등록 → ACCESS_DENIED (마스터 등록은 ADMIN만)")
    void create_byUserDenied() {
        assertCode(() -> service.create(form("전자부품", null), user), ErrorCode.ACCESS_DENIED);
        verify(categoryRepository, never()).existsByName(any());
    }

    @Test
    @DisplayName("소속 품목 있는 카테고리 삭제 → CATEGORY_HAS_ITEMS")
    void delete_hasItems() {
        Category category = Category.builder().name("전자부품").build();
        ReflectionTestUtils.setField(category, "id", 5L);
        given(categoryRepository.findById(5L)).willReturn(Optional.of(category));
        given(itemRepository.countByCategoryId(5L)).willReturn(3L);
        assertCode(() -> service.delete(5L, admin), ErrorCode.CATEGORY_HAS_ITEMS);
        verify(categoryRepository, never()).delete(any());
    }

    @Test
    @DisplayName("소속 품목 없는 카테고리 삭제 → 정상")
    void delete_noItems() {
        Category category = Category.builder().name("전자부품").build();
        ReflectionTestUtils.setField(category, "id", 5L);
        given(categoryRepository.findById(5L)).willReturn(Optional.of(category));
        given(itemRepository.countByCategoryId(5L)).willReturn(0L);
        service.delete(5L, admin);
        verify(categoryRepository).delete(category);
    }

    @Test
    @DisplayName("미존재 카테고리 삭제 → CATEGORY_NOT_FOUND")
    void delete_notFound() {
        given(categoryRepository.findById(anyLong())).willReturn(Optional.empty());
        assertCode(() -> service.delete(404L, admin), ErrorCode.CATEGORY_NOT_FOUND);
    }

    // ===== 헬퍼 =====

    private void assertCode(org.junit.jupiter.api.function.Executable exec, ErrorCode expected) {
        assertThatThrownBy(exec::execute)
                .isInstanceOf(BusinessException.class)
                .extracting(e -> ((BusinessException) e).getErrorCode())
                .isEqualTo(expected);
    }

    private CategoryForm form(String name, String description) {
        CategoryForm f = new CategoryForm();
        f.setName(name);
        f.setDescription(description);
        return f;
    }
}
