package com.example.scm.domain.enums;

/**
 * 품목의 사용 상태. 단종 품목은 과거 발주 기록을 위해 행을 삭제하지 않고
 * {@link #DISCONTINUED}로 남긴다.
 * 학습 모듈 03에서 Item보다 먼저 읽고, 실제 단종 처리는 모듈 28에서 다시 본다.
 */
public enum ItemStatus {
    ACTIVE,
    DISCONTINUED
}
