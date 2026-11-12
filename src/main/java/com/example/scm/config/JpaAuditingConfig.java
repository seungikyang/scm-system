package com.example.scm.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * 자료를 만들거나 수정한 시각을 자동으로 적는 기능을 켭니다.
 * BaseTimeEntity의 시간 칸을 사람이 매번 채우지 않아도 되게 하는 설정입니다.
 *
 * JPA Auditing(생성·수정 시각 자동 기록)을 켜는 설정.
 * 이 설정이 있어야 {@code BaseTimeEntity}의 {@code @CreatedDate}와
 * {@code @LastModifiedDate}가 저장 시점에 실제 값으로 채워진다.
 * 학습 모듈 33의 첫 파일로 읽고, 모듈 01에서 보았던 BaseTimeEntity와 연결한다.
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
}
