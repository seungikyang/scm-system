package com.example.scm.common.response;

import java.util.List;
import lombok.Getter;
import org.springframework.data.domain.Page;

/**
 * 긴 목록을 여러 페이지로 나누어 보내는 양식입니다.
 * 현재 페이지의 자료뿐 아니라 전체 건수와 마지막 페이지인지도 함께 알려 줍니다.
 *
 * Spring Data 의 Page 객체를 API 응답용 JSON 구조로 고정한 DTO.
 *
 * <p>학습 모듈 32의 마지막에 읽는다. 먼저 개별 Entity → DTO 변환을 확인하고,
 * 그다음 {@code Page.map()}이 내용 타입을 바꿔도 페이지 메타정보를 유지하는지 본다.</p>
 *
 * Page 를 그대로 반환하면 직렬화되는 필드가 Spring 버전에 따라 달라질 수 있어서,
 * 프런트가 실제로 필요한 필드(내용 + 페이지 메타정보)만 골라 노출한다.
 * 생성자를 private 으로 막고 of() 팩토리로만 만들게 하여 만드는 경로를 하나로 유지한다.
 */
@Getter
public class PageResponse<T> {
    // T는 목록에 담을 자료형 자리입니다. 같은 페이지 양식에 품목이나 주문 등 다른 자료를 담을 수 있습니다.

    private final List<T> content;
    private final int page;
    private final int size;
    private final long totalElements;
    private final int totalPages;
    private final boolean first;
    private final boolean last;

    private PageResponse(Page<T> page) {
        this.content = page.getContent();
        // 컴퓨터의 페이지 번호는 0부터 셉니다. page=0은 사람이 보는 첫 번째 페이지입니다.
        this.page = page.getNumber();
        this.size = page.getSize();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.first = page.isFirst();
        this.last = page.isLast();
    }

    public static <T> PageResponse<T> of(Page<T> page) {
        return new PageResponse<>(page);
    }
}
