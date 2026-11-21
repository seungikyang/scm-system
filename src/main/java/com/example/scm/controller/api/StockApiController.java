package com.example.scm.controller.api;

import com.example.scm.common.auth.CurrentUser;
import com.example.scm.common.auth.LoginUser;
import com.example.scm.common.response.PageResponse;
import com.example.scm.dto.stock.StockListView;
import com.example.scm.dto.stock.StockSearchForm;
import com.example.scm.dto.stock.StockSummaryView;
import com.example.scm.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 다른 프로그램이 URL로 요청하는 재고 관련 작업의 접수 창구입니다.
 * 요청을 Service 담당자에게 전달하고 결과를 JSON 자료로 보냅니다. API는 프로그램끼리 요청과 결과를 주고받는 통로입니다.
 *
 * 현재고 목록과 요약 수치를 제공하는 읽기 전용 REST 컨트롤러.
 * 검색 조건과 페이징 값을 받아 Service 결과를 안정적인 {@link PageResponse} JSON 구조로
 * 감싸 반환한다.
 */
@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockApiController {

    private final StockService stockService;

    @GetMapping
    public ResponseEntity<PageResponse<StockListView>> list(
            @ModelAttribute StockSearchForm searchForm,
            @PageableDefault(size = 20, sort = "itemCode", direction = Sort.Direction.ASC)
            Pageable pageable,
            @CurrentUser LoginUser loginUser) {
        return ResponseEntity.ok(PageResponse.of(
                stockService.search(searchForm, pageable, loginUser)));
    }

    @GetMapping("/summary")
    public ResponseEntity<StockSummaryView> summary(@CurrentUser LoginUser loginUser) {
        return ResponseEntity.ok(stockService.getSummary(loginUser));
    }
}
