package com.example.scm.controller.web;

import com.example.scm.common.auth.CurrentUser;
import com.example.scm.common.auth.LoginUser;
import com.example.scm.service.CategoryService;
import com.example.scm.service.ItemService;
import com.example.scm.service.PartnerService;
import com.example.scm.service.PurchaseOrderService;
import com.example.scm.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 로그인 후 첫 화면에 필요한 요약 수치를 모으는 MVC 컨트롤러.
 * {@link Model}에 넣은 이름은 dashboard.html의 {@code ${...}} 표현식과 연결되며,
 * 반환 문자열 {@code "dashboard"}가 렌더링할 템플릿 파일을 가리킨다.
 * 학습 모듈 35의 마지막에 읽어 여러 Service의 조회 결과를 한 화면에 조합하는 예를 본다.
 */
@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final PartnerService partnerService;
    private final ItemService itemService;
    private final CategoryService categoryService;
    private final PurchaseOrderService purchaseOrderService;
    private final StockService stockService;

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
        model.addAttribute("stockSummary", stockService.getSummary(loginUser));
        return "dashboard";
    }
}
