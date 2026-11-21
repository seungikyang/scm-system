package com.example.scm.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.scm.common.exception.BusinessException;
import com.example.scm.common.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Stock 도메인 테스트")
class StockTest {

    @Test
    @DisplayName("재고 증가 시 Integer overflow를 음수 재고로 저장하지 않는다")
    void increase_rejectsOverflow() {
        Stock stock = new Stock(1L, Integer.MAX_VALUE);

        assertThatThrownBy(() -> stock.increase(1))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> ((BusinessException) e).getErrorCode())
                .isEqualTo(ErrorCode.INVALID_INPUT);
        assertThat(stock.getQuantity()).isEqualTo(Integer.MAX_VALUE);
    }

    @Test
    @DisplayName("0 이하 입고 수량을 거부한다")
    void increase_rejectsNonPositiveAmount() {
        Stock stock = new Stock(1L, 10);

        assertThatThrownBy(() -> stock.increase(0))
                .isInstanceOf(BusinessException.class);
        assertThat(stock.getQuantity()).isEqualTo(10);
    }
}
