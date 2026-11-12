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
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 물건을 문구류, 전자제품처럼 묶는 분류를 관리합니다.
 * 분류에 속한 물건이 남아 있으면 분류를 지울 수 없도록 검사합니다.
 *
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
        String name = trimmed(form.getName());
        validateNameUnique(name);
        Category category = Category.builder()
                .name(name)
                .description(form.getDescription())
                .build();
        return flushUniqueName(() -> categoryRepository.saveAndFlush(category)).getId();
    }

    @Transactional
    public Long create(CategoryCreateRequest request, LoginUser loginUser) {
        Authz.requireRole(loginUser, UserRole.ADMIN);
        String name = trimmed(request.getName());
        validateNameUnique(name);
        Category category = Category.builder()
                .name(name)
                .description(request.getDescription())
                .build();
        return flushUniqueName(() -> categoryRepository.saveAndFlush(category)).getId();
    }

    // ===== 모듈 24-2: 목록과 상세 조회 =====

    @Transactional(readOnly = true)
    public List<CategoryView> list() {
        // 카테고리 엔티티와 소속 품목 수를 화면/API 전용 DTO 하나로 합친다.
        // stream은 목록을 하나씩 살펴보는 도구입니다. map은 각 분류를 화면에 쓸 정보로 바꿉니다.
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
        String name = trimmed(form.getName());
        validateNameUniqueExcept(name, categoryId);
        category.update(name, form.getDescription());
        flushUniqueName(() -> {
            categoryRepository.flush();
            return category;
        });
    }

    @Transactional
    public void update(Long categoryId, CategoryUpdateRequest request, LoginUser loginUser) {
        Authz.requireRole(loginUser, UserRole.ADMIN);
        Category category = getEntity(categoryId);
        String name = trimmed(request.getName());
        validateNameUniqueExcept(name, categoryId);
        category.update(name, request.getDescription());
        flushUniqueName(() -> {
            categoryRepository.flush();
            return category;
        });
    }

    @Transactional
    public void delete(Long categoryId, LoginUser loginUser) {
        Authz.requireRole(loginUser, UserRole.ADMIN);
        // 분류를 먼저 잠급니다. 같은 분류에 품목을 저장 중인 작업(ItemService의 findByIdForShare)이 있으면
        // 그 작업이 끝날 때까지 기다린 뒤 품목 수를 세므로, 방금 저장된 품목을 놓치지 않습니다.
        Category category = categoryRepository.findByIdForUpdate(categoryId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));
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

    // existsByName 확인과 저장 사이에 다른 요청이 같은 이름을 먼저 저장할 수 있습니다.
    // 메서드 안에서 바로 flush해 DB unique 제약 위반을 여기서 잡고, 서버 오류(500) 대신
    // 중복 이름 오류로 바꿔 알려 줍니다. 예외가 나면 트랜잭션 전체가 롤백됩니다.
    private Category flushUniqueName(Supplier<Category> write) {
        try {
            return write.get();
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException(ErrorCode.DUPLICATE_CATEGORY_NAME);
        }
    }

    private void validateNameUnique(String name) {
        if (categoryRepository.existsByName(name)) {
            throw new BusinessException(ErrorCode.DUPLICATE_CATEGORY_NAME);
        }
    }

    // 수정할 때는 자기 자신을 뺀 나머지 분류에서만 같은 이름을 찾습니다.
    // MySQL은 대소문자를 구분하지 않고 비교하므로, 자신을 빼지 않으면 "abc"를 "ABC"로
    // 바꾸는 수정이 자기 이름과 겹친다고 판단되어 항상 중복 오류가 납니다.
    private void validateNameUniqueExcept(String name, Long categoryId) {
        if (categoryRepository.existsByNameAndIdNot(name, categoryId)) {
            throw new BusinessException(ErrorCode.DUPLICATE_CATEGORY_NAME);
        }
    }

    // 앞뒤 공백만 다른 값(" 전자부품 "과 "전자부품")이 서로 다른 자료로 등록되지 않도록
    // 이름·코드처럼 사람을 구별하는 값은 저장하고 비교하기 전에 앞뒤 공백을 지웁니다.
    private static String trimmed(String value) {
        return value == null ? null : value.trim();
    }

}
