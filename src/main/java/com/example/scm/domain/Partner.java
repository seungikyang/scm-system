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

/**
 * 거래처 엔티티. 물건을 공급하는 곳(SUPPLIER), 사가는 곳(CUSTOMER), 둘 다(BOTH)를 구분한다.
 *
 * <p>학습 모듈 02: PartnerType과 PartnerStatus를 먼저 보고 이 클래스의 canSupply와
 * deactivate가 enum을 어떻게 사용하는지 확인한다. CRUD 흐름은 모듈 24에서 이어진다.</p>
 *
 * 초보자 포인트:
 * - 거래처는 DELETE 하지 않고 status=INACTIVE 로만 비활성화한다.
 *   과거 발주 이력이 여전히 이 행을 참조하므로 데이터를 남겨야 참조 무결성이 깨지지 않는다.
 * - businessNumber(사업자번호)에 unique 제약: 같은 사업자의 중복 등록을 DB 차원에서 막는다.
 */
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

    /** 도메인 헬퍼: 이 거래처가 "파는 쪽"인가? 발주 작성 시 공급사 선택 목록 제한에 사용된다. */
    public boolean canSupply() {
        return this.partnerType == PartnerType.SUPPLIER || this.partnerType == PartnerType.BOTH;
    }
}
