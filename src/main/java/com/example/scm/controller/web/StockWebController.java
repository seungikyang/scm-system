package com.example.scm.controller.web;

import com.example.scm.common.auth.CurrentUser;
import com.example.scm.common.auth.LoginUser;
import com.example.scm.dto.stock.StockListView;
import com.example.scm.dto.stock.StockSearchForm;
import com.example.scm.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/stocks")
@RequiredArgsConstructor
public class StockWebController {

    private final StockService stockService;

    @GetMapping
    public String list(@ModelAttribute("searchForm") StockSearchForm searchForm,
                       @PageableDefault(size = 20, sort = "itemCode",
                               direction = Sort.Direction.ASC) Pageable pageable,
                       @CurrentUser LoginUser loginUser,
                       Model model) {
        Page<StockListView> stocks = stockService.search(searchForm, pageable, loginUser);
        model.addAttribute("stocks", stocks);
        model.addAttribute("summary", stockService.getSummary(loginUser));
        return "stock/list";
    }
}
