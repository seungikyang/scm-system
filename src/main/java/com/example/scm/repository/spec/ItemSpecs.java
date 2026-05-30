package com.example.scm.repository.spec;

import com.example.scm.domain.Item;
import com.example.scm.domain.enums.ItemStatus;
import com.example.scm.dto.item.ItemSearchForm;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

/**
 * 품목 검색 동적 조건. 모든 필터는 선택(null/blank 무시).
 */
public final class ItemSpecs {

    private ItemSpecs() {
    }

    public static Specification<Item> search(ItemSearchForm form) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (form != null) {
                if (StringUtils.hasText(form.getName())) {
                    predicates.add(cb.like(root.get("name"), "%" + form.getName().trim() + "%"));
                }
                if (StringUtils.hasText(form.getItemCode())) {
                    predicates.add(cb.like(root.get("itemCode"), "%" + form.getItemCode().trim() + "%"));
                }
                if (form.getCategoryId() != null) {
                    predicates.add(cb.equal(root.get("categoryId"), form.getCategoryId()));
                }
                ItemStatus status = form.getStatus();
                if (status != null) {
                    predicates.add(cb.equal(root.get("status"), status));
                }
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
