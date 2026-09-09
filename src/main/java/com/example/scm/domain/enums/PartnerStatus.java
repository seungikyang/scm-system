package com.example.scm.domain.enums;

/**
 * 거래처의 사용 상태. 비활성 거래처는 이력 보존을 위해 삭제하지 않지만
 * 새 발주를 작성할 때는 선택할 수 없다.
 * 학습 모듈 02에서 Partner의 deactivate/isActive와 함께 확인한다.
 */
public enum PartnerStatus {
    ACTIVE,
    INACTIVE
}
