package com.example.scm.controller.api;
import com.example.scm.common.auth.*;
import com.example.scm.common.response.PageResponse;
import com.example.scm.domain.enums.SalesOrderStatus;
import com.example.scm.dto.salesorder.*;
import com.example.scm.service.SalesOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
/**
 * 다른 프로그램이 URL로 요청하는 수주 관련 작업의 접수 창구입니다.
 * 요청을 Service 담당자에게 전달하고 결과를 JSON 자료로 보냅니다. API는 프로그램끼리 요청과 결과를 주고받는 통로입니다.
 */
@RestController @RequestMapping("/api/sales-orders") @RequiredArgsConstructor
public class SalesOrderApiController {
    private final SalesOrderService orders;
    @PostMapping
    public ResponseEntity<SalesOrderResponse> create(@Valid @RequestBody SalesOrderCreateRequest request, @CurrentUser LoginUser user) {
        // 201은 새 자료 생성 성공입니다. body에는 새 수주 번호와 주문 내역을 넣습니다.
        return ResponseEntity.status(201).body(orders.create(request, user));
    }
    @GetMapping("/my")
    public PageResponse<SalesOrderResponse> my(@RequestParam(required = false) SalesOrderStatus status,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @CurrentUser LoginUser user) {
        // 상태를 생략하면 내 주문을 모두 검색합니다. 페이지를 생략하면 최신순으로 한 페이지 20개입니다.
        return PageResponse.of(orders.myOrders(user, status, pageable));
    }
    @GetMapping("/pending")
    public PageResponse<SalesOrderResponse> pending(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @CurrentUser LoginUser user) {
        return PageResponse.of(orders.pending(user, pageable));
    }
    @GetMapping("/{soId}")
    public SalesOrderResponse detail(@PathVariable Long soId, @CurrentUser LoginUser user) { return orders.detail(soId, user); }
    @PatchMapping("/{soId}/confirm")
    public SalesOrderResponse confirm(@PathVariable Long soId, @CurrentUser LoginUser user) { return orders.confirm(soId, user); }
    @PatchMapping("/{soId}/ship")
    public SalesOrderResponse ship(@PathVariable Long soId, @CurrentUser LoginUser user) { return orders.ship(soId, user); }
    @PatchMapping("/{soId}/complete")
    public SalesOrderResponse complete(@PathVariable Long soId, @CurrentUser LoginUser user) { return orders.complete(soId, user); }
    @PatchMapping("/{soId}/cancel")
    public SalesOrderResponse cancel(@PathVariable Long soId, @Valid @RequestBody SalesOrderCancelRequest request,
            @CurrentUser LoginUser user) { return orders.cancel(soId, request.getCancelReason(), user); }
}
