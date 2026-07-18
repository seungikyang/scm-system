package com.example.scm.repository;

import com.example.scm.domain.Item;
import com.example.scm.domain.enums.ItemStatus;
import jakarta.persistence.LockModeType;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ItemRepository
        extends JpaRepository<Item, Long>, JpaSpecificationExecutor<Item> {

    boolean existsByItemCode(String itemCode);

    List<Item> findByCategoryId(Long categoryId);

    long countByCategoryId(Long categoryId);

    // 발주 작성 폼 품목 셀렉트 (ACTIVE 만, 코드 정렬)
    List<Item> findByStatusOrderByItemCodeAsc(ItemStatus status);

    /** 최초 재고 행 생성까지 품목별로 직렬화해 Stock UNIQUE 경합을 방지한다. */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select i from Item i where i.id in :itemIds order by i.id")
    List<Item> findAllByIdForUpdate(@Param("itemIds") Collection<Long> itemIds);
}
