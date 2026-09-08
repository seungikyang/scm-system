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
 * 관리자와 매니저가 발주를 결재하고 입고 처리하는 REST API.
 * 학습 모듈 19에서 사용자용 PurchaseOrderApiController 다음에 읽으며, URL 분리와 실제
 * Service 권한 검사가 각각 어떤 역할을 하는지 비교한다.
 * URL에 admin이 있어도 그것만으로 보안이 생기지는 않으므로, 각 Service 메서드가
 * 실제 로그인 사용자의 역할을 ADMIN 또는 MANAGER로 제한한다.
 */
@RestController
@RequestMapping("/api/admin/purchase-orders")
@RequiredArgsConstructor
public class AdminPurchaseOrderApiController {

    private final PurchaseOrderService purchaseOrderService;

    /** 전체 발주를 상태와 거래처 조건으로 필터링해 조회한다. */
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

    /** 결재 요청 상태인 발주를 승인한다. */
    @PatchMapping("/{poId}/approve")
    public ResponseEntity<PurchaseOrderStatusResponse> approve(@PathVariable Long poId,
                                                               @CurrentUser LoginUser loginUser) {
        return ResponseEntity.ok(purchaseOrderService.approve(poId, loginUser));
    }

    /** 결재 요청 상태인 발주를 사유와 함께 반려한다. */
    @PatchMapping("/{poId}/reject")
    public ResponseEntity<PurchaseOrderStatusResponse> reject(
            @PathVariable Long poId,
            @Valid @RequestBody PurchaseOrderRejectRequest request,
            @CurrentUser LoginUser loginUser) {
        return ResponseEntity.ok(
                purchaseOrderService.reject(poId, request.getRejectReason(), loginUser));
    }

    /** 승인된 발주를 입고 처리하며, Service에서 재고도 함께 증가시킨다. */
    @PatchMapping("/{poId}/receive")
    public ResponseEntity<PurchaseOrderStatusResponse> receive(@PathVariable Long poId,
                                                               @CurrentUser LoginUser loginUser) {
        return ResponseEntity.ok(purchaseOrderService.receive(poId, loginUser));
    }
}
