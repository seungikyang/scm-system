package com.example.scm.domain;

import com.example.scm.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 품목 분류 카테고리. name 에 unique 제약으로 동일 분류명 중복을 막는다.
 *
 * <p>학습 모듈 02: Category를 먼저 읽고 Partner로 이동한다. 이 단계에서는 컬럼과
 * unique 정책에 집중하고 CRUD Service는 모듈 24에서 확인한다.</p>
 *
 * 초보자 포인트: Item 과의 연관관계는 Item.categoryId(Long) 한 방향으로만 둔다.
 * Category 에 @OneToMany 목록을 두면 카테고리 조회만으로 품목 전체가 로딩될 수 있어
 * (N+1 / 불필요한 메모리) 이 저장소는 의도적으로 단방향 ID 참조를 선택했다.
 */
@Entity
@Getter
@Table(name = "categories")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(length = 255)
    private String description;

    @Builder
    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void update(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
