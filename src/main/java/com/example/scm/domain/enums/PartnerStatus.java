package com.example.scm.domain.enums;

/**
 * 거래처를 새 주문에 사용할 수 있는지 표시합니다.
 * ACTIVE는 사용 가능, INACTIVE는 사용 중지입니다. 사용 중지여도 과거 거래 기록은 남습니다.
 *
 * 거래처의 사용 상태. 비활성 거래처는 이력 보존을 위해 삭제하지 않지만
 * 새 발주를 작성할 때는 선택할 수 없다.
 * 학습 모듈 02에서 Partner의 deactivate/isActive와 함께 확인한다.
 */
public enum PartnerStatus {
    ACTIVE,
    INACTIVE
}
