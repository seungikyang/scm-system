package com.example.scm.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.example.scm.common.auth.LoginUser;
import com.example.scm.common.exception.BusinessException;
import com.example.scm.common.exception.ErrorCode;
import com.example.scm.domain.Partner;
import com.example.scm.domain.enums.PartnerStatus;
import com.example.scm.domain.enums.PartnerType;
import com.example.scm.domain.enums.UserRole;
import com.example.scm.dto.partner.PartnerForm;
import com.example.scm.repository.PartnerRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
@DisplayName("PartnerService 단위 테스트")
class PartnerServiceTest {

    @Mock
    private PartnerRepository partnerRepository;

    @InjectMocks
    private PartnerService service;

    private LoginUser admin;
    private LoginUser user;

    @BeforeEach
    void setUp() {
        admin = new LoginUser(1L, "관리자", "admin@scm.com", UserRole.ADMIN);
        user = new LoginUser(10L, "사용자", "user@scm.com", UserRole.USER);
    }

    @Test
    @DisplayName("ADMIN은 중복되지 않은 사업자번호로 활성 거래처를 등록한다")
    void create_success() {
        given(partnerRepository.existsByBusinessNumber("123-45-67890")).willReturn(false);
        given(partnerRepository.save(any(Partner.class))).willAnswer(invocation -> {
            Partner partner = invocation.getArgument(0);
            ReflectionTestUtils.setField(partner, "id", 5L);
            return partner;
        });

        Long partnerId = service.create(form(), admin);

        assertThat(partnerId).isEqualTo(5L);
    }

    @Test
    @DisplayName("중복 사업자번호는 저장 전에 차단한다")
    void create_duplicateBusinessNumber() {
        given(partnerRepository.existsByBusinessNumber("123-45-67890")).willReturn(true);

        assertCode(() -> service.create(form(), admin), ErrorCode.DUPLICATE_BUSINESS_NUMBER);
        verify(partnerRepository, never()).save(any());
    }

    @Test
    @DisplayName("일반 사용자는 거래처를 등록할 수 없다")
    void create_byUserDenied() {
        assertCode(() -> service.create(form(), user), ErrorCode.ACCESS_DENIED);
        verify(partnerRepository, never()).existsByBusinessNumber(any());
    }

    @Test
    @DisplayName("거래처 삭제 요청은 이력을 보존하는 비활성화로 처리한다")
    void deactivate_success() {
        Partner partner = partner(5L);
        given(partnerRepository.findById(5L)).willReturn(Optional.of(partner));

        service.deactivate(5L, admin);

        assertThat(partner.getStatus()).isEqualTo(PartnerStatus.INACTIVE);
    }

    @Test
    @DisplayName("존재하지 않는 거래처 비활성화는 PARTNER_NOT_FOUND다")
    void deactivate_notFound() {
        given(partnerRepository.findById(404L)).willReturn(Optional.empty());

        assertCode(() -> service.deactivate(404L, admin), ErrorCode.PARTNER_NOT_FOUND);
    }

    private PartnerForm form() {
        PartnerForm form = new PartnerForm();
        form.setName("테스트 공급사");
        form.setBusinessNumber("123-45-67890");
        form.setPartnerType(PartnerType.SUPPLIER);
        form.setContactName("담당자");
        form.setPhone("02-1234-5678");
        form.setEmail("partner@example.com");
        form.setAddress("서울시");
        return form;
    }

    private Partner partner(Long id) {
        Partner partner = Partner.builder()
                .name("테스트 공급사")
                .businessNumber("123-45-67890")
                .partnerType(PartnerType.SUPPLIER)
                .status(PartnerStatus.ACTIVE)
                .build();
        ReflectionTestUtils.setField(partner, "id", id);
        return partner;
    }

    private void assertCode(org.junit.jupiter.api.function.Executable executable, ErrorCode expected) {
        assertThatThrownBy(executable::execute)
                .isInstanceOf(BusinessException.class)
                .extracting(error -> ((BusinessException) error).getErrorCode())
                .isEqualTo(expected);
    }
}
