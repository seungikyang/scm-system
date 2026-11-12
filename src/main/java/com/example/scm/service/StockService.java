package com.example.scm.service;

import com.example.scm.common.validation.PageableValidator;

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
 * 창고에 물건이 몇 개 남았는지 보여 주는 담당자입니다.
 * 현재 수량과 안전재고를 비교해 부족한 품목을 찾습니다. 수량 변경은 입고·출고 작업에서 처리합니다.
 *
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
        // 현재고 목록은 Item을 기준으로 조회하므로 품목 필드로 정렬한다.
        PageableValidator.validate(pageable, PageableValidator.ITEM);
        // null 검색어를 빈 문자열로 정규화하면 Repository 쿼리의 분기가 단순해진다.
        // lowOnly가 true이면 현재 수량이 안전재고 이하인 물건만 남깁니다. 같을 때도 부족 알림에 포함합니다.
        return stockRepository.searchCurrentStock(
                ItemStatus.ACTIVE, form.normalizedKeyword(), form.isLowOnly(), pageable);
    }

    @Transactional(readOnly = true)
    public StockSummaryView getSummary(LoginUser loginUser) {
        Authz.requireLogin(loginUser);
        // 물건 종류 수, 전체 개수, 안전재고 이하인 종류 수는 서로 다른 값이라 따로 계산합니다.
        return new StockSummaryView(
                stockRepository.countItems(ItemStatus.ACTIVE),
                stockRepository.sumCurrentQuantity(ItemStatus.ACTIVE),
                stockRepository.countLowStockItems(ItemStatus.ACTIVE));
    }
}
