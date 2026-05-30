package com.example.scm.repository;

import com.example.scm.domain.PurchaseOrder;
import com.example.scm.domain.enums.PurchaseOrderStatus;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PurchaseOrderRepository
        extends JpaRepository<PurchaseOrder, Long>, JpaSpecificationExecutor<PurchaseOrder> {

    // ===== 내 발주서 목록 (writerId, 선택 status 필터) =====
    Page<PurchaseOrder> findByWriterId(Long writerId, Pageable pageable);

    Page<PurchaseOrder> findByWriterIdAndStatus(Long writerId, PurchaseOrderStatus status,
                                                Pageable pageable);

    // ===== 상세 (라인 fetch join, N+1 방지) =====
    @Query("select distinct po from PurchaseOrder po left join fetch po.lines "
            + "where po.id = :id")
    Optional<PurchaseOrder> findByIdWithLines(@Param("id") Long id);

    // ===== 채번 (일자별 시퀀스 산출) =====
    long countByOrderNumberStartingWith(String prefix);

    // ===== 대시보드 집계 =====
    long countByStatus(PurchaseOrderStatus status);

    long countByWriterId(Long writerId);
}
