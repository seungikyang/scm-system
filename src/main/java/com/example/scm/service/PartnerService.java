package com.example.scm.service;

import com.example.scm.common.validation.PageableValidator;

import com.example.scm.common.auth.Authz;
import com.example.scm.common.auth.LoginUser;
import com.example.scm.common.exception.BusinessException;
import com.example.scm.common.exception.ErrorCode;
import com.example.scm.domain.Partner;
import com.example.scm.domain.enums.PartnerStatus;
import com.example.scm.domain.enums.UserRole;
import com.example.scm.dto.partner.PartnerCreateRequest;
import com.example.scm.dto.partner.PartnerDetailView;
import com.example.scm.dto.partner.PartnerForm;
import com.example.scm.dto.partner.PartnerListView;
import com.example.scm.dto.partner.PartnerSearchForm;
import com.example.scm.dto.partner.PartnerUpdateRequest;
import com.example.scm.repository.PartnerRepository;
import com.example.scm.repository.spec.PartnerSpecs;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 물건을 공급하거나 사 가는 회사인 거래처를 관리합니다.
 * 중복 회사 번호나 권한을 검사하고 저장합니다. 비활성화는 과거 기록을 남긴 채 새 거래를 막는 처리입니다.
 *
 * 거래처 조회와 등록·수정·비활성화 규칙을 담당하는 서비스.
 *
 * <p>학습 모듈 24: create → search/detail → update → deactivate 순서로 읽는다. 다음
 * 모듈 25에서 같은 Service를 호출하는 REST/Web Controller를 비교한다.</p>
 *
 * <p>REST용 DTO와 웹 폼용 DTO를 각각 받는 오버로드가 있지만, 두 경로 모두 여기에서
 * ADMIN 권한과 사업자번호 중복을 확인한다. 거래처 삭제 대신 비활성화를 사용해 과거
 * 발주가 참조하는 데이터를 보존한다.</p>
 */
@Service
@RequiredArgsConstructor
public class PartnerService {

    private final PartnerRepository partnerRepository;

    // ===== 모듈 24-1: 등록 (ADMIN only) =====

    @Transactional
    public Long create(PartnerForm form, LoginUser loginUser) {
        Authz.requireRole(loginUser, UserRole.ADMIN);
        String businessNumber = trimmed(form.getBusinessNumber());
        validateBusinessNumberUnique(businessNumber);

        Partner partner = Partner.builder()
                .name(trimmed(form.getName()))
                .businessNumber(businessNumber)
                .partnerType(form.getPartnerType())
                .contactName(form.getContactName())
                .phone(form.getPhone())
                .email(form.getEmail())
                .address(form.getAddress())
                .status(PartnerStatus.ACTIVE)
                .build();
        return saveNewPartner(partner);
    }

    @Transactional
    public Long create(PartnerCreateRequest request, LoginUser loginUser) {
        Authz.requireRole(loginUser, UserRole.ADMIN);
        String businessNumber = trimmed(request.getBusinessNumber());
        validateBusinessNumberUnique(businessNumber);

        Partner partner = Partner.builder()
                .name(trimmed(request.getName()))
                .businessNumber(businessNumber)
                .partnerType(request.getPartnerType())
                .contactName(request.getContactName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .address(request.getAddress())
                .status(PartnerStatus.ACTIVE)
                .build();
        return saveNewPartner(partner);
    }

    // ===== 모듈 24-2: 목록과 상세 조회 =====

    @Transactional(readOnly = true)
    public Page<PartnerListView> search(PartnerSearchForm form, Pageable pageable) {
        PageableValidator.validate(pageable, PageableValidator.PARTNER);
        // pageable은 몇 번째 페이지를 몇 건씩 볼지 정합니다. 검색 결과를 화면용 요약으로 바꿉니다.
        return partnerRepository.findAll(PartnerSpecs.search(form), pageable)
                .map(PartnerListView::from);
    }

    @Transactional(readOnly = true)
    public PartnerDetailView getDetail(Long partnerId) {
        return PartnerDetailView.from(getEntity(partnerId));
    }

    @Transactional(readOnly = true)
    public PartnerForm getForm(Long partnerId) {
        return PartnerForm.from(getEntity(partnerId));
    }

    // ===== 모듈 24-3: 수정과 비활성화 (ADMIN only) =====

    @Transactional
    public void update(Long partnerId, PartnerForm form, LoginUser loginUser) {
        Authz.requireRole(loginUser, UserRole.ADMIN);
        Partner partner = getEntity(partnerId);
        // 영속 엔티티를 변경하면 트랜잭션 종료 시 JPA가 UPDATE SQL을 만든다.
        partner.update(trimmed(form.getName()), form.getPartnerType(), form.getContactName(),
                form.getPhone(), form.getEmail(), form.getAddress());
    }

    @Transactional
    public void update(Long partnerId, PartnerUpdateRequest request, LoginUser loginUser) {
        Authz.requireRole(loginUser, UserRole.ADMIN);
        Partner partner = getEntity(partnerId);
        partner.update(trimmed(request.getName()), request.getPartnerType(), request.getContactName(),
                request.getPhone(), request.getEmail(), request.getAddress());
    }

    @Transactional
    public void deactivate(Long partnerId, LoginUser loginUser) {
        Authz.requireRole(loginUser, UserRole.ADMIN);
        Partner partner = getEntity(partnerId);
        // 참조 이력을 지우지 않는 소프트 삭제(상태 변경) 방식이다.
        partner.deactivate();
    }

    // ===== 대시보드 집계 =====

    @Transactional(readOnly = true)
    public long countAll() {
        return partnerRepository.count();
    }

    // ===== 내부 헬퍼 =====

    private Partner getEntity(Long partnerId) {
        return partnerRepository.findById(partnerId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PARTNER_NOT_FOUND));
    }

    // existsByBusinessNumber 확인과 저장 사이에 다른 요청이 같은 사업자번호를 먼저 저장할 수 있습니다.
    // 그때는 DB unique 제약이 막아 주므로, 서버 오류(500) 대신 중복 사업자번호 오류로 바꿔 알려 줍니다.
    private Long saveNewPartner(Partner partner) {
        try {
            return partnerRepository.saveAndFlush(partner).getId();
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException(ErrorCode.DUPLICATE_BUSINESS_NUMBER);
        }
    }

    // 앞뒤 공백만 다른 값(" 전자부품 "과 "전자부품")이 서로 다른 자료로 등록되지 않도록
    // 이름·코드처럼 사람을 구별하는 값은 저장하고 비교하기 전에 앞뒤 공백을 지웁니다.
    private static String trimmed(String value) {
        return value == null ? null : value.trim();
    }

    private void validateBusinessNumberUnique(String businessNumber) {
        if (partnerRepository.existsByBusinessNumber(businessNumber)) {
            throw new BusinessException(ErrorCode.DUPLICATE_BUSINESS_NUMBER);
        }
    }

}
