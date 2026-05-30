package com.example.scm.dto.purchaseorder;

import lombok.Builder;
import lombok.Getter;

/**
 * 발주 상세 화면용 View DTO — 상세 데이터 + 권한/상태 기반 버튼 플래그. (02_contracts §3.3)
 * 플래그는 Service(컨트롤러)가 계산해 담는다. 뷰에서 role 직접 판단 금지(경계면 단순화).
 *
 * - canSubmit       : status==DRAFT && 작성자 본인 → 결재요청 버튼
 * - canCancel       : status∈{DRAFT,REQUESTED,APPROVED} && 작성자 본인 → 취소 버튼
 * - canApproveReject: status==REQUESTED && role∈{ADMIN,MANAGER} → 승인/반려 버튼
 * - canReceive      : status==APPROVED && role∈{ADMIN,MANAGER} → 입고 버튼
 */
@Getter
@Builder
public class PurchaseOrderDetailView {

    private final PurchaseOrderDetailResponse order;
    private final boolean canSubmit;
    private final boolean canCancel;
    private final boolean canApproveReject;
    private final boolean canReceive;
}
