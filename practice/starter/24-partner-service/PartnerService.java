// 실제 구현 위치 예: src/main/java/com/example/scm/service/PartnerService.java
// 목표: 거래처 CRUD + 사업자번호 unique + 삭제 정책을 채우세요. PRD 2.5.2, TRD 3.7.2 참고.

@Service
@RequiredArgsConstructor
public class PartnerService {

    private final PartnerRepository partnerRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final SalesOrderRepository salesOrderRepository;

    @Transactional
    public PartnerResponse register(PartnerCreateRequest request) {
        String businessNumber = request.getBusinessNumber().replaceAll("\\s+", "");

        // TODO 01: 사업자번호 중복 검사.
        if (partnerRepository.existsBy____(businessNumber)) {
            throw new BusinessException(ErrorCode.____);
        }

        Partner partner = Partner.create(
            request.getName().trim(),
            businessNumber,
            request.getPartnerType(),
            request.getContactName(),
            request.getPhone(),
            request.getEmail(),
            request.getAddress()
        );

        return PartnerResponse.from(partnerRepository.save(partner));
    }

    @Transactional(readOnly = true)
    public Page<PartnerResponse> search(String keyword, PartnerType type, Pageable pageable) {
        String trimmed = (keyword == null) ? "" : keyword.trim();

        Page<Partner> page;
        if (trimmed.isBlank() && type == null) {
            page = partnerRepository.findAll(pageable);
        } else if (!trimmed.isBlank() && type == null) {
            // TODO 02: 이름 또는 사업자번호 contains 검색.
            page = partnerRepository.findByNameContainingOrBusinessNumberContaining(
                trimmed, trimmed, pageable
            );
        } else if (trimmed.isBlank() && type != null) {
            page = partnerRepository.findByPartnerType(type, pageable);
        } else {
            // TODO 03: 두 조건 동시일 때 사용할 시그니처를 적어 보세요.
            // A:
            page = partnerRepository.findAll(pageable);
        }
        return page.map(PartnerResponse::from);
    }

    @Transactional(readOnly = true)
    public PartnerResponse getDetail(Long partnerId) {
        Partner partner = partnerRepository.findById(partnerId)
            .orElseThrow(() -> new BusinessException(ErrorCode.PARTNER_NOT_FOUND));
        return PartnerResponse.from(partner);
    }

    @Transactional
    public PartnerResponse update(Long partnerId, PartnerUpdateRequest request) {
        Partner partner = partnerRepository.findById(partnerId)
            .orElseThrow(() -> new BusinessException(ErrorCode.PARTNER_NOT_FOUND));

        // TODO 04: 사업자번호 변경 시 중복 재검사.
        if (request.getBusinessNumber() != null
                && !request.getBusinessNumber().equals(partner.getBusinessNumber())
                && partnerRepository.existsByBusinessNumber(request.getBusinessNumber())) {
            throw new BusinessException(ErrorCode.____);
        }

        partner.update(
            request.getName(),
            request.getBusinessNumber(),
            request.getPartnerType(),
            request.getContactName(),
            request.getPhone(),
            request.getEmail(),
            request.getAddress()
        );
        return PartnerResponse.from(partner);
    }

    @Transactional
    public void deactivate(Long partnerId) {
        Partner partner = partnerRepository.findById(partnerId)
            .orElseThrow(() -> new BusinessException(ErrorCode.PARTNER_NOT_FOUND));

        // TODO 05: 발주/수주 이력이 있으면 hard delete 대신 INACTIVE 처리.
        long poCount = purchaseOrderRepository.countByPartner_Id(partnerId);
        long soCount = salesOrderRepository.countByPartner_Id(partnerId);
        if (poCount > 0 || soCount > 0) {
            partner.____();
            return;
        }
        partnerRepository.delete(partner);
    }
}

// 학습 질문:
// Q1. 사업자번호의 공백/하이픈을 정규화하는 시점은? (DTO / Service / Entity)
//     A:
// Q2. 발주 이력이 남은 거래처를 hard delete 하면 어떤 문제가 생기는가?
//     A:
// Q3. 거래처 유형(SUPPLIER → BOTH) 변경은 OK 인데, BOTH → SUPPLIER 는 막아야 한다면 어디서?
//     A:
