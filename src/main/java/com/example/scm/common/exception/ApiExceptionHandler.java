package com.example.scm.common.exception;

import com.example.scm.common.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * 작업 중 문제가 생기면 API 사용자가 읽을 수 있는 오류 자료로 바꿉니다.
 * 예외는 정상 작업을 멈추는 신호이고, JSON은 이름과 값을 짝지어 전달하는 글 형식입니다.
 *
 * REST(api) 계층 전용 예외 처리. 공통 ErrorResponse(JSON) 로 변환한다.
 * 학습 모듈 16에서 BusinessException → Validation 오류 → 동시성 충돌 → 예상하지 못한
 * 예외 순으로 메서드를 읽는다. Spring은 선언 순서가 아니라 발생한 예외와 가장 가까운
 * 타입의 매핑을 선택한다.
 */
@Slf4j
@RestControllerAdvice(basePackages = "com.example.scm.controller.api")
public class ApiExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(BusinessException e) {
        // 업무 오류에는 이유와 상태 번호가 있습니다. 예: 재고 부족을 읽을 수 있는 안내로 바꿉니다.
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(ErrorResponse.of(errorCode, e.getMessage()));
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public ResponseEntity<ErrorResponse> handleValidation(BindException e) {
        // 잘못 쓴 입력칸마다 '칸 이름: 이유'를 만들고, 여러 안내를 쉼표로 이어 붙입니다.
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(this::formatFieldError)
                .collect(Collectors.joining(", "));
        if (message.isBlank()) {
            message = ErrorCode.INVALID_INPUT.getMessage();
        }
        return ResponseEntity.status(ErrorCode.INVALID_INPUT.getHttpStatus())
                .body(ErrorResponse.of(ErrorCode.INVALID_INPUT, message));
    }

    // JSON 파싱과 URL 타입 변환은 @Valid보다 먼저 실패할 수 있다. 입력 오류로 응답하고,
    // 원본 예외에 포함된 요청 본문·내부 타입 정보는 클라이언트에 노출하지 않는다.
    @ExceptionHandler({HttpMessageNotReadableException.class,
            MethodArgumentTypeMismatchException.class, MissingServletRequestParameterException.class})
    public ResponseEntity<ErrorResponse> handleInvalidRequest(Exception e) {
        return ResponseEntity.status(ErrorCode.INVALID_INPUT.getHttpStatus())
                .body(ErrorResponse.of(ErrorCode.INVALID_INPUT));
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleUnsupportedMediaType(HttpMediaTypeNotSupportedException e) {
        return ResponseEntity.status(ErrorCode.UNSUPPORTED_MEDIA_TYPE.getHttpStatus())
                .headers(e.getHeaders())
                .body(ErrorResponse.of(ErrorCode.UNSUPPORTED_MEDIA_TYPE));
    }

    /**
     * 동시 승인 등 낙관적 락 충돌 → INVALID_STATUS(400) (02_* 합의, 409 미사용).
     */
    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<ErrorResponse> handleOptimisticLock(OptimisticLockingFailureException e) {
        // 내가 열어 둔 장부를 다른 사람이 먼저 고쳤다면, 내 오래된 값으로 덮어쓰지 않고 다시 확인하게 합니다.
        log.warn("Optimistic lock conflict: {}", e.getMessage());
        return ResponseEntity.status(ErrorCode.INVALID_STATUS.getHttpStatus())
                .body(ErrorResponse.of(ErrorCode.INVALID_STATUS, "다른 사용자가 먼저 처리했습니다. 다시 확인해 주세요."));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e, HttpServletRequest request) {
        // 개발자가 고칠 수 있도록 자세한 원인은 서버 기록에 남기고, 응답에는 공통 오류 안내를 넣습니다.
        log.error("Unhandled exception on {} {}", request.getMethod(), request.getRequestURI(), e);
        return ResponseEntity.status(ErrorCode.INTERNAL_ERROR.getHttpStatus())
                .body(ErrorResponse.of(ErrorCode.INTERNAL_ERROR));
    }

    private String formatFieldError(FieldError fieldError) {
        // 값의 모양이 맞지 않아 변환에 실패하면(예: status=BAD) Spring의 기본 안내에
        // 'com.example.scm...ItemStatus' 같은 내부 클래스 이름이 들어갑니다.
        // 내부 구조를 응답에 드러내지 않도록 이 경우에는 짧은 안내로 바꿉니다.
        if (fieldError.isBindingFailure()) {
            return fieldError.getField() + ": 값의 형식이 올바르지 않습니다.";
        }
        return fieldError.getField() + ": " + fieldError.getDefaultMessage();
    }
}
