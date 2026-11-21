package com.example.scm.repository;
import com.example.scm.domain.SalesOrder;
import com.example.scm.domain.enums.SalesOrderStatus;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import java.util.List;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
/**
 * 수주 자료를 데이터베이스에서 찾거나 저장하는 창구입니다.
 * 데이터베이스는 자료를 모아 두는 전자 장부입니다. Spring이 함수 이름이나 조회식을 보고 실제 작업을 준비합니다.
 */
public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long>, JpaSpecificationExecutor<SalesOrder> {
    // group by는 같은 상태끼리 모으기입니다. count는 주문 건수, sum은 주문 금액 합계입니다.
    @Query("select s.status as status, count(s) as orderCount, sum(s.totalAmount) as totalAmount "
            + "from SalesOrder s where s.orderDate between :from and :to group by s.status")
    List<OrderStatisticsRow> summarize(@Param("from") LocalDate from, @Param("to") LocalDate to);
    // 번호는 "SO-날짜-0001"처럼 길이가 같아서, 문자열 max만으로도 오늘 가장 큰 순번을 찾을 수 있습니다.
    // 전체 주문을 읽어 세지 않고 DB가 한 줄만 돌려주므로 주문이 많아도 빠릅니다.
    @Query("select max(s.orderNumber) from SalesOrder s where s.orderNumber like concat(:prefix, '%')")
    String findMaxOrderNumber(@Param("prefix") String prefix);
    boolean existsByOrderNumber(String orderNumber);
    // ":status is null or ..."는 상태를 고르지 않으면 조건을 건너뛰라는 뜻입니다. 함수 하나로 "전체"와 "상태별" 검색을 모두 처리합니다.
    @Query("select s from SalesOrder s where s.writerId = :writerId and (:status is null or s.status = :status)")
    Page<SalesOrder> findMyOrders(@Param("writerId") Long writerId, @Param("status") SalesOrderStatus status, Pageable pageable);
    Page<SalesOrder> findByStatus(SalesOrderStatus status, Pageable pageable);
    long countByStatus(SalesOrderStatus status);
    // PESSIMISTIC_WRITE는 수정용 잠금입니다. 같은 주문을 동시에 출고·취소하지 않게 차례를 정합니다.
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from SalesOrder s where s.id = :id")
    Optional<SalesOrder> findForUpdate(@Param("id") Long id);
}
