package com.example.scm.domain.enums;

/**
 * 거래처가 공급사인지 고객사인지 구분하는 값.
 * {@link #BOTH}는 공급과 구매 양쪽 거래가 가능한 거래처다.
 * 학습 모듈 02에서 Partner보다 먼저 읽는다.
 */
public enum PartnerType {
    SUPPLIER,
    CUSTOMER,
    BOTH
}
