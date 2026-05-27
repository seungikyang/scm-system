// 실제 구현 위치 예: src/main/java/com/example/scm/repository/*
// 목표: PRD/TRD 의 모든 도메인 Repository 를 메서드 이름 쿼리로 정리하세요.
//       07-repository 의 ItemRepository 와 짝이 되는 여섯 가지 Repository 입니다.

// =====================================================================
// 1. UserRepository
// =====================================================================
public interface UserRepository extends JpaRepository<User, Long> {

    // TODO 01: 로그인용 이메일 조회.
    Optional<User> findBy____(String email);

    // TODO 02: 회원가입 시 중복 검사.
    boolean existsBy____(String email);
}

// =====================================================================
// 2. PartnerRepository (거래처)
// =====================================================================
public interface PartnerRepository extends JpaRepository<Partner, Long> {

    // TODO 03: 사업자번호 중복 검사.
    boolean existsByBusinessNumber(String businessNumber);

    // TODO 04: 사업자번호로 조회. (관리자 화면 자동완성 등)
    Optional<Partner> findByBusinessNumber(String businessNumber);

    // TODO 05: 유형 필터 (SUPPLIER/CUSTOMER/BOTH) + 페이징.
    Page<Partner> findByPartnerType(PartnerType type, Pageable pageable);

    // TODO 06: 이름 또는 사업자번호 contains 검색 + 페이징.
    Page<Partner> findByNameContainingOr____Containing(
            String nameKeyword,
            String businessNumberKeyword,
            Pageable pageable
    );
}

// =====================================================================
// 3. CategoryRepository
// =====================================================================
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // TODO 07: 카테고리명 중복 검사.
    boolean existsByName(String name);

    Optional<Category> findByName(String name);
}

// =====================================================================
// 4. ItemRepository (07 에서 다룬 메서드와 일관성 맞추기)
// =====================================================================
public interface ItemRepository extends JpaRepository<Item, Long> {

    boolean existsByItemCode(String itemCode);
    Optional<Item> findByItemCode(String itemCode);

    // TODO 08: 카테고리별 품목 수 — 카테고리 삭제 검증에 사용.
    long countByCategory_Id(Long categoryId);

    Page<Item> findByCategory_Id(Long categoryId, Pageable pageable);
    Page<Item> findByNameContainingOrItemCodeContaining(
            String nameKeyword, String codeKeyword, Pageable pageable);
    Page<Item> findByStatus(ItemStatus status, Pageable pageable);

    // TODO 09: 상세 조회용 fetch join (LazyInitializationException 방지).
    @Query("SELECT i FROM Item i JOIN ____ i.category WHERE i.id = :id")
    Optional<Item> findDetailById(@Param("id") Long id);
}

// =====================================================================
// 5. PurchaseOrderRepository
// =====================================================================
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {

    // TODO 10: 내 발주 목록 (페이징).
    Page<PurchaseOrder> findByWriterId(Long writerId, Pageable pageable);

    // TODO 11: 거래처별 발주 수 — 거래처 삭제 검증에 사용.
    long countByPartner_Id(Long partnerId);

    // TODO 12: 관리자용 — 상태별 / 거래처별 / (둘 다) 조회.
    Page<PurchaseOrder> findByStatus(PurchaseOrderStatus status, Pageable pageable);
    Page<PurchaseOrder> findByPartner_Id(Long partnerId, Pageable pageable);
    Page<PurchaseOrder> findByStatusAndPartner_Id(
            PurchaseOrderStatus status, Long partnerId, Pageable pageable);

    // TODO 13: 발주일 BETWEEN 검색.
    @Query("""
        SELECT po FROM PurchaseOrder po
        WHERE po.orderDate ____ :from AND :to
        ORDER BY po.orderDate DESC
        """)
    Page<PurchaseOrder> searchByOrderDateRange(
            @Param("from") LocalDate from,
            @Param("to") LocalDate to,
            Pageable pageable);

    // TODO 14: 헤더 + 라인 fetch join (상세 조회 N+1 방지).
    @Query("""
        SELECT DISTINCT po FROM PurchaseOrder po
        LEFT JOIN ____ po.lines
        WHERE po.id = :id
        """)
    Optional<PurchaseOrder> findDetailById(@Param("id") Long id);

    // TODO 15: 동일 발주 동시 승인 방지를 위한 비관적 락 조회.
    @Lock(LockModeType.____)
    @Query("SELECT po FROM PurchaseOrder po WHERE po.id = :id")
    Optional<PurchaseOrder> findByIdForUpdate(@Param("id") Long id);
}

// =====================================================================
// 6. NoticeRepository
// =====================================================================
public interface NoticeRepository extends JpaRepository<Notice, Long> {

    // 중요 공지를 위로 끌어올리려면 보통 어디서(Service 정렬 / Repository 쿼리) 해결할까요?
    // → 단순 정렬은 Pageable + Sort 로 충분합니다.
    //   복합 정렬이 자주 쓰이면 default Sort 빈을 두는 방식도 가능.

    // TODO 16: 조회수 원자적 증가 — 동시성 충돌 회피.
    @____
    @Query("UPDATE Notice n SET n.viewCount = n.viewCount + 1 WHERE n.id = :id")
    int increaseViewCount(@Param("id") Long id);
}

// =====================================================================
// 7. SalesOrderRepository
// =====================================================================
public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long> {

    // TODO 17: 내가 작성한 수주 목록.
    Page<SalesOrder> findByWriterId(Long writerId, Pageable pageable);
    Page<SalesOrder> findByWriterIdAndStatus(
            Long writerId, SalesOrderStatus status, Pageable pageable);

    // TODO 18: 매니저가 처리할 수주 목록.
    Page<SalesOrder> findByManagerIdAndStatus(
            Long managerId, SalesOrderStatus status, Pageable pageable);

    // TODO 19: 처리 대기 (status = CONFIRMED).
    Page<SalesOrder> findByStatus(SalesOrderStatus status, Pageable pageable);

    Page<SalesOrder> findByPartner_Id(Long partnerId, Pageable pageable);
    Page<SalesOrder> findByStatusAndPartner_Id(
            SalesOrderStatus status, Long partnerId, Pageable pageable);

    long countByPartner_Id(Long partnerId);

    // 학습 질문 (한 줄 답):
    // Q1. 메서드 이름이 너무 길어지면 어떤 시점에 @Query 로 옮길까?
    //     A:
    // Q2. Spring Data JPA 가 메서드 이름을 어떤 규칙으로 파싱하는지 한 줄로 적어 보세요.
    //     A:
    // Q3. 중간에 컬럼명이 바뀌었을 때, 메서드 이름 쿼리는 어떻게 깨지는가? (Property → Column 매핑 관점)
    //     A:
    // Q4. nested property 쿼리(`findByPartner_Id`) 의 SQL JOIN 형태는?
    //     A:
    // Q5. 발주번호 채번 동시성을 Repository 레벨에서 어떻게 막을까?
    //     A:
}
