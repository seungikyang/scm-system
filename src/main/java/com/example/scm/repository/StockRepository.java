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

public interface StockRepository extends JpaRepository<Stock, Long> {

    Optional<Stock> findByItemId(Long itemId);

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
