package com.example.scm.service;

import com.example.scm.common.auth.Authz;
import com.example.scm.common.auth.LoginUser;
import com.example.scm.common.exception.BusinessException;
import com.example.scm.common.exception.ErrorCode;
import com.example.scm.domain.Category;
import com.example.scm.domain.enums.UserRole;
import com.example.scm.dto.category.CategoryCreateRequest;
import com.example.scm.dto.category.CategoryDetailView;
import com.example.scm.dto.category.CategoryForm;
import com.example.scm.dto.category.CategoryUpdateRequest;
import com.example.scm.dto.category.CategoryView;
import com.example.scm.dto.item.ItemListView;
import com.example.scm.repository.CategoryRepository;
import com.example.scm.repository.ItemRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ItemRepository itemRepository;

    // ===== 조회 =====

    @Transactional(readOnly = true)
    public List<CategoryView> list() {
        return categoryRepository.findAllByOrderByNameAsc().stream()
                .map(c -> CategoryView.from(c, itemRepository.countByCategoryId(c.getId())))
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoryDetailView getDetail(Long categoryId) {
        Category category = getEntity(categoryId);
        List<ItemListView> items = itemRepository.findByCategoryId(categoryId).stream()
                .map(item -> ItemListView.from(item, category.getName()))
                .toList();
        return CategoryDetailView.from(category, items);
    }

    @Transactional(readOnly = true)
    public CategoryView getView(Long categoryId) {
        Category category = getEntity(categoryId);
        return CategoryView.from(category, itemRepository.countByCategoryId(categoryId));
    }

    @Transactional(readOnly = true)
    public CategoryForm getForm(Long categoryId) {
        return CategoryForm.from(getEntity(categoryId));
    }

    // ===== 변경 (ADMIN only) =====

    @Transactional
    public Long create(CategoryForm form, LoginUser loginUser) {
        Authz.requireRole(loginUser, UserRole.ADMIN);
        validateNameUnique(form.getName());
        Category category = Category.builder()
                .name(form.getName())
                .description(form.getDescription())
                .build();
        return categoryRepository.save(category).getId();
    }

    @Transactional
    public Long create(CategoryCreateRequest request, LoginUser loginUser) {
        Authz.requireRole(loginUser, UserRole.ADMIN);
        validateNameUnique(request.getName());
        Category category = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
        return categoryRepository.save(category).getId();
    }

    @Transactional
    public void update(Long categoryId, CategoryForm form, LoginUser loginUser) {
        Authz.requireRole(loginUser, UserRole.ADMIN);
        Category category = getEntity(categoryId);
        if (!category.getName().equals(form.getName())) {
            validateNameUnique(form.getName());
        }
        category.update(form.getName(), form.getDescription());
    }

    @Transactional
    public void update(Long categoryId, CategoryUpdateRequest request, LoginUser loginUser) {
        Authz.requireRole(loginUser, UserRole.ADMIN);
        Category category = getEntity(categoryId);
        if (!category.getName().equals(request.getName())) {
            validateNameUnique(request.getName());
        }
        category.update(request.getName(), request.getDescription());
    }

    @Transactional
    public void delete(Long categoryId, LoginUser loginUser) {
        Authz.requireRole(loginUser, UserRole.ADMIN);
        Category category = getEntity(categoryId);
        if (itemRepository.countByCategoryId(categoryId) > 0) {
            throw new BusinessException(ErrorCode.CATEGORY_HAS_ITEMS);
        }
        categoryRepository.delete(category);
    }

    // ===== 내부 헬퍼 =====

    private Category getEntity(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    private void validateNameUnique(String name) {
        if (categoryRepository.existsByName(name)) {
            throw new BusinessException(ErrorCode.DUPLICATE_CATEGORY_NAME);
        }
    }

    @Transactional(readOnly = true)
    public long countAll() {
        return categoryRepository.count();
    }
}
