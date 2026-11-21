package com.example.scm.repository;

import com.example.scm.domain.Stock;
import com.example.scm.domain.enums.ItemStatus;
import com.example.scm.dto.stock.StockListView;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * 재고 자료를 데이터베이스에서 찾거나 저장하는 창구입니다.
 * 데이터베이스는 자료를 모아 두는 전자 장부입니다. Spring이 함수 이름이나 조회식을 보고 실제 작업을 준비합니다.
 *
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

    // 다른 작업이 방금 입고한 수량도 반영하도록, 변경할 재고는 최신 값을 잠가 읽습니다.
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from Stock s where s.itemId = :itemId")
    Optional<Stock> findByItemIdForUpdate(@Param("itemId") Long itemId);

    // left join은 물건 목록을 기준으로 재고 장부를 붙입니다. 아직 입고되지 않은 물건도 목록에 남습니다.
    // coalesce(수량, 0)는 재고 값이 비어 있으면 0을 쓰라는 뜻입니다.
    // value는 현재 페이지 데이터, countQuery는 전체 페이지 수 계산에 사용한다.
    @Query(value = """
            select new com.example.scm.dto.stock.StockListView(
                i.id, i.itemCode, i.name, i.unit, coalesce(s.quantity, 0), i.safetyStock)
            from Item i left join Stock s on s.itemId = i.id
            where i.status = :itemStatus
              and (:keyword = ''
                   or lower(i.itemCode) like concat('%', :keyword, '%') escape '!'
                   or lower(i.name) like concat('%', :keyword, '%') escape '!')
              and (:lowOnly = false or coalesce(s.quantity, 0) <= i.safetyStock)
            """,
            countQuery = """
            select count(i)
            from Item i left join Stock s on s.itemId = i.id
            where i.status = :itemStatus
              and (:keyword = ''
                   or lower(i.itemCode) like concat('%', :keyword, '%') escape '!'
                   or lower(i.name) like concat('%', :keyword, '%') escape '!')
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
