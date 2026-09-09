package com.example.scm.repository;

import com.example.scm.domain.Stock;
import com.example.scm.domain.enums.ItemStatus;
import com.example.scm.dto.stock.StockListView;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * 현재고 조회와 집계를 담당하는 DB 접근 인터페이스.
 *
 * <p>학습 모듈 11: 먼저 단순한 findByItemId로 입고 저장을 이해하고, 그다음 LEFT JOIN
 * 목록 쿼리와 집계 쿼리로 이동한다. 전체 Repository 비교는 모듈 31에서 다시 한다.</p>
 *
 * <p>재고 행이 아직 없는 운영 품목도 화면에는 수량 0으로 보여야 하므로 Item을 기준으로
 * Stock을 {@code left join}한다. {@code coalesce}는 join 결과가 null일 때 0으로 바꾸며,
 * 생성자 표현식은 조회 결과를 엔티티가 아닌 {@link StockListView}로 바로 만든다.</p>
 */
public interface StockRepository extends JpaRepository<Stock, Long> {

    Optional<Stock> findByItemId(Long itemId);

    // value는 현재 페이지 데이터, countQuery는 전체 페이지 수 계산에 사용한다.
    @Query(value = """
            select new com.example.scm.dto.stock.StockListView(
                i.id, i.itemCode, i.name, i.unit, coalesce(s.quantity, 0), i.safetyStock)
            from Item i left join Stock s on s.itemId = i.id
            where i.status = :itemStatus
              and (:keyword = ''
                   or lower(i.itemCode) like lower(concat('%', :keyword, '%'))
                   or lower(i.name) like lower(concat('%', :keyword, '%')))
              and (:lowOnly = false or coalesce(s.quantity, 0) <= i.safetyStock)
            """,
            countQuery = """
            select count(i)
            from Item i left join Stock s on s.itemId = i.id
            where i.status = :itemStatus
              and (:keyword = ''
                   or lower(i.itemCode) like lower(concat('%', :keyword, '%'))
                   or lower(i.name) like lower(concat('%', :keyword, '%')))
              and (:lowOnly = false or coalesce(s.quantity, 0) <= i.safetyStock)
            """)
    Page<StockListView> searchCurrentStock(@Param("itemStatus") ItemStatus itemStatus,
                                           @Param("keyword") String keyword,
                                           @Param("lowOnly") boolean lowOnly,
                                           Pageable pageable);

    @Query("select count(i) from Item i where i.status = :itemStatus")
    long countItems(@Param("itemStatus") ItemStatus itemStatus);

    @Query("""
            select count(i)
            from Item i left join Stock s on s.itemId = i.id
            where i.status = :itemStatus and coalesce(s.quantity, 0) <= i.safetyStock
            """)
    long countLowStockItems(@Param("itemStatus") ItemStatus itemStatus);

    @Query("""
            select coalesce(sum(s.quantity), 0)
            from Item i left join Stock s on s.itemId = i.id
            where i.status = :itemStatus
            """)
    long sumCurrentQuantity(@Param("itemStatus") ItemStatus itemStatus);
}
