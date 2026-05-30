package com.example.scm.service;

import com.example.scm.common.auth.Authz;
import com.example.scm.common.auth.LoginUser;
import com.example.scm.common.exception.BusinessException;
import com.example.scm.common.exception.ErrorCode;
import com.example.scm.domain.Category;
import com.example.scm.domain.Item;
import com.example.scm.domain.enums.ItemStatus;
import com.example.scm.domain.enums.UserRole;
import com.example.scm.dto.item.ItemCreateRequest;
import com.example.scm.dto.item.ItemDetailView;
import com.example.scm.dto.item.ItemForm;
import com.example.scm.dto.item.ItemListView;
import com.example.scm.dto.item.ItemSearchForm;
import com.example.scm.dto.item.ItemUpdateRequest;
import com.example.scm.repository.CategoryRepository;
import com.example.scm.repository.ItemRepository;
import com.example.scm.repository.spec.ItemSpecs;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;

    // ===== 조회 =====

    @Transactional(readOnly = true)
    public Page<ItemListView> search(ItemSearchForm form, Pageable pageable) {
        Page<Item> page = itemRepository.findAll(ItemSpecs.search(form), pageable);
        Map<Long, String> categoryNames = categoryRepository.findAll().stream()
                .collect(Collectors.toMap(Category::getId, Category::getName));
        return page.map(item ->
                ItemListView.from(item, categoryNames.get(item.getCategoryId())));
    }

    @Transactional(readOnly = true)
    public ItemDetailView getDetail(Long itemId) {
        Item item = getEntity(itemId);
        return ItemDetailView.from(item, resolveCategoryName(item.getCategoryId()));
    }

    @Transactional(readOnly = true)
    public ItemForm getForm(Long itemId) {
        return ItemForm.from(getEntity(itemId));
    }

    // ===== 변경 (ADMIN only) =====

    @Transactional
    public Long create(ItemForm form, LoginUser loginUser) {
        Authz.requireRole(loginUser, UserRole.ADMIN);
        validateItemCodeUnique(form.getItemCode());
        validateCategoryExists(form.getCategoryId());

        Item item = Item.builder()
                .itemCode(form.getItemCode())
                .name(form.getName())
                .categoryId(form.getCategoryId())
                .unit(form.getUnit())
                .unitPrice(form.getUnitPrice())
                .safetyStock(form.getSafetyStock())
                .status(ItemStatus.ACTIVE)
                .build();
        return itemRepository.save(item).getId();
    }

    @Transactional
    public Long create(ItemCreateRequest request, LoginUser loginUser) {
        Authz.requireRole(loginUser, UserRole.ADMIN);
        validateItemCodeUnique(request.getItemCode());
        validateCategoryExists(request.getCategoryId());

        Item item = Item.builder()
                .itemCode(request.getItemCode())
                .name(request.getName())
                .categoryId(request.getCategoryId())
                .unit(request.getUnit())
                .unitPrice(request.getUnitPrice())
                .safetyStock(request.getSafetyStock())
                .status(ItemStatus.ACTIVE)
                .build();
        return itemRepository.save(item).getId();
    }

    @Transactional
    public void update(Long itemId, ItemForm form, LoginUser loginUser) {
        Authz.requireRole(loginUser, UserRole.ADMIN);
        validateCategoryExists(form.getCategoryId());
        Item item = getEntity(itemId);
        item.update(form.getName(), form.getCategoryId(), form.getUnit(),
                form.getUnitPrice(), form.getSafetyStock());
    }

    @Transactional
    public void update(Long itemId, ItemUpdateRequest request, LoginUser loginUser) {
        Authz.requireRole(loginUser, UserRole.ADMIN);
        validateCategoryExists(request.getCategoryId());
        Item item = getEntity(itemId);
        item.update(request.getName(), request.getCategoryId(), request.getUnit(),
                request.getUnitPrice(), request.getSafetyStock());
    }

    @Transactional
    public void discontinue(Long itemId, LoginUser loginUser) {
        Authz.requireRole(loginUser, UserRole.ADMIN);
        Item item = getEntity(itemId);
        item.discontinue();
    }

    // ===== 내부 헬퍼 =====

    private Item getEntity(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ITEM_NOT_FOUND));
    }

    private void validateItemCodeUnique(String itemCode) {
        if (itemRepository.existsByItemCode(itemCode)) {
            throw new BusinessException(ErrorCode.DUPLICATE_ITEM_CODE);
        }
    }

    private void validateCategoryExists(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new BusinessException(ErrorCode.CATEGORY_NOT_FOUND);
        }
    }

    private String resolveCategoryName(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .map(Category::getName)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public long countAll() {
        return itemRepository.count();
    }
}
