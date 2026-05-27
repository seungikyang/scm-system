# 패키지 구조와 계층 책임 (TRD 3.2.2)

이 문서는 SCM 시스템의 패키지 구조와 계층 책임을 직접 채워 보는 빈칸입니다.

## 1. 두 가지 전략

### 1.1 계층 우선 (Layer-first)

```text
com.example.scm
├── controller
│   ├── ItemController
│   ├── PartnerController
│   ├── PurchaseOrderController
│   └── ...
├── service
│   ├── ItemService
│   ├── PartnerService
│   ├── PurchaseOrderService
│   └── ...
├── repository
│   ├── ItemRepository
│   ├── PartnerRepository
│   └── ...
├── domain
│   ├── Item
│   ├── Partner
│   ├── PurchaseOrder
│   └── ...
├── dto
│   ├── ItemCreateRequest
│   ├── ItemResponse
│   └── ...
├── config
└── exception
```

장점: ____
단점: ____

### 1.2 도메인 우선 (Feature-first)

```text
com.example.scm
├── item
│   ├── ItemController
│   ├── ItemService
│   ├── ItemRepository
│   ├── Item
│   └── dto/
├── partner/
├── purchase/
├── sales/
├── notice/
└── common/
    ├── config/
    └── exception/
```

장점: ____
단점: ____

## 2. 의존 방향 그래프

```text
Controller ──→ Service ──→ Repository ──→ Database
                 │
                 └──→ Domain (Entity, 도메인 메서드)
                          ↑
                          │
                        DTO (변환 도구로서만)
```

채워 보세요:
- Domain 이 Service 를 의존하면 안 되는 이유: ____
- DTO 가 Entity 를 의존하는 것은 OK, Entity 가 DTO 를 의존하면 안 되는 이유: ____
- Repository 가 DTO 를 알 필요가 있는가? (projection 의 예외 케이스): ____

## 3. 5가지 안티 패턴 점검

각 항목을 자기 프로젝트에서 확인해 보세요.

| 안티 패턴 | 설명 | 점검 |
|---|---|---|
| Anemic Domain | Entity 가 setter 만 있고 로직이 없음. Service 만 두꺼워짐 | ____ |
| Controller-fat | Controller 가 Repository 직접 호출하고 비즈니스 로직 수행 | ____ |
| God Service | 한 Service 안에 모든 도메인 로직 | ____ |
| Entity 응답 | Controller 가 Entity 를 그대로 응답 | ____ |
| Circular Dependency | A Service → B Service → A Service 의 순환 의존 | ____ |

## 4. 이 프로젝트의 선택과 이유

- 선택한 전략: ____
- 이유 1: ____
- 이유 2: ____
- 향후 확장 시 다른 전략으로 옮길 가능성: ____

## 5. 의존 정리 체크리스트

- [ ] Controller 가 Repository 를 직접 import 하지 않는다.
- [ ] Service 가 Entity 의 setter 를 직접 호출하지 않는다.
- [ ] Entity 가 Service / Repository / DTO 를 import 하지 않는다.
- [ ] config 패키지가 service 나 domain 을 의존하지 않는다 (역방향 OK).
- [ ] exception 패키지가 도메인을 의존하지 않는다.

## 자가 점검

- 우리 프로젝트의 가장 두꺼운 Service 는? 책임을 어떻게 쪼갤 수 있는가? ____
- 가장 두꺼운 Controller 는? Service 로 이동해야 할 코드는? ____
- 가장 자주 import 되는 패키지는? 그 결합도가 적정한가? ____
