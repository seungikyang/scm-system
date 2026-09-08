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
 * 거래처 검색 폼에서 값이 들어온 조건만 WHERE 절에 추가한다.
 * 문자열은 부분 일치(like), enum은 정확히 일치(equal)시키고 모든 조건을 AND로 묶는다.
 * 학습 모듈 31에서 ItemSpecs와 구조를 비교하며 반복되는 동적 검색 패턴을 찾는다.
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
            // 아무 필터도 선택하지 않으면 전체 거래처를 반환한다.
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
