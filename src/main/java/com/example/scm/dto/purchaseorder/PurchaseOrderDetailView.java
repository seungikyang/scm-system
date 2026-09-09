package com.example.scm.dto.purchaseorder;

import lombok.Builder;
import lombok.Getter;

/**
 * 발주 상세 화면용 View DTO. 상세 데이터와 권한·상태에 따른 버튼 표시 여부를 묶는다.
 * 학습 모듈 32의 마지막 화면 전용 예제로, API 상세 DTO에 UI 판단값을 덧붙이는 방식을 본다.
 * 플래그는 Service(컨트롤러)가 계산해 담는다. 뷰에서 role 직접 판단 금지(경계면 단순화).
 *
 * - canSubmit       : {@code status == DRAFT}이고 작성자 본인 → 결재요청 버튼
 * - canCancel       : 진행 중 상태이고 작성자 본인 → 취소 버튼
 * - canApproveReject: {@code status == REQUESTED}이고 ADMIN/MANAGER → 승인/반려 버튼
 * - canReceive      : {@code status == APPROVED}이고 ADMIN/MANAGER → 입고 버튼
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
