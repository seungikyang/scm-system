package com.example.scm.controller.api;

import com.example.scm.common.auth.CurrentUser;
import com.example.scm.common.auth.LoginUser;
import com.example.scm.common.response.PageResponse;
import com.example.scm.domain.enums.SalesOrderStatus;
import com.example.scm.dto.salesorder.SalesOrderResponse;
import com.example.scm.service.SalesOrderService;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

/**
 * 다른 프로그램이 URL로 요청하는 수주 관리의 접수 창구입니다.
 * 요청을 Service 담당자에게 전달하고 결과를 JSON 자료로 보냅니다. API는 프로그램끼리 요청과 결과를 주고받는 통로입니다.
 */
@RestController
@RequestMapping("/api/admin/sales-orders")
@RequiredArgsConstructor
public class AdminSalesOrderApiController {
    private final SalesOrderService orders;

    @GetMapping
    public PageResponse<SalesOrderResponse> list(
            @RequestParam(required = false) SalesOrderStatus status,
            @RequestParam(required = false) Long partnerId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @PageableDefault(size = 20, sort = {"createdAt", "id"}, direction = Sort.Direction.DESC) Pageable pageable,
            @CurrentUser LoginUser user) {
        return PageResponse.of(orders.adminOrders(user, status, partnerId, from, to, pageable));
    }
}
