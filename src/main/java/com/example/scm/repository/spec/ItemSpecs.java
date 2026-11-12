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
 * 물건 검색에서 선택한 조건을 모아 한 번에 적용합니다.
 * AND는 모든 조건을 만족해야 한다는 뜻입니다. 비어 있는 조건은 검색에서 제외합니다.
 *
 * 사용자가 입력한 값만 WHERE 절에 넣는 품목 동적 검색 조건.
 *
 * <p>학습 모듈 09: ItemRepository의 기본 파생 쿼리(모듈 07)를 이해한 뒤 읽는다.</p>
 *
 * <p>Specification의 람다에서 {@code root}는 Item 필드, {@code cb}는 equal/like 같은
 * 조건을 만드는 도구다. 비어 있지 않은 조건만 리스트에 모은 뒤 AND로 연결한다.</p>
 */
public final class ItemSpecs {

    private ItemSpecs() {
    }

    public static Specification<Item> search(ItemSearchForm form) {
        return (root, query, cb) -> {
            // Predicate는 '이 물건을 검색 결과에 넣을까?'를 판단하는 조건 한 개입니다.
            List<Predicate> predicates = new ArrayList<>();
            if (form != null) {
                if (StringUtils.hasText(form.getName())) {
                    // %는 앞뒤에 어떤 글자가 와도 된다는 뜻입니다. '볼트'로 '볼트 M6'도 찾습니다.
                    // 대소문자를 무시하고, 검색어 속 %·_는 글자 그대로 찾는다(LikePatterns 참고).
                    predicates.add(LikePatterns.containsIgnoreCase(cb, root.get("name"), form.getName()));
                }
                if (StringUtils.hasText(form.getItemCode())) {
                    predicates.add(LikePatterns.containsIgnoreCase(cb, root.get("itemCode"), form.getItemCode()));
                }
                if (form.getCategoryId() != null) {
                    predicates.add(cb.equal(root.get("categoryId"), form.getCategoryId()));
                }
                ItemStatus status = form.getStatus();
                if (status != null) {
                    predicates.add(cb.equal(root.get("status"), status));
                }
            }
            // 조건이 하나도 없으면 항상 참이 되어 전체 목록을 조회한다.
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
