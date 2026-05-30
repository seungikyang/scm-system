package com.example.scm.dto.item;

import com.example.scm.domain.enums.ItemStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 품목 목록 검색 조건 (Web + REST query). 모든 조건은 선택(nullable).
 */
@Getter
@Setter
@NoArgsConstructor
public class ItemSearchForm {

    private String name;
    private String itemCode;
    private Long categoryId;
    private ItemStatus status;
}
