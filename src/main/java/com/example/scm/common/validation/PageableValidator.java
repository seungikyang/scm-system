package com.example.scm.common.validation;

import com.example.scm.common.exception.BusinessException;
import com.example.scm.common.exception.ErrorCode;
import java.util.Set;
import org.springframework.data.domain.Pageable;

/**
 * 목록 조회 전에 페이지 범위와 정렬 기준을 검사합니다.
 * 잘못된 검색 요청이 데이터베이스 오류로 이어지지 않도록 서비스에서 공통으로 사용합니다.
 */
public final class PageableValidator {

    // 화면마다 실제로 정렬할 수 있는 필드만 허용합니다. 예: 품목명은 name, 단가는 unitPrice입니다.
    public static final Set<String> ITEM = Set.of("id", "itemCode", "name", "categoryId",
            "unit", "unitPrice", "safetyStock", "status", "createdAt", "updatedAt");
    public static final Set<String> PARTNER = Set.of("id", "name", "businessNumber", "partnerType",
            "contactName", "phone", "email", "address", "status", "createdAt", "updatedAt");
    public static final Set<String> PURCHASE_ORDER = Set.of("id", "orderNumber", "partnerId",
            "writerId", "approverId", "orderDate", "dueDate", "totalAmount", "status",
            "rejectReason", "version", "approvedAt", "receivedAt", "createdAt", "updatedAt");
    public static final Set<String> SALES_ORDER = Set.of("id", "orderNumber", "partnerId",
            "writerId", "managerId", "orderDate", "shipDate", "totalAmount", "status",
            "cancelReason", "version", "confirmedAt", "shippedAt", "completedAt", "createdAt", "updatedAt");

    private PageableValidator() {
    }

    public static void validateOffset(Pageable pageable) {
        // 시작 위치는 페이지 번호 × 페이지 크기입니다. 저장소가 처리할 수 있는 정수 범위를 넘으면 거부합니다.
        if (pageable.isPaged() && pageable.getOffset() > Integer.MAX_VALUE) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "지원하지 않는 페이지 범위입니다.");
        }
    }

    public static void validate(Pageable pageable, Set<String> allowedProperties) {
        validateOffset(pageable);
        // 여러 정렬 조건 중 하나라도 잘못되면 전체 요청을 입력 오류로 처리한다.
        if (pageable.getSort().stream().anyMatch(order -> !allowedProperties.contains(order.getProperty()))) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "지원하지 않는 정렬 필드입니다.");
        }
    }
}
