package com.example.scm.common.exception;

import lombok.Getter;

/**
 * 업무 규칙 때문에 작업을 계속할 수 없다고 알리는 신호입니다.
 * 예를 들어 재고가 3개인데 5개를 출고하려 하면, 왜 안 되는지 담아 작업을 멈춥니다.
 *
 * 비즈니스 규칙 위반을 표현하는 표준 예외.
 *
 * <p>학습 모듈 15: ErrorCode를 먼저 읽은 다음 이 예외가 코드를 운반하는 방식을 본다.
 * 모듈 16에서는 이 예외를 받는 두 ExceptionHandler를 비교한다.</p>
 *
 * Service 는 "왜 안 되는지"를 ErrorCode 만 담아 던진다(throw). 그러면
 * ApiExceptionHandler(JSON)와 WebExceptionHandler(화면)가 각 계층에 맞는
 * HTTP 상태와 응답 형식으로 일관되게 바꿔 준다. 컨트롤러마다 흩어져 있던
 * 에러 분기 코드를 한 곳으로 모으는 장치다.
 */
@Getter
public class BusinessException extends RuntimeException {

    /** 어떤 규칙을 위반했는지. HTTP 상태·코드 문자열·기본 메시지를 함께 품고 있다. */
    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public BusinessException(ErrorCode errorCode, String customMessage) {
        super(customMessage);
        this.errorCode = errorCode;
    }
}
