// 실제 구현 위치 예: src/main/java/com/example/scm/domain/Partner.java
// 목표: 거래처 엔티티와 PartnerType / PartnerStatus enum 을 채우세요.
//       TRD 3.3.2, 3.5.2, 3.6.2 참고.
//
// 주의: 02 폴더 안에 Category 와 Partner 가 함께 있습니다.
//       두 엔티티 모두 마스터 데이터지만 도메인 의미가 다르므로 별도 파일로 두었습니다.

// ===== PartnerType enum =====
public enum PartnerType {
    // TODO 01: 공급사 / 고객사 / 양쪽 세 가지를 채우세요.
    ____, ____, ____
}

// ===== PartnerStatus enum =====
public enum PartnerStatus {
    // TODO 02: 활성 / 비활성 두 가지를 채우세요.
    ____, ____
}

// ===== Partner entity =====
@Entity
@Table(name = "partners",
       uniqueConstraints = {
           // TODO 03: 사업자번호는 유일 식별자입니다. 어떤 제약을 추가할까요?
           @UniqueConstraint(name = "uk_partner_business_number", columnNames = "____")
       })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Partner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    // TODO 04: 사업자번호 컬럼. 길이/유일성 제약을 채우세요.
    @Column(nullable = false, length = ____, unique = true)
    private String businessNumber;

    // TODO 05: enum 을 DB 에 어떻게 저장하면 enum 순서 변경에도 안전할까요?
    @Enumerated(EnumType.____)
    @Column(nullable = false, length = 20)
    private PartnerType partnerType;

    private String contactName;
    private String phone;
    private String email;
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PartnerStatus status;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    // ===== 정적 팩토리 =====
    public static Partner create(String name,
                                 String businessNumber,
                                 PartnerType partnerType,
                                 String contactName,
                                 String phone,
                                 String email,
                                 String address) {
        Partner p = new Partner();
        p.name = name.trim();
        // TODO 06: 사업자번호의 공백을 제거하는 시점은? DTO / Service / Entity 중 어디?
        p.businessNumber = businessNumber.replaceAll("\\s+", "");
        // TODO 07: partnerType 이 null 이면 어떤 기본값?
        p.partnerType = (partnerType == null) ? PartnerType.____ : partnerType;
        p.contactName = contactName;
        p.phone = phone;
        p.email = email;
        p.address = address;
        // TODO 08: 신규 거래처의 초기 상태는?
        p.status = PartnerStatus.____;
        return p;
    }

    // ===== 도메인 메서드 =====
    public void update(String name,
                       String businessNumber,
                       PartnerType partnerType,
                       String contactName,
                       String phone,
                       String email,
                       String address) {
        if (name != null && !name.isBlank()) this.name = name.trim();
        if (businessNumber != null && !businessNumber.isBlank()) {
            this.businessNumber = businessNumber.replaceAll("\\s+", "");
        }
        if (partnerType != null) this.partnerType = partnerType;
        this.contactName = contactName;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    public void deactivate() {
        // TODO 09: 이미 INACTIVE 인 거래처를 다시 비활성화하면 어떻게 할까요?
        if (this.status == PartnerStatus.____) {
            throw new IllegalStateException("이미 비활성 상태입니다.");
        }
        this.status = PartnerStatus.INACTIVE;
    }

    public void activate() {
        this.status = PartnerStatus.ACTIVE;
    }

    // ===== 도메인 규칙 헬퍼 =====
    public boolean canSupply() {
        // TODO 10: 발주(매입) 가능한 유형은 SUPPLIER 또는 ____ 이다.
        return this.partnerType == PartnerType.SUPPLIER
            || this.partnerType == PartnerType.____;
    }

    public boolean canBuy() {
        // 수주(판매) 가능한 유형은 CUSTOMER 또는 BOTH.
        return this.partnerType == PartnerType.CUSTOMER
            || this.partnerType == PartnerType.BOTH;
    }
}

// 학습 질문:
// Q1. 사업자번호의 공백/하이픈 정규화를 Service 가 아닌 Entity 에서 하는 트레이드오프는?
//     A:
// Q2. canSupply()/canBuy() 같은 도메인 헬퍼를 두는 이유는?
//     A:
// Q3. PartnerType 을 SUPPLIER / CUSTOMER / BOTH 세 enum 으로 둔 vs SUPPLIER + CUSTOMER 두 개 Flag 컬럼으로 둔 차이는?
//     A:
// Q4. INACTIVE 거래처로 작성된 과거 발주/수주 이력을 어떻게 처리해야 하는가?
//     A:
