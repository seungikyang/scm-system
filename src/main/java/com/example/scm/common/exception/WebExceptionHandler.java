package com.example.scm.common.exception;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * Web(Thymeleaf) 계층 전용 예외 처리. 사용자 친화 error 뷰를 렌더한다.
 * 학습 모듈 16에서 ApiExceptionHandler를 먼저 읽은 뒤, 같은 예외가 JSON 대신 Model과
 * error.html로 변환되는 차이를 비교한다.
 * 폼 처리 중 BusinessException 은 각 컨트롤러에서 redirect+flash(errorMessage)로 처리하고,
 * 그 밖의 조회/렌더 경로에서 발생한 예외만 여기서 error 뷰로 잡는다.
 */
@Slf4j
@ControllerAdvice(basePackages = "com.example.scm.controller.web")
public class WebExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public String handleBusiness(BusinessException e, Model model, HttpServletResponse response) {
        ErrorCode errorCode = e.getErrorCode();
        response.setStatus(errorCode.getHttpStatus().value());
        model.addAttribute("status", errorCode.getHttpStatus().value());
        model.addAttribute("code", errorCode.getCode());
        model.addAttribute("errorMessage", e.getMessage());
        return "error";
    }

    // 검색 조건이나 경로 ID의 변환 실패도 사용자 입력 오류다. 오류 화면과 HTTP 상태를 맞춘다.
    @ExceptionHandler({BindException.class, MethodArgumentTypeMismatchException.class,
            MissingServletRequestParameterException.class})
    public String handleInvalidRequest(Exception e, Model model, HttpServletResponse response) {
        return handleBusiness(new BusinessException(ErrorCode.INVALID_INPUT), model, response);
    }

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public String handleOptimisticLock(OptimisticLockingFailureException e, Model model,
                                       HttpServletResponse response) {
        log.warn("Optimistic lock conflict (web): {}", e.getMessage());
        response.setStatus(ErrorCode.INVALID_STATUS.getHttpStatus().value());
        model.addAttribute("status", ErrorCode.INVALID_STATUS.getHttpStatus().value());
        model.addAttribute("code", ErrorCode.INVALID_STATUS.getCode());
        model.addAttribute("errorMessage", "다른 사용자가 먼저 처리했습니다. 다시 확인해 주세요.");
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model, HttpServletResponse response) {
        log.error("Unhandled exception (web)", e);
        response.setStatus(ErrorCode.INTERNAL_ERROR.getHttpStatus().value());
        model.addAttribute("status", ErrorCode.INTERNAL_ERROR.getHttpStatus().value());
        model.addAttribute("code", ErrorCode.INTERNAL_ERROR.getCode());
        model.addAttribute("errorMessage", ErrorCode.INTERNAL_ERROR.getMessage());
        return "error";
    }
}
