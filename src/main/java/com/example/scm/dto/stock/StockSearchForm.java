package com.example.scm.dto.stock;

import com.example.scm.repository.spec.LikePatterns;
import lombok.Getter;
import lombok.Setter;

/**
 * 재고 화면에서 물건 이름·코드를 찾거나 부족한 물건만 고르는 조건입니다.
 * 검색어가 없으면 빈 문자열로 정리해 전체 물건을 찾을 수 있게 합니다.
 *
 * 재고 목록의 선택 검색 조건. GET query string의 keyword/lowOnly 값이 여기에 바인딩된다.
 */
@Getter
@Setter
public class StockSearchForm {

    private String keyword;
    private boolean lowOnly;

    /**
     * null을 빈 문자열로 바꾸고 공백 제거·소문자 변환·%와 _ 탈출 처리를 해서
     * Repository가 한 형태만 처리하게 한다. 규칙은 LikePatterns.normalizedKeyword와 같다.
     */
    public String normalizedKeyword() {
        return LikePatterns.normalizedKeyword(keyword);
    }
}
