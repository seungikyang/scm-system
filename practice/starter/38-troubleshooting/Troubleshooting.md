# 트러블슈팅 워크북 (TRD 3.16)

5건의 케이스 + 본인 1건을 PRD 형식(문제 → 원인 → 해결 → 배운 점) 으로 채우세요.

---

## 1. 발주 승인 상태 중복 처리

### 문제
이미 승인된 발주를 다시 승인하거나 반려할 수 있는 문제가 발생했다.

### 원인
승인/반려 처리 전에 현재 상태가 `REQUESTED` 인지 ____ 하지 않았다.

### 해결
도메인 메서드 `approve()`/`reject()` 안에 상태 가드를 추가했다.

```java
if (this.status != PurchaseOrderStatus.REQUESTED) {
    throw new IllegalStateException("____");
}
```

Service 는 이 `IllegalStateException` 을 잡아 `BusinessException(ErrorCode.____)` 로 변환했다.

### 배운 점
상태 전이는 ____ 안에 두면 호출자가 어떤 경로로 들어와도 같은 규칙이 적용된다.

---

## 2. 발주 라인 부분 저장

### 문제
발주서를 작성하다 라인 저장 중 오류가 발생하면 헤더만 남고 라인이 ____ 되는 경우가 있었다.

### 원인
Service 메서드에 ____ 어노테이션이 없어, 헤더 저장과 라인 저장이 다른 트랜잭션 또는 영속성 컨텍스트에서 실행되었다.

### 해결
- Service 메서드에 `@Transactional` 적용.
- `@OneToMany(cascade = ALL, orphanRemoval = true)` 로 헤더 저장 시 라인도 함께 저장.
- 라인 검증을 헤더 save 전에 모두 끝냄.

### 배운 점
헤더-라인 구조는 ____ 한 트랜잭션 단위로 묶어야 하며, 라인 검증을 헤더 저장 ____ 에 끝내는 편이 안전하다.

---

## 3. Entity 직접 반환으로 비밀번호 노출

### 문제
`GET /api/users/me` 응답에 ____ 가 그대로 들어갔다.

### 원인
Controller 가 User Entity 를 그대로 반환했고, Jackson 이 모든 필드를 직렬화했다.

### 해결
- 응답 전용 ____ DTO (`MyInfoResponse`) 를 만들고 필요한 필드만 노출.
- 정적 팩토리 `MyInfoResponse.from(user)` 로 변환 책임을 한 곳에 집중.

### 배운 점
Entity 와 응답 DTO 는 분리해야 ____ 사고를 줄일 수 있다.

---

## 4. LazyInitializationException

### 문제
품목 상세 응답에서 `Category.name` 에 접근하는 순간 ____ Exception 이 발생했다.

### 원인
- `@ManyToOne(fetch = LAZY)` 인데 트랜잭션이 이미 종료된 뒤 카테고리에 접근.
- `open-in-view: false` 설정.

### 해결
- DTO 변환을 ____ 트랜잭션 안에서 끝낸다.
- 또는 fetch join 으로 Category 를 함께 로드한다.

```java
@Query("SELECT i FROM Item i JOIN ____ i.category WHERE i.id = :id")
Optional<Item> findDetailById(Long id);
```

### 배운 점
LAZY 로딩은 영속성 컨텍스트가 살아있는 동안에만 접근 가능하다. ____ 변환을 Service 안에서 끝내자.

---

## 5. 거래처 유형 불일치 검증 누락

### 문제
`CUSTOMER` 유형만 가진 거래처로 ____ 를 작성하면 비즈니스 규칙에 어긋나는데 통과되었다.

### 원인
화면에서만 거래처 유형을 필터링했고, ____ 계층에서 검증하지 않았다.

### 해결
`PurchaseOrderService.create()` 에 다음 가드를 추가했다.

```java
if (partner.getPartnerType() != PartnerType.SUPPLIER
        && partner.getPartnerType() != PartnerType.____) {
    throw new BusinessException(ErrorCode.PARTNER_TYPE_MISMATCH);
}
```

### 배운 점
화면 검증은 ____ 가능하므로 Service 계층에 도메인 규칙을 반드시 두어야 한다.

---

## 6. (보너스) 발주번호 채번 동시성

### 문제
같은 날 두 사용자가 발주서를 거의 동시에 만들면 같은 ____ 번호가 채번되는 race 가 있었다.

### 원인
"오늘 발주 수 + 1" 패턴이 ____ 조건이 아니라서 두 호출이 같은 값을 읽음.

### 해결 후보(하나 선택):
- DB 시퀀스 / Auto Increment + 포맷팅
- 별도 채번 테이블에 `SELECT ... FOR UPDATE`
- unique 충돌 시 ____ 회 재시도 패턴

### 배운 점
문서 번호 채번은 ____ 이 핵심 위험이다. 인덱스/락/재시도 중 어떤 조합을 쓸지 도메인 요구에 맞춰 결정한다.

---

## 7. (본인 케이스)

직접 만난 트러블슈팅 1건을 동일 4단계로 작성하세요.

### 문제
____

### 원인
____

### 해결
____

### 배운 점
____

---

## 자가 점검

- 트러블슈팅 항목을 매 PR 마다 새로 발견하고 있는가? ____
- 같은 케이스를 다시 만나지 않게 어떤 노트/체크리스트를 둘 것인가? ____
- 이 5건 중 면접 때 30초 안에 설명할 수 있는 것은? ____
