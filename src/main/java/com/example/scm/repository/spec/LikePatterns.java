package com.example.scm.repository.spec;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import java.util.Locale;

/**
 * 검색어를 LIKE 부분 일치 조건으로 바꿀 때 쓰는 공통 도구입니다.
 *
 * <p>LIKE에서 {@code %}는 "아무 글자 여러 개", {@code _}는 "아무 글자 한 개"라는 특별한 뜻이
 * 있습니다. 사용자가 검색창에 {@code _}나 {@code %}를 넣으면 그 글자 자체를 찾아야 하므로,
 * 앞에 탈출 문자 {@code !}를 붙여 일반 글자로 취급하게 합니다.</p>
 *
 * <p>탈출 문자로 역슬래시 대신 {@code !}를 쓰는 이유: MySQL은 문자열 안의 역슬래시를 다시
 * 해석하므로 H2와 MySQL에서 결과가 달라질 수 있습니다. JPQL에서는 {@code escape '!'}를 붙인다.</p>
 *
 * <p>대소문자를 구분하지 않도록 검색어와 컬럼을 모두 소문자로 맞춰 비교합니다.
 * 품목·거래처·재고·공지 검색이 같은 규칙을 따르게 하려는 목적입니다.</p>
 */
public final class LikePatterns {

    public static final char ESCAPE_CHAR = '!';

    private LikePatterns() {
    }

    /** 탈출 문자와 와일드카드 앞에 {@code !}를 붙여 글자 그대로 찾게 한다. null은 빈 문자열로 바꾼다. */
    public static String escape(String keyword) {
        if (keyword == null) {
            return "";
        }
        StringBuilder escaped = new StringBuilder(keyword.length());
        for (char ch : keyword.toCharArray()) {
            if (ch == ESCAPE_CHAR || ch == '%' || ch == '_') {
                escaped.append(ESCAPE_CHAR);
            }
            escaped.append(ch);
        }
        return escaped.toString();
    }

    /** 공백을 정리하고 소문자로 바꾼 뒤 탈출 처리한 값. JPQL 쿼리의 :keyword 파라미터로 넘긴다. */
    public static String normalizedKeyword(String keyword) {
        return keyword == null ? "" : escape(keyword.trim().toLowerCase(Locale.ROOT));
    }

    /** Specification에서 쓰는 "대소문자 무시 부분 일치" 조건. */
    public static Predicate containsIgnoreCase(CriteriaBuilder cb, Expression<String> field, String keyword) {
        return cb.like(cb.lower(field), "%" + normalizedKeyword(keyword) + "%", ESCAPE_CHAR);
    }
}
