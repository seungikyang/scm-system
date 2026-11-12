package com.example.scm.service;

import com.example.scm.common.validation.PageableValidator;

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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 물건의 이름, 분류, 가격, 안전재고를 등록하거나 고치는 담당자입니다.
 * 안전재고는 부족해지기 전에 확보해 두려는 수량이고, 단종은 새 주문에서 사용하지 않는 상태입니다.
 *
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

    // final 필드는 이 담당자가 계속 사용할 도구입니다. Spring이 필요한 도구를 생성자로 건네줍니다.
    // Lombok이 final 필드를 받는 생성자를 만들고, Spring이 실행 시 그 생성자에 빈을 주입한다.
    // Service가 직접 Repository를 만들지 않아 단위 테스트에서는 DB 대역으로 바꿀 수 있다.
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;

    // ===== 모듈 08: 품목 등록 (ADMIN only) =====

    @Transactional
    // 등록 순서: 관리자 확인 → 코드 중복 확인 → 분류 확인 → 물건 정보 만들기 → 장부에 저장.
    public Long create(ItemForm form, LoginUser loginUser) {
        Authz.requireRole(loginUser, UserRole.ADMIN);
        String itemCode = trimmed(form.getItemCode());
        validateItemCodeUnique(itemCode);
        validateCategoryExists(form.getCategoryId());

        Item item = Item.builder()
                .itemCode(itemCode)
                .name(trimmed(form.getName()))
                .categoryId(form.getCategoryId())
                .unit(form.getUnit())
                .unitPrice(form.getUnitPrice())
                .safetyStock(form.getSafetyStock())
                .status(ItemStatus.ACTIVE)
                .build();
        return saveNewItem(item);
    }

    @Transactional
    public Long create(ItemCreateRequest request, LoginUser loginUser) {
        Authz.requireRole(loginUser, UserRole.ADMIN);
        String itemCode = trimmed(request.getItemCode());
        validateItemCodeUnique(itemCode);
        validateCategoryExists(request.getCategoryId());

        Item item = Item.builder()
                .itemCode(itemCode)
                .name(trimmed(request.getName()))
                .categoryId(request.getCategoryId())
                .unit(request.getUnit())
                .unitPrice(request.getUnitPrice())
                .safetyStock(request.getSafetyStock())
                .status(ItemStatus.ACTIVE)
                .build();
        return saveNewItem(item);
    }

    // ===== 모듈 09: 품목 검색과 페이징 =====

    @Transactional(readOnly = true)
    public Page<ItemListView> search(ItemSearchForm form, Pageable pageable) {
        PageableValidator.validate(pageable, PageableValidator.ITEM);
        // Pageable은 '몇 번째 페이지에 몇 개를 어떤 순서로 보여 줄지'를 담는 주문서입니다.
        Page<Item> page = itemRepository.findAll(ItemSpecs.search(form), pageable);
        // 엔티티에는 categoryId만 있으므로 화면에 보여 줄 이름을 Service에서 합친다.
        Map<Long, String> categoryNames = categoryRepository.findAll().stream()
                .collect(Collectors.toMap(Category::getId, Category::getName));
        // map은 품목 자료를 화면용 자료로 하나씩 바꿉니다. 전체 건수와 페이지 번호는 유지합니다.
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
        item.update(trimmed(form.getName()), form.getCategoryId(), form.getUnit(),
                form.getUnitPrice(), form.getSafetyStock());
    }

    @Transactional
    public void update(Long itemId, ItemUpdateRequest request, LoginUser loginUser) {
        Authz.requireRole(loginUser, UserRole.ADMIN);
        validateCategoryExists(request.getCategoryId());
        Item item = getEntity(itemId);
        item.update(trimmed(request.getName()), request.getCategoryId(), request.getUnit(),
                request.getUnitPrice(), request.getSafetyStock());
    }

    @Transactional
    public void discontinue(Long itemId, LoginUser loginUser) {
        Authz.requireRole(loginUser, UserRole.ADMIN);
        Item item = getEntity(itemId);
        // 과거 발주 이력을 보존하기 위해 DELETE 대신 상태만 바꾼다.
        // 예전 주문에 적힌 물건 정보를 지우면 안 되므로, 삭제 대신 새 주문에서 못 쓰게 표시합니다.
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

    // 앞뒤 공백만 다른 값(" 전자부품 "과 "전자부품")이 서로 다른 자료로 등록되지 않도록
    // 이름·코드처럼 사람을 구별하는 값은 저장하고 비교하기 전에 앞뒤 공백을 지웁니다.
    private static String trimmed(String value) {
        return value == null ? null : value.trim();
    }

    private void validateItemCodeUnique(String itemCode) {
        if (itemRepository.existsByItemCode(itemCode)) {
            throw new BusinessException(ErrorCode.DUPLICATE_ITEM_CODE);
        }
    }

    // existsByItemCode 확인과 저장 사이에 다른 요청이 같은 코드를 먼저 저장할 수 있습니다.
    // 그때는 DB unique 제약이 막아 주므로, 서버 오류(500) 대신 중복 코드 오류로 바꿔 알려 줍니다.
    // 분류 존재 여부는 앞에서 확인했으므로 여기서 나는 제약 위반은 품목코드 중복으로 본다.
    private Long saveNewItem(Item item) {
        try {
            return itemRepository.saveAndFlush(item).getId();
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException(ErrorCode.DUPLICATE_ITEM_CODE);
        }
    }

    // 분류가 있는지만 확인하면, 확인 직후 다른 관리자가 그 분류를 삭제해 품목이 없는 분류를 가리킬 수 있습니다.
    // 그래서 findByIdForShare로 분류를 잠가 이 저장 묶음이 끝날 때까지 삭제를 기다리게 합니다.
    // CategoryService.delete는 같은 분류를 findByIdForUpdate로 잠근 뒤 품목 수를 셉니다.
    private void validateCategoryExists(Long categoryId) {
        if (categoryRepository.findByIdForShare(categoryId).isEmpty()) {
            throw new BusinessException(ErrorCode.CATEGORY_NOT_FOUND);
        }
    }

    private String resolveCategoryName(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .map(Category::getName)
                .orElse(null);
    }

}
