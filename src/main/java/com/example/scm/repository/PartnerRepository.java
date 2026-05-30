package com.example.scm.repository;

import com.example.scm.domain.Partner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PartnerRepository
        extends JpaRepository<Partner, Long>, JpaSpecificationExecutor<Partner> {

    boolean existsByBusinessNumber(String businessNumber);
}
