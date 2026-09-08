package com.example.scm.repository;

import com.example.scm.domain.Partner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 거래처 DB 접근 인터페이스.
 * 학습 모듈 31에서는 기본 CRUD → exists 파생 쿼리 → Specification 실행 기능 순으로
 * 각 부모 인터페이스가 제공하는 책임을 나눠 본다.
 * {@link JpaRepository}는 기본 CRUD를, {@link JpaSpecificationExecutor}는 화면에서
 * 선택한 검색 조건만 조합하는 동적 검색을 제공한다.
 */
public interface PartnerRepository
        extends JpaRepository<Partner, Long>, JpaSpecificationExecutor<Partner> {

    boolean existsByBusinessNumber(String businessNumber);
}
