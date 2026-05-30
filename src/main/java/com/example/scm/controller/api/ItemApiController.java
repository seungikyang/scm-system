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

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemApiController {

    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<ItemDetailView> create(@Valid @RequestBody ItemCreateRequest request,
                                                 @CurrentUser LoginUser loginUser) {
        Long id = itemService.create(request, loginUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(itemService.getDetail(id));
    }

    @GetMapping
    public ResponseEntity<PageResponse<ItemListView>> list(
            @ModelAttribute ItemSearchForm searchForm,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
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
