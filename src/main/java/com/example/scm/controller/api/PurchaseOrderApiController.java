package com.example.scm.controller.api;

import com.example.scm.common.auth.CurrentUser;
import com.example.scm.common.auth.LoginUser;
import com.example.scm.common.response.PageResponse;
import com.example.scm.domain.enums.PurchaseOrderStatus;
import com.example.scm.dto.purchaseorder.PurchaseOrderCreateRequest;
import com.example.scm.dto.purchaseorder.PurchaseOrderCreateResponse;
import com.example.scm.dto.purchaseorder.PurchaseOrderDetailResponse;
import com.example.scm.dto.purchaseorder.PurchaseOrderStatusResponse;
import com.example.scm.dto.purchaseorder.PurchaseOrderSummaryResponse;
import com.example.scm.service.PurchaseOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 일반 사용자가 자신의 발주를 작성·조회·상신·취소하는 REST API.
 *
 * <p>학습 모듈 19: 이 사용자 API를 먼저 읽은 뒤 AdminPurchaseOrderApiController와
 * 비교한다. 두 컨트롤러가 같은 PurchaseOrderService를 사용하는 점을 찾는다.</p>
 *
 * <p>이 클래스는 URL과 HTTP 응답만 결정한다. 작성자 본인인지, 현재 상태에서 동작이
 * 가능한지는 PurchaseOrderService가 확인하므로 화면 컨트롤러에서도 같은 규칙을 쓴다.</p>
 */
@RestController
@RequestMapping("/api/purchase-orders")
@RequiredArgsConstructor
public class PurchaseOrderApiController {

    private final PurchaseOrderService purchaseOrderService;

    /** 발주서를 새로 작성한다. 생성 성공이므로 HTTP 201을 반환한다. */
    @PostMapping
    public ResponseEntity<PurchaseOrderCreateResponse> create(
            @Valid @RequestBody PurchaseOrderCreateRequest request,
            @CurrentUser LoginUser loginUser) {
        PurchaseOrderCreateResponse response = purchaseOrderService.create(request, loginUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /** 임시 저장(DRAFT) 발주를 결재 요청(REQUESTED) 상태로 바꾼다. */
    @PatchMapping("/{poId}/submit")
    public ResponseEntity<PurchaseOrderStatusResponse> submit(@PathVariable Long poId,
                                                              @CurrentUser LoginUser loginUser) {
        return ResponseEntity.ok(purchaseOrderService.submit(poId, loginUser));
    }

    /** 로그인 사용자가 작성한 발주만 페이지 단위로 조회한다. */
    @GetMapping("/my")
    public ResponseEntity<PageResponse<PurchaseOrderSummaryResponse>> myList(
            @RequestParam(required = false) PurchaseOrderStatus status,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable,
            @CurrentUser LoginUser loginUser) {
        Page<PurchaseOrderSummaryResponse> page =
                purchaseOrderService.getMyOrders(loginUser, status, pageable);
        return ResponseEntity.ok(PageResponse.of(page));
    }

    /** 발주 상세를 조회한다. 작성자 또는 관리자/매니저만 접근할 수 있다. */
    @GetMapping("/{poId}")
    public ResponseEntity<PurchaseOrderDetailResponse> detail(@PathVariable Long poId,
                                                              @CurrentUser LoginUser loginUser) {
        return ResponseEntity.ok(purchaseOrderService.getDetail(poId, loginUser));
    }

    /** 취소 가능한 상태의 본인 발주를 CANCELED로 바꾼다. */
    @PatchMapping("/{poId}/cancel")
    public ResponseEntity<PurchaseOrderStatusResponse> cancel(@PathVariable Long poId,
                                                              @CurrentUser LoginUser loginUser) {
        return ResponseEntity.ok(purchaseOrderService.cancel(poId, loginUser));
    }
}
