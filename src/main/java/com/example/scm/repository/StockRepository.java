package com.example.scm.repository;

import com.example.scm.domain.Stock;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Long> {

    Optional<Stock> findByItemId(Long itemId);
}
