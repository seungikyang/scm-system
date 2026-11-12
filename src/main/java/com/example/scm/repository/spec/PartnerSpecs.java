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
 * 거래처 검색에서 선택한 조건을 모아 한 번에 적용합니다.
 * AND는 모든 조건을 만족해야 한다는 뜻입니다. 비어 있는 조건은 검색에서 제외합니다.
 *
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
                    // 대소문자를 무시하고, 검색어 속 %·_는 글자 그대로 찾는다(LikePatterns 참고).
                    predicates.add(LikePatterns.containsIgnoreCase(cb, root.get("name"), form.getName()));
                }
                if (StringUtils.hasText(form.getBusinessNumber())) {
                    predicates.add(LikePatterns.containsIgnoreCase(
                            cb, root.get("businessNumber"), form.getBusinessNumber()));
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
