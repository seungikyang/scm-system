package com.example.scm.controller.api;

import com.example.scm.common.auth.CurrentUser;
import com.example.scm.common.auth.LoginUser;
import com.example.scm.common.response.PageResponse;
import com.example.scm.dto.item.ItemCreateRequest;
import com.example.scm.dto.item.ItemDetailView;
import com.example.scm.dto.item.ItemListView;
import com.example.scm.dto.item.ItemSearchForm;
import com.example.scm.dto.item.ItemUpdateRequest;
import com.example.scm.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 품목 CRUD를 JSON으로 제공하는 REST 컨트롤러.
 *
 * <p>학습 모듈 18: create → list → detail → update → discontinue 순서로 HTTP 메서드,
 * DTO, Service 호출, 응답 상태를 한 줄씩 대응시킨다.</p>
 *
 * <p>매핑 애너테이션은 HTTP 메서드와 URL을 Java 메서드에 연결한다. 컨트롤러는 요청을
 * DTO로 받고 응답 상태를 정하며, ADMIN 권한·중복·단종 같은 업무 규칙은 ItemService에
 * 맡긴다.</p>
 */
@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemApiController {

    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<ItemDetailView> create(@Valid @RequestBody ItemCreateRequest request,
                                                 @CurrentUser LoginUser loginUser) {
        // MVC가 Jackson으로 JSON을 DTO로 변환하고 @Valid 제약 검사 후 이 메서드를 호출한다.
        Long id = itemService.create(request, loginUser);
        // 응답 DTO는 JSON이 된다. 201 CREATED는 새 자원이 생성되었다는 뜻이다.
        return ResponseEntity.status(HttpStatus.CREATED).body(itemService.getDetail(id));
    }

    @GetMapping
    public ResponseEntity<PageResponse<ItemListView>> list(
            @ModelAttribute ItemSearchForm searchForm,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        // query string은 searchForm에, page/size/sort는 Pageable에 자동 바인딩된다.
        Page<ItemListView> page = itemService.search(searchForm, pageable);
        return ResponseEntity.ok(PageResponse.of(page));
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemDetailView> detail(@PathVariable Long itemId) {
        return ResponseEntity.ok(itemService.getDetail(itemId));
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<ItemDetailView> update(@PathVariable Long itemId,
                                                 @Valid @RequestBody ItemUpdateRequest request,
                                                 @CurrentUser LoginUser loginUser) {
        itemService.update(itemId, request, loginUser);
        return ResponseEntity.ok(itemService.getDetail(itemId));
    }

    @PatchMapping("/{itemId}/discontinue")
    public ResponseEntity<ItemDetailView> discontinue(@PathVariable Long itemId,
                                                      @CurrentUser LoginUser loginUser) {
        itemService.discontinue(itemId, loginUser);
        return ResponseEntity.ok(itemService.getDetail(itemId));
    }
}
