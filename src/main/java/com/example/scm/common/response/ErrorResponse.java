package com.example.scm.common.response;

import com.example.scm.common.exception.ErrorCode;
import java.time.LocalDateTime;
import lombok.Getter;

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
