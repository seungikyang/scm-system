package com.example.scm.controller.api;

import com.example.scm.common.auth.CurrentUser;
import com.example.scm.common.auth.LoginUser;
import com.example.scm.common.response.PageResponse;
import com.example.scm.domain.enums.PurchaseOrderStatus;
import com.example.scm.dto.purchaseorder.PurchaseOrderRejectRequest;
import com.example.scm.dto.purchaseorder.PurchaseOrderStatusResponse;
import com.example.scm.dto.purchaseorder.PurchaseOrderSummaryResponse;
import com.example.scm.service.PurchaseOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 발주(관리자/매니저 영역) REST API — ADMIN+MANAGER. (02_contracts §1.6~1.9, OQ-6)
 */
@RestController
@RequestMapping("/api/admin/purchase-orders")
@RequiredArgsConstructor
public class AdminPurchaseOrderApiController {

    private final PurchaseOrderService purchaseOrderService;

    /** 1.6 관리자 발주 목록 — GET /api/admin/purchase-orders (200) */
    @GetMapping
    public ResponseEntity<PageResponse<PurchaseOrderSummaryResponse>> list(
            @RequestParam(required = false) PurchaseOrderStatus status,
            @RequestParam(required = false) Long partnerId,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable,
            @CurrentUser LoginUser loginUser) {
        Page<PurchaseOrderSummaryResponse> page =
                purchaseOrderService.getAdminOrders(loginUser, status, partnerId, pageable);
        return ResponseEntity.ok(PageResponse.of(page));
    }

    /** 1.7 발주 승인 — PATCH /api/admin/purchase-orders/{poId}/approve (200) */
    @PatchMapping("/{poId}/approve")
    public ResponseEntity<PurchaseOrderStatusResponse> approve(@PathVariable Long poId,
                                                               @CurrentUser LoginUser loginUser) {
        return ResponseEntity.ok(purchaseOrderService.approve(poId, loginUser));
    }

    /** 1.8 발주 반려 — PATCH /api/admin/purchase-orders/{poId}/reject (200) */
    @PatchMapping("/{poId}/reject")
    public ResponseEntity<PurchaseOrderStatusResponse> reject(
            @PathVariable Long poId,
            @Valid @RequestBody PurchaseOrderRejectRequest request,
            @CurrentUser LoginUser loginUser) {
        return ResponseEntity.ok(
                purchaseOrderService.reject(poId, request.getRejectReason(), loginUser));
    }

    /** 1.9 입고 처리 — PATCH /api/admin/purchase-orders/{poId}/receive (200) */
    @PatchMapping("/{poId}/receive")
    public ResponseEntity<PurchaseOrderStatusResponse> receive(@PathVariable Long poId,
                                                               @CurrentUser LoginUser loginUser) {
        return ResponseEntity.ok(purchaseOrderService.receive(poId, loginUser));
    }
}
