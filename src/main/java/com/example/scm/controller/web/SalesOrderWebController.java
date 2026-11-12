package com.example.scm.controller.web;
import com.example.scm.common.auth.*;
import com.example.scm.common.exception.BusinessException;
import com.example.scm.domain.enums.SalesOrderStatus;
import com.example.scm.dto.salesorder.*;
import com.example.scm.service.SalesOrderService;
import com.example.scm.service.PurchaseOrderService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
/**
 * 브라우저의 수주 화면 요청을 받아 담당 Service에 일을 맡깁니다.
 * Model은 화면에 넣을 자료 꾸러미이고 반환하는 문자열은 사용할 HTML 화면 파일의 이름입니다.
 */
@Controller @RequestMapping("/sales-orders") @RequiredArgsConstructor
public class SalesOrderWebController {
    private final SalesOrderService orders;
    private final PurchaseOrderService purchaseOrders;
    @GetMapping("/new")
    public String newForm(Model model) {
        // 처음 여는 빈 양식에 오늘 날짜와 물건 한 줄을 넣어 사용자가 바로 작성할 수 있게 합니다.
        SalesOrderCreateRequest form = new SalesOrderCreateRequest(); form.setOrderDate(LocalDate.now());
        form.getLines().add(new SalesOrderCreateRequest.LineRequest());
        model.addAttribute("salesOrderForm", form); options(model); return "salesorder/form";
    }
    @PostMapping
    public String create(@Valid @ModelAttribute("salesOrderForm") SalesOrderCreateRequest form, BindingResult errors,
                         @CurrentUser LoginUser user, Model model) {
        // 입력이 잘못되면 폼을 다시 보여 줍니다. 사용자가 이미 적은 값도 함께 돌려줍니다.
        if (errors.hasErrors()) { options(model); return "salesorder/form"; }
        // 저장 성공 뒤 상세 주소로 다시 이동합니다. 새로고침으로 같은 작성 요청을 반복하는 일을 줄입니다.
        try { return "redirect:/sales-orders/" + orders.create(form, user).salesOrderId(); }
        catch (BusinessException e) { model.addAttribute("errorMessage", e.getMessage()); options(model); return "salesorder/form"; }
    }
    @GetMapping("/my")
    public String my(@RequestParam(required = false) SalesOrderStatus status,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @CurrentUser LoginUser user, Model model) {
        model.addAttribute("orders", orders.myOrders(user, status, pageable));
        model.addAttribute("pendingView", false); model.addAttribute("selectedStatus", status);
        model.addAttribute("statusOptions", SalesOrderStatus.values()); return "salesorder/list";
    }
    @GetMapping("/pending")
    public String pending(@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                          @CurrentUser LoginUser user, Model model) {
        model.addAttribute("orders", orders.pending(user, pageable)); model.addAttribute("pendingView", true);
        return "salesorder/list";
    }
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, @CurrentUser LoginUser user, Model model) {
        var order = orders.detail(id, user); var status = order.status();
        boolean manager = user.isAdmin() || user.isManager();
        // &&는 두 조건 모두, ||는 둘 중 하나가 맞으면 참입니다. 내 역할과 주문 단계로 버튼을 정합니다.
        model.addAttribute("order", order);
        model.addAttribute("canConfirm", order.writerId().equals(user.id()) && status == SalesOrderStatus.DRAFT);
        model.addAttribute("canShip", manager && status == SalesOrderStatus.CONFIRMED);
        model.addAttribute("canComplete", manager && status == SalesOrderStatus.SHIPPED);
        // 출고(SHIPPED) 후 취소는 재고를 되돌리므로 ADMIN/MANAGER에게만 버튼을 보여 준다(Service에서도 다시 검사).
        model.addAttribute("canCancel", status == SalesOrderStatus.DRAFT || status == SalesOrderStatus.CONFIRMED
                || (manager && status == SalesOrderStatus.SHIPPED));
        return "salesorder/detail";
    }
    @PostMapping("/{id}/confirm")
    public String confirm(@PathVariable Long id, @CurrentUser LoginUser user, RedirectAttributes flash) {
        return transition(id, () -> orders.confirm(id, user), flash);
    }
    @PostMapping("/{id}/ship")
    public String ship(@PathVariable Long id, @CurrentUser LoginUser user, RedirectAttributes flash) {
        return transition(id, () -> orders.ship(id, user), flash);
    }
    @PostMapping("/{id}/complete")
    public String complete(@PathVariable Long id, @CurrentUser LoginUser user, RedirectAttributes flash) {
        return transition(id, () -> orders.complete(id, user), flash);
    }
    @PostMapping("/{id}/cancel")
    public String cancel(@PathVariable Long id, @ModelAttribute SalesOrderCancelRequest form,
                         @CurrentUser LoginUser user, RedirectAttributes flash) {
        return transition(id, () -> orders.cancel(id, form.getCancelReason(), user), flash);
    }
    // 성공·실패 메시지를 다음 화면에 잠깐 전달합니다. 이것을 flash 메시지라고 합니다.
    // redirect는 브라우저가 상세 주소로 다시 요청하도록 보내는 안내입니다.
    private String transition(Long id, Runnable action, RedirectAttributes flash) {
        // Runnable은 나중에 실행할 일을 담습니다. 확정·출고·완료·취소가 같은 안내 방식을 공유합니다.
        try { action.run(); flash.addFlashAttribute("successMessage", "수주 상태가 변경되었습니다."); }
        catch (BusinessException e) { flash.addFlashAttribute("errorMessage", e.getMessage()); }
        return "redirect:/sales-orders/" + id;
    }
    private void options(Model model) {
        model.addAttribute("partners", orders.customerOptions()); model.addAttribute("items", purchaseOrders.getItemOptions());
    }
}
