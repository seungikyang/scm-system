// 실제 구현 위치 예: src/main/java/com/example/scm/exception/GlobalExceptionHandler.java
// 목표: 모든 컨트롤러의 예외를 한 곳에서 변환하세요. TRD 3.9 참고.

// TODO 01: 모든 @RestController 에 공통 적용되는 어노테이션은?
@____
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // ===== 비즈니스 예외 =====
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(BusinessException e) {
        ErrorCode code = e.getErrorCode();
        // TODO 02: HTTP status 와 본문 body 를 어떻게 만들까?
        return ResponseEntity
            .status(code.____)
            .body(ErrorResponse.of(code, e.getMessage()));
    }

    // ===== Bean Validation 예외 (@Valid) =====
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException e) {
        // TODO 03: BindingResult 에서 필드명 + 메시지 리스트를 어떻게 모을지 채우세요.
        List<ErrorResponse.FieldErrorItem> fields = e.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(fe -> new ErrorResponse.FieldErrorItem(fe.getField(), fe.____()))
            .toList();

        ErrorResponse body = new ErrorResponse(
            400,
            ErrorCode.INVALID_INPUT.getCode(),
            "입력값 검증에 실패했습니다.",
            LocalDateTime.now(),
            fields
        );
        return ResponseEntity.badRequest().body(body);
    }

    // ===== 접근 권한 예외 =====
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException e) {
        return ResponseEntity
            .status(403)
            .body(ErrorResponse.of(ErrorCode.____, e.getMessage()));
    }

    // ===== 알 수 없는 예외 =====
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnknown(Exception e) {
        // TODO 04: 로그는 어떤 레벨로 남기는 게 좋을까요?
        log.____("Unexpected error", e);
        return ResponseEntity
            .status(500)
            .body(ErrorResponse.of(ErrorCode.INTERNAL_ERROR, null));
    }
}

// 학습 질문:
// Q1. @RestControllerAdvice 와 @ControllerAdvice 의 차이는?
//     A:
// Q2. 예외 메시지를 그대로 사용자에게 노출하면 안 되는 경우는?
//     A:
// Q3. 동일 예외 타입이라도 패키지별로 어드바이스를 분리할 수 있는가?
//     A:
