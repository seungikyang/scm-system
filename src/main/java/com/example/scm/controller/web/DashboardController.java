package com.example.scm.controller.web;

import com.example.scm.common.auth.CurrentUser;
import com.example.scm.common.auth.LoginUser;
import com.example.scm.common.exception.BusinessException;
import com.example.scm.common.exception.ErrorCode;
import com.example.scm.dto.dashboard.OrderStatisticsResponse;
import com.example.scm.service.CategoryService;
import com.example.scm.service.ItemService;
import com.example.scm.service.PartnerService;
import com.example.scm.service.PurchaseOrderService;
import com.example.scm.service.StockService;
import com.example.scm.service.SalesOrderService;
import com.example.scm.service.NoticeService;
import com.example.scm.service.OrderStatisticsService;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 로그인 후 첫 화면에 여러 업무의 요약을 모읍니다.
 * 각 Service에서 거래처 수, 대기 주문 수, 부족 재고, 최근 공지 같은 자료를 받아 화면에 넣습니다.
 *
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
    private final SalesOrderService salesOrderService;
    private final NoticeService noticeService;
    private final OrderStatisticsService orderStatisticsService;

    @GetMapping("/")
    public String dashboard(@CurrentUser LoginUser loginUser, Model model,
                            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
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
        model.addAttribute("salesOrderPendingCount", salesOrderService.countPending());
        model.addAttribute("recentNotices", noticeService.list(null,
                PageRequest.of(0, 5), loginUser).getContent());
        if (loginUser != null && (loginUser.isAdmin() || loginUser.isManager())) {
            model.addAttribute("orderStatistics", summarizeOrders(from, to, loginUser, model));
        }
        return "dashboard";
    }

    // 시작일이 종료일보다 늦은 잘못된 기간이 들어와도 대시보드 전체를 오류 화면으로 바꾸지 않는다.
    // 통계 영역만 기본 기간(이번 달 1일 ~ 오늘)으로 다시 계산하고, 화면 위에 안내 문구를 띄운다.
    private OrderStatisticsResponse summarizeOrders(LocalDate from, LocalDate to, LoginUser loginUser,
                                                    Model model) {
        try {
            return orderStatisticsService.summarize(from, to, loginUser);
        } catch (BusinessException e) {
            if (e.getErrorCode() != ErrorCode.INVALID_DATE_RANGE) {
                throw e;
            }
            model.addAttribute("errorMessage", "통계 기간의 시작일이 종료일보다 늦어 기본 기간으로 표시합니다.");
            return orderStatisticsService.summarize(null, null, loginUser);
        }
    }
}
