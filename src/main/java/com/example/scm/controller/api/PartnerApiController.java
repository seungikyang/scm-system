package com.example.scm.controller.api;

import com.example.scm.common.auth.CurrentUser;
import com.example.scm.common.auth.LoginUser;
import com.example.scm.common.response.PageResponse;
import com.example.scm.dto.partner.PartnerCreateRequest;
import com.example.scm.dto.partner.PartnerDetailView;
import com.example.scm.dto.partner.PartnerListView;
import com.example.scm.dto.partner.PartnerResponse;
import com.example.scm.dto.partner.PartnerSearchForm;
import com.example.scm.dto.partner.PartnerUpdateRequest;
import com.example.scm.service.PartnerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 거래처 CRUD를 JSON으로 제공하는 REST 컨트롤러.
 * 학습 모듈 25에서 PartnerService를 읽은 뒤 create → list → detail → update → deactivate
 * 순으로 Service 메서드와 일대일 대응시킨다.
 * 생성은 201, 조회·수정은 200, 본문이 필요 없는 비활성화는 204를 반환한다. 실제 ADMIN
 * 권한과 업무 규칙은 모든 진입 경로에 공통 적용되도록 PartnerService가 검사한다.
 */
@RestController
@RequestMapping("/api/partners")
@RequiredArgsConstructor
public class PartnerApiController {

    private final PartnerService partnerService;

    @PostMapping
    public ResponseEntity<PartnerResponse> create(@Valid @RequestBody PartnerCreateRequest request,
                                                  @CurrentUser LoginUser loginUser) {
        Long id = partnerService.create(request, loginUser);
        PartnerResponse response = PartnerResponse.from(partnerService.getDetail(id));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<PageResponse<PartnerListView>> list(
            @ModelAttribute PartnerSearchForm searchForm,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        Page<PartnerListView> page = partnerService.search(searchForm, pageable);
        return ResponseEntity.ok(PageResponse.of(page));
    }

    @GetMapping("/{partnerId}")
    public ResponseEntity<PartnerDetailView> detail(@PathVariable Long partnerId) {
        return ResponseEntity.ok(partnerService.getDetail(partnerId));
    }

    @PutMapping("/{partnerId}")
    public ResponseEntity<PartnerResponse> update(@PathVariable Long partnerId,
                                                  @Valid @RequestBody PartnerUpdateRequest request,
                                                  @CurrentUser LoginUser loginUser) {
        partnerService.update(partnerId, request, loginUser);
        PartnerResponse response = PartnerResponse.from(partnerService.getDetail(partnerId));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{partnerId}")
    public ResponseEntity<Void> deactivate(@PathVariable Long partnerId,
                                           @CurrentUser LoginUser loginUser) {
        // URL 의미는 DELETE지만 이력을 위해 DB 행은 지우지 않고 INACTIVE로 바꾼다.
        partnerService.deactivate(partnerId, loginUser);
        return ResponseEntity.noContent().build();
    }
}
