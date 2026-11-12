package com.example.scm.controller.web;

import com.example.scm.common.auth.CurrentUser;
import com.example.scm.common.auth.LoginUser;
import com.example.scm.domain.enums.SalesOrderStatus;
import com.example.scm.service.SalesOrderService;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * 브라우저의 수주 화면 요청을 받아 담당 Service에 일을 맡깁니다.
 * Model은 화면에 넣을 자료 꾸러미이고 반환하는 문자열은 사용할 HTML 화면 파일의 이름입니다.
 */
@Controller
@RequestMapping("/admin/sales-orders")
@RequiredArgsConstructor
public class AdminSalesOrderWebController {
    private final SalesOrderService orders;

    @GetMapping
    public String list(@RequestParam(required = false) SalesOrderStatus status,
                       @RequestParam(required = false) Long partnerId,
                       @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                       @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
                       @PageableDefault(size = 20, sort = {"createdAt", "id"}, direction = Sort.Direction.DESC) Pageable pageable,
                       @CurrentUser LoginUser user, Model model) {
        model.addAttribute("orders", orders.adminOrders(user, status, partnerId, from, to, pageable));
        model.addAttribute("partners", orders.customerSearchOptions(user));
        model.addAttribute("statusOptions", SalesOrderStatus.values());
        model.addAttribute("selectedStatus", status);
        model.addAttribute("selectedPartnerId", partnerId);
        model.addAttribute("from", from);
        model.addAttribute("to", to);
        return "salesorder/admin-list";
    }
}
