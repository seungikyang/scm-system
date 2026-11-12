package com.example.scm.repository.spec;

import com.example.scm.domain.PurchaseOrder;
import com.example.scm.domain.enums.PurchaseOrderStatus;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

/**
 * 발주 검색에서 선택한 조건을 모아 한 번에 적용합니다.
 * AND는 모든 조건을 만족해야 한다는 뜻입니다. 비어 있는 조건은 검색에서 제외합니다.
 *
 * 관리자 발주 목록에서 선택한 상태/거래처 조건만 조합하는 동적 검색 도구.
 * null인 조건은 건너뛰므로 필터를 선택하지 않으면 전체 목록이 된다.
 * 학습 모듈 31에서 단순 파생 쿼리로 경우의 수를 모두 만들었을 때와 비교한다.
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
