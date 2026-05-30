package com.example.scm.service;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PartnerService {

    private final PartnerRepository partnerRepository;

    // ===== 조회 =====

    @Transactional(readOnly = true)
    public Page<PartnerListView> search(PartnerSearchForm form, Pageable pageable) {
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

    // ===== 변경 (ADMIN only) =====

    @Transactional
    public Long create(PartnerForm form, LoginUser loginUser) {
        Authz.requireRole(loginUser, UserRole.ADMIN);
        validateBusinessNumberUnique(form.getBusinessNumber());

        Partner partner = Partner.builder()
                .name(form.getName())
                .businessNumber(form.getBusinessNumber())
                .partnerType(form.getPartnerType())
                .contactName(form.getContactName())
                .phone(form.getPhone())
                .email(form.getEmail())
                .address(form.getAddress())
                .status(PartnerStatus.ACTIVE)
                .build();
        return partnerRepository.save(partner).getId();
    }

    @Transactional
    public Long create(PartnerCreateRequest request, LoginUser loginUser) {
        Authz.requireRole(loginUser, UserRole.ADMIN);
        validateBusinessNumberUnique(request.getBusinessNumber());

        Partner partner = Partner.builder()
                .name(request.getName())
                .businessNumber(request.getBusinessNumber())
                .partnerType(request.getPartnerType())
                .contactName(request.getContactName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .address(request.getAddress())
                .status(PartnerStatus.ACTIVE)
                .build();
        return partnerRepository.save(partner).getId();
    }

    @Transactional
    public void update(Long partnerId, PartnerForm form, LoginUser loginUser) {
        Authz.requireRole(loginUser, UserRole.ADMIN);
        Partner partner = getEntity(partnerId);
        partner.update(form.getName(), form.getPartnerType(), form.getContactName(),
                form.getPhone(), form.getEmail(), form.getAddress());
    }

    @Transactional
    public void update(Long partnerId, PartnerUpdateRequest request, LoginUser loginUser) {
        Authz.requireRole(loginUser, UserRole.ADMIN);
        Partner partner = getEntity(partnerId);
        partner.update(request.getName(), request.getPartnerType(), request.getContactName(),
                request.getPhone(), request.getEmail(), request.getAddress());
    }

    @Transactional
    public void deactivate(Long partnerId, LoginUser loginUser) {
        Authz.requireRole(loginUser, UserRole.ADMIN);
        Partner partner = getEntity(partnerId);
        partner.deactivate();
    }

    // ===== 내부 헬퍼 =====

    private Partner getEntity(Long partnerId) {
        return partnerRepository.findById(partnerId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PARTNER_NOT_FOUND));
    }

    private void validateBusinessNumberUnique(String businessNumber) {
        if (partnerRepository.existsByBusinessNumber(businessNumber)) {
            throw new BusinessException(ErrorCode.DUPLICATE_BUSINESS_NUMBER);
        }
    }

    @Transactional(readOnly = true)
    public long countAll() {
        return partnerRepository.count();
    }
}
