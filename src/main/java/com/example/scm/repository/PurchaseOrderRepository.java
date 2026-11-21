package com.example.scm.repository;

import com.example.scm.domain.PurchaseOrder;
import com.example.scm.domain.enums.PurchaseOrderStatus;
import java.util.Optional;
import java.util.List;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * 발주 자료를 데이터베이스에서 찾거나 저장하는 창구입니다.
 * 데이터베이스는 자료를 모아 두는 전자 장부입니다. Spring이 함수 이름이나 조회식을 보고 실제 작업을 준비합니다.
 *
 * 발주 헤더의 DB 접근 인터페이스.
 * 기본 CRUD 외에 작성자별 목록, 상태별 집계, 동적 검색(Specification)을 제공한다.
 * 발주 라인은 지연 로딩되므로 상세 조회에서만 fetch join으로 한 번에 가져온다.
 * 학습 모듈 31에서 파생 쿼리 → fetch join → 채번 쿼리 → 집계 순으로 읽는다.
 */
public interface PurchaseOrderRepository
        extends JpaRepository<PurchaseOrder, Long>, JpaSpecificationExecutor<PurchaseOrder> {

    @Query("select po.status as status, count(po) as orderCount, sum(po.totalAmount) as totalAmount "
            + "from PurchaseOrder po where po.orderDate between :from and :to group by po.status")
    List<OrderStatisticsRow> summarize(@Param("from") LocalDate from, @Param("to") LocalDate to);

    // ===== 내 발주서 목록 (writerId, 선택 status 필터) =====
    Page<PurchaseOrder> findByWriterId(Long writerId, Pageable pageable);

    Page<PurchaseOrder> findByWriterIdAndStatus(Long writerId, PurchaseOrderStatus status,
                                                Pageable pageable);

    // ===== 상세: 헤더마다 라인을 다시 조회하는 N+1 문제를 fetch join으로 방지 =====
    @Query("select distinct po from PurchaseOrder po left join fetch po.lines "
            + "where po.id = :id")
    Optional<PurchaseOrder> findByIdWithLines(@Param("id") Long id);

    // ===== 채번 (삭제/공백과 무관하게 일자별 마지막 번호 산출) =====
    @Query("select max(po.orderNumber) from PurchaseOrder po "
            + "where po.orderNumber like concat(:prefix, '%')")
    String findMaxOrderNumber(@Param("prefix") String prefix);

    boolean existsByOrderNumber(String orderNumber);

    // ===== 대시보드 집계 =====
    long countByStatus(PurchaseOrderStatus status);

    long countByWriterId(Long writerId);
}
