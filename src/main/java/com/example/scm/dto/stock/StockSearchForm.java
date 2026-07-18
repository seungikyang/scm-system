package com.example.scm.dto.stock;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockSearchForm {

    private String keyword;
    private boolean lowOnly;

    public String normalizedKeyword() {
        return keyword == null ? "" : keyword.trim();
    }
}
