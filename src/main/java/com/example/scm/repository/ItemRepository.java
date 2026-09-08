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

/**
 * 품목 DB 접근 인터페이스.
 *
 * <p>학습 모듈 07: JpaRepository의 기본 CRUD → 메서드 이름 쿼리 → Pageable 개념 순으로
 * 익힌다. Specification은 모듈 09, 잠금 쿼리는 입고를 다루는 모듈 11에서 다시 본다.</p>
 *
 * <p>기본 CRUD와 동적 검색을 함께 제공한다. 입고 처리에서 사용하는
 * {@code findAllByIdForUpdate}만은 동시 요청이 같은 품목의 최초 재고 행을 동시에 만들지
 * 못하도록 DB 행을 잠그는 특수 쿼리다.</p>
 */
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
