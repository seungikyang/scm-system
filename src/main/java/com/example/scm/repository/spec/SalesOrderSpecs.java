package com.example.scm.repository.spec;

import com.example.scm.domain.SalesOrder;
import com.example.scm.domain.enums.SalesOrderStatus;
import jakarta.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

/**
 * 수주 검색에서 선택한 조건을 모아 한 번에 적용합니다.
 * AND는 모든 조건을 만족해야 한다는 뜻입니다. 비어 있는 조건은 검색에서 제외합니다.
 */
public final class SalesOrderSpecs {
    private SalesOrderSpecs() {}

    public static Specification<SalesOrder> adminSearch(SalesOrderStatus status, Long partnerId,
                                                       LocalDate from, LocalDate to) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (status != null) predicates.add(cb.equal(root.get("status"), status));
            if (partnerId != null) predicates.add(cb.equal(root.get("partnerId"), partnerId));
            if (from != null) predicates.add(cb.greaterThanOrEqualTo(root.get("orderDate"), from));
            if (to != null) predicates.add(cb.lessThanOrEqualTo(root.get("orderDate"), to));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
