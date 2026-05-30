package com.example.scm.controller.web;

import com.example.scm.common.auth.CurrentUser;
import com.example.scm.common.auth.LoginUser;
import com.example.scm.common.exception.BusinessException;
import com.example.scm.domain.enums.PurchaseOrderStatus;
import com.example.scm.dto.purchaseorder.PurchaseOrderRejectRequest;
import com.example.scm.dto.purchaseorder.PurchaseOrderSummaryResponse;
import com.example.scm.service.PurchaseOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 발주 승인 관리(관리자/매니저) 화면 — ADMIN+MANAGER. 뷰: purchaseorder/admin-list.
 * Model 속성/필터/버튼 플래그는 02_contracts §3.4 계약 그대로. (권한 검사는 Service 에서 ADMIN/MANAGER)
 */
@Controller
@RequestMapping("/admin/purchase-orders")
@RequiredArgsConstructor
public class AdminPurchaseOrderWebController {

    private final PurchaseOrderService purchaseOrderService;

    // ===== 3.4 발주 승인 관리 화면 =====

    @GetMapping
    public String list(@RequestParam(required = false) PurchaseOrderStatus status,
                       @RequestParam(required = false) Long partnerId,
                       @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
                       Pageable pageable,
                       @CurrentUser LoginUser loginUser,
                       Model model) {
        Page<PurchaseOrderSummaryResponse> orders =
                purchaseOrderService.getAdminOrders(loginUser, status, partnerId, pageable);
        model.addAttribute("orders", orders);
        model.addAttribute("totalPages", orders.getTotalPages());
        model.addAttribute("totalElements", orders.getTotalElements());
        model.addAttribute("statusOptions", PurchaseOrderStatus.values());
        model.addAttribute("partners", purchaseOrderService.getSupplierOptions());
        model.addAttribute("selectedStatus", status != null ? status.name() : null);
        model.addAttribute("selectedPartnerId", partnerId);
        model.addAttribute("isAdminView", true);
        model.addAttribute("rejectForm", new PurchaseOrderRejectRequest());
        return "purchaseorder/admin-list";
    }

    // ===== 상태 전이 폼 처리 (ADMIN/MANAGER) =====

    @PostMapping("/{poId}/approve")
    public String approve(@PathVariable Long poId,
                          @CurrentUser LoginUser loginUser,
                          RedirectAttributes redirectAttributes) {
        try {
            purchaseOrderService.approve(poId, loginUser);
            redirectAttributes.addFlashAttribute("successMessage", "발주가 승인되었습니다.");
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/purchase-orders";
    }

    @PostMapping("/{poId}/reject")
    public String reject(@PathVariable Long poId,
                         @ModelAttribute("rejectForm") PurchaseOrderRejectRequest rejectForm,
                         @CurrentUser LoginUser loginUser,
                         RedirectAttributes redirectAttributes) {
        try {
            purchaseOrderService.reject(poId, rejectForm.getRejectReason(), loginUser);
            redirectAttributes.addFlashAttribute("successMessage", "발주가 반려되었습니다.");
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/purchase-orders";
    }

    @PostMapping("/{poId}/receive")
    public String receive(@PathVariable Long poId,
                          @CurrentUser LoginUser loginUser,
                          RedirectAttributes redirectAttributes) {
        try {
            purchaseOrderService.receive(poId, loginUser);
            redirectAttributes.addFlashAttribute("successMessage", "입고 처리되었습니다.");
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/purchase-orders";
    }
}
