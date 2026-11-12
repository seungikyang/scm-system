package com.example.scm.dto.item;

import com.example.scm.domain.enums.ItemStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 물건 목록을 찾을 때 사용할 이름, 코드, 분류, 상태 조건입니다.
 * 선택하지 않은 조건은 건너뛰고, 입력한 조건들을 모두 만족하는 물건을 찾습니다.
 *
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
