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
 * 발주(사용자/본인 영역) REST API. (02_contracts §1.1~1.5)
 */
@RestController
@RequestMapping("/api/purchase-orders")
@RequiredArgsConstructor
public class PurchaseOrderApiController {

    private final PurchaseOrderService purchaseOrderService;

    /** 1.1 발주서 작성 — POST /api/purchase-orders (201) */
    @PostMapping
    public ResponseEntity<PurchaseOrderCreateResponse> create(
            @Valid @RequestBody PurchaseOrderCreateRequest request,
            @CurrentUser LoginUser loginUser) {
        PurchaseOrderCreateResponse response = purchaseOrderService.create(request, loginUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /** 1.2 발주 요청(결재 상신) — PATCH /api/purchase-orders/{poId}/submit (200) */
    @PatchMapping("/{poId}/submit")
    public ResponseEntity<PurchaseOrderStatusResponse> submit(@PathVariable Long poId,
                                                              @CurrentUser LoginUser loginUser) {
        return ResponseEntity.ok(purchaseOrderService.submit(poId, loginUser));
    }

    /** 1.3 내 발주서 목록 — GET /api/purchase-orders/my (200) */
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

    /** 1.4 발주서 상세 — GET /api/purchase-orders/{poId} (200) */
    @GetMapping("/{poId}")
    public ResponseEntity<PurchaseOrderDetailResponse> detail(@PathVariable Long poId,
                                                              @CurrentUser LoginUser loginUser) {
        return ResponseEntity.ok(purchaseOrderService.getDetail(poId, loginUser));
    }

    /** 1.5 발주서 취소 — PATCH /api/purchase-orders/{poId}/cancel (200) */
    @PatchMapping("/{poId}/cancel")
    public ResponseEntity<PurchaseOrderStatusResponse> cancel(@PathVariable Long poId,
                                                              @CurrentUser LoginUser loginUser) {
        return ResponseEntity.ok(purchaseOrderService.cancel(poId, loginUser));
    }
}
