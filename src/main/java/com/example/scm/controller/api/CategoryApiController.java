package com.example.scm.controller.api;

import com.example.scm.common.auth.CurrentUser;
import com.example.scm.common.auth.LoginUser;
import com.example.scm.dto.category.CategoryCreateRequest;
import com.example.scm.dto.category.CategoryDetailView;
import com.example.scm.dto.category.CategoryUpdateRequest;
import com.example.scm.dto.category.CategoryView;
import com.example.scm.service.CategoryService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 카테고리 CRUD를 JSON으로 제공하는 REST 컨트롤러.
 * 학습 모듈 25에서 CategoryService 다음에 읽고, Partner API와 달리 목록이 Page가 아닌
 * List인 이유와 실제 삭제를 사용하는 조건을 비교한다.
 * {@code @RequestBody}는 JSON을 DTO로, {@code @PathVariable}은 URL의 ID를 메서드 인자로
 * 변환한다. 요청 형식은 여기서 검증하고 권한·중복·삭제 가능 여부는 Service가 판단한다.
 */
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryApiController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryView> create(@Valid @RequestBody CategoryCreateRequest request,
                                               @CurrentUser LoginUser loginUser) {
        Long id = categoryService.create(request, loginUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.getView(id));
    }

    @GetMapping
    public ResponseEntity<List<CategoryView>> list() {
        return ResponseEntity.ok(categoryService.list());
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDetailView> detail(@PathVariable Long categoryId) {
        return ResponseEntity.ok(categoryService.getDetail(categoryId));
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryView> update(@PathVariable Long categoryId,
                                               @Valid @RequestBody CategoryUpdateRequest request,
                                               @CurrentUser LoginUser loginUser) {
        categoryService.update(categoryId, request, loginUser);
        return ResponseEntity.ok(categoryService.getView(categoryId));
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> delete(@PathVariable Long categoryId,
                                       @CurrentUser LoginUser loginUser) {
        categoryService.delete(categoryId, loginUser);
        return ResponseEntity.noContent().build();
    }
}
