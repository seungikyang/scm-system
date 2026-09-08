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

/**
 * 카테고리 조회와 등록·수정·삭제 규칙을 담당하는 서비스.
 *
 * <p>학습 모듈 24: create → list/detail → update → delete 순서로 읽고, delete에서
 * ItemRepository가 필요한 이유를 설명한다.</p>
 *
 * <p>카테고리는 품목이 참조하는 기준 정보이므로, 소속 품목이 하나라도 있으면 삭제를
 * 막는다. 컨트롤러 종류와 관계없이 변경 작업은 ADMIN만 실행할 수 있도록 이 계층에서
 * 권한을 다시 확인한다.</p>
 */
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ItemRepository itemRepository;

    // ===== 모듈 24-1: 등록 (ADMIN only) =====

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

    // ===== 모듈 24-2: 목록과 상세 조회 =====

    @Transactional(readOnly = true)
    public List<CategoryView> list() {
        // 카테고리 엔티티와 소속 품목 수를 화면/API 전용 DTO 하나로 합친다.
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

    // ===== 모듈 24-3: 수정과 삭제 (ADMIN only) =====

    @Transactional
    public void update(Long categoryId, CategoryForm form, LoginUser loginUser) {
        Authz.requireRole(loginUser, UserRole.ADMIN);
        Category category = getEntity(categoryId);
        // 이름이 실제로 바뀔 때만 중복 조회를 수행한다.
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
        // DB 외래 키 오류에 맡기지 않고 사용자가 이해할 수 있는 업무 오류로 알려 준다.
        if (itemRepository.countByCategoryId(categoryId) > 0) {
            throw new BusinessException(ErrorCode.CATEGORY_HAS_ITEMS);
        }
        categoryRepository.delete(category);
    }

    // ===== 대시보드 집계 =====

    @Transactional(readOnly = true)
    public long countAll() {
        return categoryRepository.count();
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

}
