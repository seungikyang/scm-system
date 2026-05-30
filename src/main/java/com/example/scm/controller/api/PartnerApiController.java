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
        partnerService.deactivate(partnerId, loginUser);
        return ResponseEntity.noContent().build();
    }
}
