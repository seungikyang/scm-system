package com.example.scm.repository;

import com.example.scm.domain.Category;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 카테고리 DB 접근 인터페이스. 메서드 이름에 담긴 조건과 정렬 규칙을 Spring Data JPA가
 * 읽어 쿼리로 만든다. 예: {@code findAllByOrderByNameAsc}는 이름 오름차순 전체 조회다.
 * 학습 모듈 31에서 PartnerRepository와 비교해 Specification이 필요 없는 이유를 생각한다.
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByName(String name);

    List<Category> findAllByOrderByNameAsc();
}
