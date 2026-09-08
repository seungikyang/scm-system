package com.example.scm.domain;

import com.example.scm.common.entity.BaseTimeEntity;
import com.example.scm.domain.enums.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 사용자 엔티티. users 테이블의 한 행과 1:1로 대응되는 클래스다.
 *
 * <p>학습 모듈 01: BaseTimeEntity → UserRole → 이 클래스 순서로 읽는다. 로그인에서 이
 * 엔티티를 사용하는 과정은 기반 엔티티 학습을 마친 뒤 모듈 23에서 이어진다.</p>
 *
 * 초보자 포인트:
 * - password 는 평문을 절대 저장하지 않고 BCrypt 해시를 저장한다 (AuthService 참고).
 * - 엔티티를 세션이나 API 응답에 그대로 노출하지 않는다 → LoginUser / UserResponse DTO 로
 *   변환해서 전달한다(필요한 필드만 노출 + 엔티티 변경이 응답에 파급되지 않게).
 */
@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    /** DB 가 id 를 순서대로 자동 채번(AUTO_INCREMENT)한다. 코드에서 직접 지정하지 않는다. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 로그인 식별자. unique 제약으로 동일 이메일 가입을 DB 차원에서 막는다. */
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    /** BCrypt 해시 문자열. 절대 평문이 아니다. */
    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 50)
    private String name;

    /**
     * enum 저장은 반드시 STRING 으로. 기본값인 ORDINAL(숫자 번호)은 상수 순서가 바뀌면
     * 기존 데이터의 의미가 통째로 밀려나므로 실무에서 금지하는 관례다.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserRole role;

    /**
     * Lombok @Builder 생성자. new User(...) 직접 호출 대신
     * User.builder().email(...).build() 형태로 어떤 값이 들어가는지 읽기 쉽게 만든다.
     * (JPA 요구사항 때문에 기본 생성자는 protected 로 숨겨 둔다 — @NoArgsConstructor)
     */
    @Builder
    public User(String email, String password, String name, UserRole role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    public void changePassword(String encodedPassword) {
        this.password = encodedPassword;
    }
}
