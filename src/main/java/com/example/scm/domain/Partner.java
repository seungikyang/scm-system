package com.example.scm.domain;

import com.example.scm.common.entity.BaseTimeEntity;
import com.example.scm.domain.enums.PartnerStatus;
import com.example.scm.domain.enums.PartnerType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "partners")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Partner extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(name = "business_number", nullable = false, unique = true, length = 20)
    private String businessNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "partner_type", nullable = false, length = 20)
    private PartnerType partnerType;

    @Column(name = "contact_name", length = 50)
    private String contactName;

    @Column(length = 30)
    private String phone;

    @Column(length = 100)
    private String email;

    @Column(length = 255)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PartnerStatus status;

    @Builder
    public Partner(String name, String businessNumber, PartnerType partnerType,
                   String contactName, String phone, String email, String address,
                   PartnerStatus status) {
        this.name = name;
        this.businessNumber = businessNumber;
        this.partnerType = partnerType;
        this.contactName = contactName;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.status = (status != null) ? status : PartnerStatus.ACTIVE;
    }

    public void update(String name, PartnerType partnerType, String contactName,
                       String phone, String email, String address) {
        this.name = name;
        this.partnerType = partnerType;
        this.contactName = contactName;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    public void deactivate() {
        this.status = PartnerStatus.INACTIVE;
    }

    public boolean isActive() {
        return this.status == PartnerStatus.ACTIVE;
    }

    public boolean canSupply() {
        return this.partnerType == PartnerType.SUPPLIER || this.partnerType == PartnerType.BOTH;
    }
}
