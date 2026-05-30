package com.example.scm.repository.spec;

import com.example.scm.domain.Partner;
import com.example.scm.domain.enums.PartnerStatus;
import com.example.scm.domain.enums.PartnerType;
import com.example.scm.dto.partner.PartnerSearchForm;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

/**
 * 거래처 검색 동적 조건. 모든 필터는 선택(null/blank 무시).
 */
public final class PartnerSpecs {

    private PartnerSpecs() {
    }

    public static Specification<Partner> search(PartnerSearchForm form) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (form != null) {
                if (StringUtils.hasText(form.getName())) {
                    predicates.add(cb.like(root.get("name"), "%" + form.getName().trim() + "%"));
                }
                if (StringUtils.hasText(form.getBusinessNumber())) {
                    predicates.add(cb.like(root.get("businessNumber"),
                            "%" + form.getBusinessNumber().trim() + "%"));
                }
                PartnerType type = form.getPartnerType();
                if (type != null) {
                    predicates.add(cb.equal(root.get("partnerType"), type));
                }
                PartnerStatus status = form.getStatus();
                if (status != null) {
                    predicates.add(cb.equal(root.get("status"), status));
                }
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
