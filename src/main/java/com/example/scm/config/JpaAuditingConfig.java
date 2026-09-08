package com.example.scm.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * JPA Auditing(생성·수정 시각 자동 기록)을 켜는 설정.
 * 이 설정이 있어야 {@code BaseTimeEntity}의 {@code @CreatedDate}와
 * {@code @LastModifiedDate}가 저장 시점에 실제 값으로 채워진다.
 * 학습 모듈 33의 첫 파일로 읽고, 모듈 01에서 보았던 BaseTimeEntity와 연결한다.
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
}
