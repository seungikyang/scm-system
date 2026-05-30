package com.example.scm.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // 400 - 입력/검증
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "INVALID_INPUT", "입력값 검증에 실패했습니다."),

    // 401 / 403
    AUTHENTICATION_REQUIRED(HttpStatus.UNAUTHORIZED, "AUTHENTICATION_REQUIRED", "로그인이 필요합니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "ACCESS_DENIED", "접근 권한이 없습니다."),

    // 404 - 미존재
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "사용자를 찾을 수 없습니다."),
    PARTNER_NOT_FOUND(HttpStatus.NOT_FOUND, "PARTNER_NOT_FOUND", "거래처를 찾을 수 없습니다."),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "CATEGORY_NOT_FOUND", "카테고리를 찾을 수 없습니다."),
    ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "ITEM_NOT_FOUND", "품목을 찾을 수 없습니다."),
    PURCHASE_ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "PURCHASE_ORDER_NOT_FOUND", "발주서를 찾을 수 없습니다."),
    SALES_ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "SALES_ORDER_NOT_FOUND", "수주서를 찾을 수 없습니다."),
    NOTICE_NOT_FOUND(HttpStatus.NOT_FOUND, "NOTICE_NOT_FOUND", "공지사항을 찾을 수 없습니다."),

    // 400 - 중복
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "DUPLICATE_EMAIL", "이미 사용 중인 이메일입니다."),
    DUPLICATE_BUSINESS_NUMBER(HttpStatus.BAD_REQUEST, "DUPLICATE_BUSINESS_NUMBER", "이미 등록된 사업자번호입니다."),
    DUPLICATE_CATEGORY_NAME(HttpStatus.BAD_REQUEST, "DUPLICATE_CATEGORY_NAME", "이미 사용 중인 카테고리명입니다."),
    DUPLICATE_ITEM_CODE(HttpStatus.BAD_REQUEST, "DUPLICATE_ITEM_CODE", "이미 등록된 품목코드입니다."),

    // 400 - 비즈니스 규칙
    CATEGORY_HAS_ITEMS(HttpStatus.BAD_REQUEST, "CATEGORY_HAS_ITEMS", "소속 품목이 있는 카테고리는 삭제할 수 없습니다."),
    PARTNER_TYPE_MISMATCH(HttpStatus.BAD_REQUEST, "PARTNER_TYPE_MISMATCH", "거래처 유형이 일치하지 않습니다."),
    EMPTY_ORDER_LINES(HttpStatus.BAD_REQUEST, "EMPTY_ORDER_LINES", "주문 라인이 비어 있습니다."),
    INVALID_DATE_RANGE(HttpStatus.BAD_REQUEST, "INVALID_DATE_RANGE", "잘못된 날짜 범위입니다."),
    INVALID_STATUS(HttpStatus.BAD_REQUEST, "INVALID_STATUS", "현재 상태에서는 처리할 수 없습니다."),
    ITEM_DISCONTINUED(HttpStatus.BAD_REQUEST, "ITEM_DISCONTINUED", "단종된 품목은 사용할 수 없습니다."),

    // 500
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "서버 내부 오류가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
