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

/**
 * 품목 조회와 등록·수정·단종 규칙을 조정하는 서비스 계층.
 *
 * <p>컨트롤러는 HTTP 요청/화면 처리만 맡고, 권한 검사와 중복 검사는 이곳에서 수행한다.
 * 조회 메서드의 {@code readOnly = true}는 읽기 작업의 최적화를 돕는 설정이며 권한 검사가 아니다.
 * 변경 메서드는 트랜잭션 안에서 관리 중인 엔티티 값을 바꾸면 JPA 변경 감지가
 * UPDATE SQL을 실행한다.</p>
 *
 * <p>비슷한 create/update 메서드가 두 개인 이유는 웹 폼 DTO와 REST 요청 DTO가 다르기
 * 때문이다. 어떤 진입 경로든 동일한 권한·검증 규칙을 거친다.</p>
 *
 * <p>학습 순서: 모듈 08의 등록(create) → 09의 검색(search) → 28의 상세·수정·단종
 * 순서로 구역을 배치했다. 한 번에 파일 전체를 읽지 말고 현재 모듈 구역까지만 읽는다.</p>
 */
@Service
@RequiredArgsConstructor
public class ItemService {

    // Lombok이 final 필드를 받는 생성자를 만들고, Spring이 실행 시 그 생성자에 빈을 주입한다.
    // Service가 직접 Repository를 만들지 않아 단위 테스트에서는 DB 대역으로 바꿀 수 있다.
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;

    // ===== 모듈 08: 품목 등록 (ADMIN only) =====

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

    // ===== 모듈 09: 품목 검색과 페이징 =====

    @Transactional(readOnly = true)
    public Page<ItemListView> search(ItemSearchForm form, Pageable pageable) {
        Page<Item> page = itemRepository.findAll(ItemSpecs.search(form), pageable);
        // 엔티티에는 categoryId만 있으므로 화면에 보여 줄 이름을 Service에서 합친다.
        Map<Long, String> categoryNames = categoryRepository.findAll().stream()
                .collect(Collectors.toMap(Category::getId, Category::getName));
        return page.map(item ->
                ItemListView.from(item, categoryNames.get(item.getCategoryId())));
    }

    // ===== 모듈 28: 품목 상세, 수정, 단종 =====

    @Transactional(readOnly = true)
    public ItemDetailView getDetail(Long itemId) {
        Item item = getEntity(itemId);
        return ItemDetailView.from(item, resolveCategoryName(item.getCategoryId()));
    }

    @Transactional(readOnly = true)
    public ItemForm getForm(Long itemId) {
        return ItemForm.from(getEntity(itemId));
    }

    @Transactional
    public void update(Long itemId, ItemForm form, LoginUser loginUser) {
        Authz.requireRole(loginUser, UserRole.ADMIN);
        validateCategoryExists(form.getCategoryId());
        Item item = getEntity(itemId);
        // 같은 트랜잭션에서 조회한 관리 엔티티이므로 커밋 과정에서 변경 감지가 동작한다.
        // 임의로 new로 만든 객체까지 필드 수정만으로 자동 저장되는 것은 아니다.
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
        // 과거 발주 이력을 보존하기 위해 DELETE 대신 상태만 바꾼다.
        item.discontinue();
    }

    // ===== 대시보드 집계 =====

    @Transactional(readOnly = true)
    public long countAll() {
        return itemRepository.count();
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

}
