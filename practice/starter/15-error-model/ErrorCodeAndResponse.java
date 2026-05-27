// 실제 구현 위치 예:
//   src/main/java/com/example/scm/exception/ErrorCode.java
//   src/main/java/com/example/scm/exception/ErrorResponse.java
//   src/main/java/com/example/scm/exception/BusinessException.java
// 목표: 도메인 에러 코드 + 공통 응답 + 비즈니스 예외 클래스를 채우세요.
//       TRD 3.9 참고.

// ===== ErrorCode =====
@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    INVALID_INPUT(400, "INVALID_INPUT", "잘못된 입력입니다."),
    AUTHENTICATION_REQUIRED(401, "AUTHENTICATION_REQUIRED", "로그인이 필요합니다."),
    ACCESS_DENIED(403, "ACCESS_DENIED", "접근 권한이 없습니다."),

    USER_NOT_FOUND(404, "USER_NOT_FOUND", "사용자를 찾을 수 없습니다."),
    // TODO 01: 거래처/카테고리/품목/발주/수주/공지 NOT_FOUND 코드를 채우세요.
    PARTNER_NOT_FOUND(____, "PARTNER_NOT_FOUND", "거래처를 찾을 수 없습니다."),
    CATEGORY_NOT_FOUND(404, "____", "카테고리를 찾을 수 없습니다."),
    ITEM_NOT_FOUND(404, "ITEM_NOT_FOUND", "____"),
    PURCHASE_ORDER_NOT_FOUND(404, "PURCHASE_ORDER_NOT_FOUND", "발주서를 찾을 수 없습니다."),
    SALES_ORDER_NOT_FOUND(404, "SALES_ORDER_NOT_FOUND", "수주서를 찾을 수 없습니다."),
    NOTICE_NOT_FOUND(404, "NOTICE_NOT_FOUND", "공지사항을 찾을 수 없습니다."),

    // 중복
    DUPLICATE_EMAIL(400, "DUPLICATE_EMAIL", "이미 사용 중인 이메일입니다."),
    DUPLICATE_BUSINESS_NUMBER(400, "DUPLICATE_BUSINESS_NUMBER", "이미 등록된 사업자번호입니다."),
    DUPLICATE_CATEGORY_NAME(400, "DUPLICATE_CATEGORY_NAME", "이미 등록된 카테고리명입니다."),
    DUPLICATE_ITEM_CODE(400, "DUPLICATE_ITEM_CODE", "이미 등록된 품목코드입니다."),

    // 도메인 규칙
    PARTNER_TYPE_MISMATCH(400, "PARTNER_TYPE_MISMATCH", "거래처 유형과 맞지 않는 요청입니다."),
    EMPTY_ORDER_LINES(400, "EMPTY_ORDER_LINES", "주문 라인이 비어 있습니다."),
    INVALID_DATE_RANGE(400, "INVALID_DATE_RANGE", "잘못된 날짜 범위입니다."),
    INVALID_STATUS(400, "INVALID_STATUS", "현재 상태에서 처리할 수 없습니다."),
    ITEM_DISCONTINUED(400, "ITEM_DISCONTINUED", "단종된 품목은 사용할 수 없습니다."),
    CATEGORY_HAS_ITEMS(400, "CATEGORY_HAS_ITEMS", "소속 품목이 있어 삭제할 수 없습니다."),

    INTERNAL_ERROR(500, "INTERNAL_ERROR", "서버 내부 오류");

    // TODO 02: 필드 세 개를 채우세요. (HTTP status / 코드 문자열 / 기본 메시지)
    private final int ____;
    private final String code;
    private final String defaultMessage;
}

// ===== ErrorResponse =====
public record ErrorResponse(
        int status,
        String code,
        String message,
        // TODO 03: 응답이 언제 발생했는지 알려 주는 필드. 타입은?
        ____ timestamp,
        // 검증 실패 시 필드 단위 메시지
        List<FieldErrorItem> fields
) {
    public static ErrorResponse of(ErrorCode errorCode, String message) {
        return new ErrorResponse(
            errorCode.getStatus(),
            errorCode.getCode(),
            (message == null) ? errorCode.getDefaultMessage() : message,
            LocalDateTime.now(),
            List.of()
        );
    }

    public record FieldErrorItem(String field, String message) {}
}

// ===== BusinessException =====
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
    }

    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}

// 학습 질문:
// Q1. HTTP status 와 ErrorCode.code 의 역할을 어떻게 나누었나?
//     A:
// Q2. BusinessException 을 checked 가 아닌 RuntimeException 으로 둔 이유는?
//     A:
// Q3. timestamp 를 Instant 가 아닌 LocalDateTime 으로 둘 때의 타임존 이슈는?
//     A:
