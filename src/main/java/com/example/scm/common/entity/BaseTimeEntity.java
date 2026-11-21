package com.example.scm.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * 자료를 언제 만들고 언제 고쳤는지 적는 공통 양식입니다.
 * 여러 자료가 이 클래스를 상속하면 같은 시간 기록 코드를 매번 만들 필요가 없습니다.
 *
 * 모든 엔티티의 생성/수정 시각을 자동 기록하는 공통 부모.
 *
 * <p>학습 모듈 01: User보다 먼저 읽고 상속되는 두 필드를 확인한다. Auditing을 켜는
 * JpaAuditingConfig의 자세한 원리는 설정 Bean을 다루는 모듈 33에서 다시 확인한다.</p>
 *
 * - @MappedSuperclass: 이 클래스는 테이블을 만들지 않고, 필드만 자식 엔티티(User, Item ...)에 물려준다.
 * - @EntityListeners + @CreatedDate/@LastModifiedDate: JPA Auditing 기능으로
 *   저장/수정 시점에 시각을 자동으로 채운다(JpaAuditingConfig 에서 활성화).
 *   서비스 코드에 setCreatedAt() 같은 호출이 없어도 된다는 것이 핵심이다.
 */
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {
    // abstract는 이 양식만으로 자료를 만들지 않고, User나 Item 같은 실제 자료가 물려받아 쓰게 합니다.

    /** 최초 저장 시각. 이후 수정되지 않는다(updatable = false). */
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    /** 마지막 수정 시각. 수정이 없는 조회에는 변하지 않는다. */
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
