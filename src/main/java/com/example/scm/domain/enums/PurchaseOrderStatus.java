package com.example.scm.domain.enums;

/**
 * 발주 상태. 상태는 정해진 방향으로만 움직인다(상태 머신).
 *
 * 학습 모듈 04에서 발주 엔티티보다 먼저 읽고 상태 이름과 순서부터 익힌다.
 * 역할별 전이 실행은 모듈 11에서 다시 확인한다.
 *
 * 정방향: DRAFT → REQUESTED → APPROVED → RECEIVED
 * 예외 : REQUESTED → REJECTED (승인자가 반려)
 *        DRAFT/REQUESTED/APPROVED → CANCELED (작성자 본인 취소)
 *
 * 허용 전이표와 각 단계 권한은 docs/STATE_MACHINE.md,
 * 실제 규칙 코드는 domain/PurchaseOrder.java 의 canSubmit()/canCancel() 등을 참고한다.
 */
public enum PurchaseOrderStatus {
    DRAFT,
    REQUESTED,
    APPROVED,
    REJECTED,
    RECEIVED,
    CANCELED
}
