package com.example.scm.repository;

import com.example.scm.domain.Item;
import com.example.scm.domain.enums.ItemStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ItemRepository
        extends JpaRepository<Item, Long>, JpaSpecificationExecutor<Item> {

    boolean existsByItemCode(String itemCode);

    List<Item> findByCategoryId(Long categoryId);

    long countByCategoryId(Long categoryId);

    // 발주 작성 폼 품목 셀렉트 (ACTIVE 만, 코드 정렬)
    List<Item> findByStatusOrderByItemCodeAsc(ItemStatus status);
}
