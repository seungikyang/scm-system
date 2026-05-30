package com.example.scm.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.example.scm.repository.PurchaseOrderRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * 발주번호 채번(OQ-14) 단위 테스트 — 형식 PO-YYYYMMDD-#### / 일자별 4자리 시퀀스.
 * 진실원: 02_architect_datamodel §7.
 * 실행은 JDK17 환경 권장(환경 제약 동일).
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("OrderNumberGenerator 단위 테스트")
class OrderNumberGeneratorTest {

    @Mock
    private PurchaseOrderRepository purchaseOrderRepository;

    @InjectMocks
    private OrderNumberGenerator generator;

    @Test
    @DisplayName("당일 첫 번호: 기존 0건 → PO-YYYYMMDD-0001")
    void firstOfDay() {
        given(purchaseOrderRepository.countByOrderNumberStartingWith("PO-20260601-")).willReturn(0L);
        String result = generator.generate(LocalDate.parse("2026-06-01"));
        assertThat(result).isEqualTo("PO-20260601-0001");
    }

    @Test
    @DisplayName("당일 N번째 번호: 기존 7건 → PO-YYYYMMDD-0008 (4자리 0패딩)")
    void nthOfDay() {
        given(purchaseOrderRepository.countByOrderNumberStartingWith("PO-20260601-")).willReturn(7L);
        String result = generator.generate(LocalDate.parse("2026-06-01"));
        assertThat(result).isEqualTo("PO-20260601-0008");
    }

    @Test
    @DisplayName("일자별 prefix 분리: 다른 날짜는 독립 시퀀스")
    void perDatePrefix() {
        given(purchaseOrderRepository.countByOrderNumberStartingWith("PO-20261231-")).willReturn(123L);
        String result = generator.generate(LocalDate.parse("2026-12-31"));
        assertThat(result).isEqualTo("PO-20261231-0124");
    }
}
