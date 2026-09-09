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

/**
 * 재고 목록과 대시보드 요약을 조회하는 서비스.
 *
 * <p>학습 모듈 11: 입고로 재고가 증가하는 흐름을 확인한 뒤, 그 결과를 목록과 요약으로
 * 읽는 순서로 살펴본다.</p>
 *
 * <p>현재고 자체의 증가는 발주 입고와 반드시 한 트랜잭션으로 묶여야 하므로
 * {@link PurchaseOrderService#receive(Long, LoginUser)}가 담당한다. 이 서비스는 읽기 전용
 * 조회를 모아 두고, API와 웹 화면이 같은 재고 계산 규칙을 사용하게 한다.</p>
 */
@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;

    @Transactional(readOnly = true)
    public Page<StockListView> search(StockSearchForm form, Pageable pageable,
                                      LoginUser loginUser) {
        Authz.requireLogin(loginUser);
        // null 검색어를 빈 문자열로 정규화하면 Repository 쿼리의 분기가 단순해진다.
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
