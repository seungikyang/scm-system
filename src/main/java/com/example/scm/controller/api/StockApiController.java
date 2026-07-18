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
