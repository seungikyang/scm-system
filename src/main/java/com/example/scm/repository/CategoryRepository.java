package com.example.scm.repository;

import com.example.scm.domain.Category;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * 분류 자료를 데이터베이스에서 찾거나 저장하는 창구입니다.
 * 데이터베이스는 자료를 모아 두는 전자 장부입니다. Spring이 함수 이름이나 조회식을 보고 실제 작업을 준비합니다.
 *
 * 카테고리 DB 접근 인터페이스. 메서드 이름에 담긴 조건과 정렬 규칙을 Spring Data JPA가
 * 읽어 쿼리로 만든다. 예: {@code findAllByOrderByNameAsc}는 이름 오름차순 전체 조회다.
 * 학습 모듈 31에서 PartnerRepository와 비교해 Specification이 필요 없는 이유를 생각한다.
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByName(String name);

    // 수정 중인 분류 자신(id)을 빼고 같은 이름이 있는지 확인한다.
    boolean existsByNameAndIdNot(String name, Long id);

    List<Category> findAllByOrderByNameAsc();

    // 품목을 이 분류에 넣는 동안 분류가 삭제되지 않도록 읽기 잠금을 겁니다.
    // 여러 품목 저장은 서로 기다리지 않고, 분류 삭제만 품목 저장이 끝날 때까지 기다립니다.
    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("select c from Category c where c.id = :id")
    Optional<Category> findByIdForShare(@Param("id") Long id);

    // 분류를 삭제하기 전에 수정용 잠금을 걸어, 진행 중인 품목 저장이 끝난 뒤의 품목 수를 세게 합니다.
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from Category c where c.id = :id")
    Optional<Category> findByIdForUpdate(@Param("id") Long id);
}
