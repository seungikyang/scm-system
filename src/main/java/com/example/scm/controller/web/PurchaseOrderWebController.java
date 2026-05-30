package com.example.scm.controller.web;

import com.example.scm.common.auth.CurrentUser;
import com.example.scm.common.auth.LoginUser;
import com.example.scm.common.exception.BusinessException;
import com.example.scm.domain.enums.PurchaseOrderStatus;
import com.example.scm.dto.purchaseorder.PurchaseOrderCreateRequest;
import com.example.scm.dto.purchaseorder.PurchaseOrderSummaryResponse;
import com.example.scm.service.PurchaseOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 발주(사용자 영역) 화면. 뷰: purchaseorder/form, purchaseorder/my-list, purchaseorder/detail.
 * Model 속성/폼/버튼 플래그는 02_contracts §3.1~3.3 계약 그대로.
 */
@Controller
@RequestMapping("/purchase-orders")
@RequiredArgsConstructor
public class PurchaseOrderWebController {

    private final PurchaseOrderService purchaseOrderService;

    // ===== 3.1 발주서 작성 화면 =====

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("purchaseOrderForm", newFormObject());
        addFormOptions(model);
        return "purchaseorder/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("purchaseOrderForm") PurchaseOrderCreateRequest form,
                         BindingResult bindingResult,
                         @CurrentUser LoginUser loginUser,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            addFormOptions(model);
            return "purchaseorder/form";
        }
        try {
            var response = purchaseOrderService.create(form, loginUser);
            redirectAttributes.addFlashAttribute("successMessage", "발주서가 작성되었습니다.");
            return "redirect:/purchase-orders/" + response.getPurchaseOrderId();
        } catch (BusinessException e) {
            addFormOptions(model);
            model.addAttribute("errorMessage", e.getMessage());
            return "purchaseorder/form";
        }
    }

    // ===== 3.2 내 발주서 목록 화면 =====

    @GetMapping("/my")
    public String myList(@RequestParam(required = false) PurchaseOrderStatus status,
                         @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
                         Pageable pageable,
                         @CurrentUser LoginUser loginUser,
                         Model model) {
        Page<PurchaseOrderSummaryResponse> orders =
                purchaseOrderService.getMyOrders(loginUser, status, pageable);
        model.addAttribute("orders", orders);
        model.addAttribute("totalPages", orders.getTotalPages());
        model.addAttribute("totalElements", orders.getTotalElements());
        model.addAttribute("statusOptions", PurchaseOrderStatus.values());
        model.addAttribute("selectedStatus", status != null ? status.name() : null);
        return "purchaseorder/my-list";
    }

    // ===== 3.3 발주서 상세 화면 =====

    @GetMapping("/{poId}")
    public String detail(@PathVariable Long poId, @CurrentUser LoginUser loginUser, Model model) {
        var view = purchaseOrderService.getDetailView(poId, loginUser);
        model.addAttribute("order", view.getOrder());
        model.addAttribute("canSubmit", view.isCanSubmit());
        model.addAttribute("canCancel", view.isCanCancel());
        model.addAttribute("canApproveReject", view.isCanApproveReject());
        model.addAttribute("canReceive", view.isCanReceive());
        return "purchaseorder/detail";
    }

    // ===== 상태 전이 폼 처리 (작성자 본인) =====

    @PostMapping("/{poId}/submit")
    public String submit(@PathVariable Long poId,
                         @CurrentUser LoginUser loginUser,
                         RedirectAttributes redirectAttributes) {
        try {
            purchaseOrderService.submit(poId, loginUser);
            redirectAttributes.addFlashAttribute("successMessage", "결재 요청되었습니다.");
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/purchase-orders/" + poId;
    }

    @PostMapping("/{poId}/cancel")
    public String cancel(@PathVariable Long poId,
                         @CurrentUser LoginUser loginUser,
                         RedirectAttributes redirectAttributes) {
        try {
            purchaseOrderService.cancel(poId, loginUser);
            redirectAttributes.addFlashAttribute("successMessage", "발주가 취소되었습니다.");
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/purchase-orders/" + poId;
    }

    // ===== 내부 헬퍼 =====

    private PurchaseOrderCreateRequest newFormObject() {
        PurchaseOrderCreateRequest form = new PurchaseOrderCreateRequest();
        // 빈 라인 1건을 미리 둬 동적 라인 인덱스 바인딩(lines[0].*)을 가능하게 한다.
        form.getLines().add(new PurchaseOrderCreateRequest.LineRequest());
        return form;
    }

    private void addFormOptions(Model model) {
        model.addAttribute("partners", purchaseOrderService.getSupplierOptions());
        model.addAttribute("items", purchaseOrderService.getItemOptions());
    }
}
