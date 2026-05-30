package com.example.scm.controller.web;

import com.example.scm.common.auth.CurrentUser;
import com.example.scm.common.auth.LoginUser;
import com.example.scm.service.CategoryService;
import com.example.scm.service.ItemService;
import com.example.scm.service.PartnerService;
import com.example.scm.service.PurchaseOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final PartnerService partnerService;
    private final ItemService itemService;
    private final CategoryService categoryService;
    private final PurchaseOrderService purchaseOrderService;

    @GetMapping("/")
    public String dashboard(@CurrentUser LoginUser loginUser, Model model) {
        model.addAttribute("partnerCount", partnerService.countAll());
        model.addAttribute("itemCount", itemService.countAll());
        model.addAttribute("categoryCount", categoryService.countAll());
        // currentUser 는 GlobalModelAdvice 가 전역 주입하지만, 대시보드 명시 요구사항이라 함께 둔다.
        model.addAttribute("currentUser", loginUser);

        // 발주 집계 (속성명 유지 — frontend 경계면).
        model.addAttribute("purchaseOrderPendingCount", purchaseOrderService.countPending());
        model.addAttribute("myPurchaseOrderCount",
                loginUser != null ? purchaseOrderService.countMyOrders(loginUser.id()) : 0L);
        return "dashboard";
    }
}
