package com.example.scm.repository;

import com.example.scm.domain.Partner;
import com.example.scm.domain.enums.PartnerType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * 거래처 자료를 데이터베이스에서 찾거나 저장하는 창구입니다.
 * 데이터베이스는 자료를 모아 두는 전자 장부입니다. Spring이 함수 이름이나 조회식을 보고 실제 작업을 준비합니다.
 *
 * 거래처 DB 접근 인터페이스.
 * 학습 모듈 31에서는 기본 CRUD → exists 파생 쿼리 → Specification 실행 기능 순으로
 * 각 부모 인터페이스가 제공하는 책임을 나눠 본다.
 * {@link JpaRepository}는 기본 CRUD를, {@link JpaSpecificationExecutor}는 화면에서
 * 선택한 검색 조건만 조합하는 동적 검색을 제공한다.
 */
public interface PartnerRepository
        extends JpaRepository<Partner, Long>, JpaSpecificationExecutor<Partner> {

    boolean existsByBusinessNumber(String businessNumber);

    // 주문을 저장하는 동안 거래처가 비활성화되거나 유형이 바뀌지 않도록 읽기 잠금을 겁니다.
    // PESSIMISTIC_READ는 여러 주문이 동시에 읽는 것은 허용하고, 거래처 수정만 주문 저장이 끝날 때까지 기다리게 합니다.
    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("select p from Partner p where p.id = :id")
    Optional<Partner> findByIdForShare(@Param("id") Long id);

    // 검색에서는 거래처의 현재 상태나 유형이 바뀌어도 과거 주문을 찾을 수 있어야 한다.
    @Query("""
            select p from Partner p
            where p.partnerType in :types
               or exists (select o.id from PurchaseOrder o where o.partnerId = p.id)
            order by p.name, p.id
            """)
    List<Partner> findPurchaseSearchPartners(@Param("types") List<PartnerType> types);

    @Query("""
            select p from Partner p
            where p.partnerType in :types
               or exists (select o.id from SalesOrder o where o.partnerId = p.id)
            order by p.name, p.id
            """)
    List<Partner> findSalesSearchPartners(@Param("types") List<PartnerType> types);
}
