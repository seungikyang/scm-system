package com.example.scm.dto.stock;

import lombok.Getter;
import lombok.Setter;

/** 재고 목록의 선택 검색 조건. GET query string의 keyword/lowOnly 값이 여기에 바인딩된다. */
@Getter
@Setter
public class StockSearchForm {

    private String keyword;
    private boolean lowOnly;

    /** null을 빈 문자열로 바꾸고 앞뒤 공백을 제거해 Repository가 한 형태만 처리하게 한다. */
    public String normalizedKeyword() {
        return keyword == null ? "" : keyword.trim();
    }
}
