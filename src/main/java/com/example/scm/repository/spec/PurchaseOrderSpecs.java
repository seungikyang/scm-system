package com.example.scm.repository.spec;

import com.example.scm.domain.PurchaseOrder;
import com.example.scm.domain.enums.PurchaseOrderStatus;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

/**
 * 관리자 발주 목록 검색 동적 조건. 모든 필터는 선택(null 무시). (02_contracts §1.6)
 */
public final class PurchaseOrderSpecs {

    private PurchaseOrderSpecs() {
    }

    public static Specification<PurchaseOrder> adminSearch(PurchaseOrderStatus status, Long partnerId) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            if (partnerId != null) {
                predicates.add(cb.equal(root.get("partnerId"), partnerId));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
