package com.example.scm.controller.api;

import com.example.scm.common.auth.CurrentUser;
import com.example.scm.common.auth.LoginUser;
import com.example.scm.dto.dashboard.OrderStatisticsResponse;
import com.example.scm.service.OrderStatisticsService;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 다른 프로그램이 URL로 요청하는 주문 통계 조회의 접수 창구입니다.
 * 요청을 Service 담당자에게 전달하고 결과를 JSON 자료로 보냅니다. API는 프로그램끼리 요청과 결과를 주고받는 통로입니다.
 */
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardApiController {
    private final OrderStatisticsService statistics;

    // 기간을 생략할 수 있게 required = false로 둡니다. 비어 있으면 OrderStatisticsService가 "이번 달 1일 ~ 오늘"로 채웁니다.
    // 날짜 형식은 ISO(예: 2026-11-14)로 고정해, 나라마다 다른 날짜 표기 때문에 잘못 읽는 일을 막습니다.
    @GetMapping("/order-statistics")
    public OrderStatisticsResponse statistics(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @CurrentUser LoginUser user) {
        return statistics.summarize(from, to, user);
    }
}
