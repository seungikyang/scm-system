package com.example.scm.common.response;

import com.example.scm.common.exception.ErrorCode;
import java.time.LocalDateTime;
import lombok.Getter;

/**
 * REST API가 오류일 때 반환하는 공통 JSON 모양.
 *
 * <p>학습 모듈 15: ErrorCode와 BusinessException을 읽은 뒤 실패 응답의 최종 모양을
 * 확인한다. 다음 모듈 16의 ApiExceptionHandler가 이 객체를 만든다.</p>
 *
 * <p>예를 들어 존재하지 않는 품목을 조회하면 status/code/message/timestamp가 항상 같은
 * 구조로 내려간다. 생성자를 숨기고 {@code of()} 팩토리 메서드만 열어 두어 누락된 필드가
 * 생기지 않게 한다.</p>
 */
@Getter
public class ErrorResponse {

    private final int status;
    private final String code;
    private final String message;
    private final LocalDateTime timestamp;

    private ErrorResponse(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(
                errorCode.getHttpStatus().value(),
                errorCode.getCode(),
                errorCode.getMessage());
    }

    public static ErrorResponse of(ErrorCode errorCode, String message) {
        return new ErrorResponse(
                errorCode.getHttpStatus().value(),
                errorCode.getCode(),
                message);
    }
}
