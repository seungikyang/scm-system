package com.example.scm.service;

import com.example.scm.common.auth.Authz;
import com.example.scm.common.auth.LoginUser;
import com.example.scm.domain.enums.ItemStatus;
import com.example.scm.dto.stock.StockListView;
import com.example.scm.dto.stock.StockSearchForm;
import com.example.scm.dto.stock.StockSummaryView;
import com.example.scm.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;

    @Transactional(readOnly = true)
    public Page<StockListView> search(StockSearchForm form, Pageable pageable,
                                      LoginUser loginUser) {
        Authz.requireLogin(loginUser);
        return stockRepository.searchCurrentStock(
                ItemStatus.ACTIVE, form.normalizedKeyword(), form.isLowOnly(), pageable);
    }

    @Transactional(readOnly = true)
    public StockSummaryView getSummary(LoginUser loginUser) {
        Authz.requireLogin(loginUser);
        return new StockSummaryView(
                stockRepository.countItems(ItemStatus.ACTIVE),
                stockRepository.sumCurrentQuantity(ItemStatus.ACTIVE),
                stockRepository.countLowStockItems(ItemStatus.ACTIVE));
    }
}
