package com.example.scm.repository;

import com.example.scm.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 사용자 엔티티의 DB 접근 창구.
 *
 * <p>학습 모듈 23에서는 findByEmail의 로그인 사용만 확인하고, 모듈 31에서
 * JpaRepository 기본 메서드와 Optional 반환 규칙까지 다시 정리한다.</p>
 *
 * <p>구현 클래스가 없어도 Spring Data JPA가 실행 시점에 구현체를 만든다.
 * {@code findByEmail}처럼 정해진 규칙으로 메서드 이름을 쓰면 이름을 분석해
 * {@code where email = ?} 쿼리도 자동 생성한다.</p>
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /** 결과가 없을 수 있으므로 null 대신 Optional로 반환한다. */
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
