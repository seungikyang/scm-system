package com.example.scm.repository;

import com.example.scm.domain.Category;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByName(String name);

    List<Category> findAllByOrderByNameAsc();
}
