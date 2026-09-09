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
 * 관리자와 매니저의 발주 승인·반려·입고 화면을 연결하는 MVC 컨트롤러.
 *
 * <p>학습 모듈 35에서 사용자용 PurchaseOrderWebController 다음에 읽고 같은 상세/상태
 * 변경이 역할에 따라 어떻게 다른 화면으로 노출되는지 비교한다.</p>
 *
 * <p>목록 Model에는 검색 결과뿐 아니라 상태/거래처 필터 옵션과 반려 사유 폼도 담는다.
 * URL이나 버튼을 숨기는 것만으로는 보안이 되지 않으므로 실제 역할 검사는 모든 상태
 * 변경 직전에 PurchaseOrderService가 수행한다.</p>
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
