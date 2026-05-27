# Thymeleaf 화면 워크북

PRD 2.7 의 화면 요구사항을 Thymeleaf 템플릿으로 옮길 때 외워야 할 기본기를 채워봅니다. 화면은 백엔드 포트폴리오에서도 데모 페이지로 자주 쓰입니다.

---

## 1. 디렉터리 구조 채우기

```text
src/main/resources/
├── ____                # Thymeleaf 템플릿 (.html)
│   ├── layout/
│   │   └── base.html
│   ├── fragments/
│   │   ├── header.html
│   │   └── nav.html
│   ├── auth/
│   │   └── login.html
│   ├── partners/
│   │   ├── list.html
│   │   ├── detail.html
│   │   └── form.html
│   ├── categories/
│   │   ├── list.html
│   │   └── form.html
│   ├── items/
│   │   ├── list.html
│   │   ├── detail.html
│   │   └── form.html
│   ├── purchase-orders/
│   │   ├── form.html        ← 라인 동적 추가
│   │   ├── my-list.html
│   │   ├── detail.html
│   │   └── admin-list.html
│   ├── notices/
│   │   ├── list.html
│   │   └── detail.html
│   └── sales-orders/
│       ├── form.html
│       ├── my-list.html
│       └── pending.html
└── ____                # 정적 리소스 (.css, .js, .png)
    ├── css/
    └── js/
```

---

## 2. 기본 문법 빈칸 채우기

### A. 네임스페이스 선언

```html
<!DOCTYPE html>
<html xmlns:th="____">
<head>...</head>
```

### B. 텍스트 출력

```html
<!-- TODO 01: model 에 담긴 item.name 을 escape 처리하여 출력 -->
<span th:____="${item.name}">기본값</span>

<!-- 위험: HTML 그대로 (사용자 입력에는 절대 쓰지 않음) -->
<div th:utext="${notice.contentRichText}"></div>
```

### C. 조건 / 반복

```html
<!-- TODO 02: 품목 목록이 비어 있으면 안내 메시지 -->
<p th:____="${#lists.isEmpty(items)}">등록된 품목이 없습니다.</p>

<!-- TODO 03: 반복문 -->
<tr th:____="item : ${items}">
    <td th:text="${item.itemCode}">ITM-001</td>
    <td th:text="${item.name}">USB 케이블</td>
    <td th:text="${item.categoryName}">전기/전자</td>
    <td th:text="${#numbers.formatDecimal(item.unitPrice, 0, 'COMMA', 2, 'POINT')}">5,000.00</td>
</tr>
```

### D. URL / 폼

```html
<!-- TODO 04: 동적 URL — /items/{id} -->
<a th:____="@{/items/{id}(id=${item.itemId})}">상세</a>

<!-- TODO 05: CSRF 토큰을 폼에 자동으로 넣는 방법 -->
<form th:action="@{/api/purchase-orders}" method="post" th:object="${form}">
    <input type="hidden" th:name="${_csrf.parameterName}" th:value="${_csrf.____}"/>

    <label>공급사</label>
    <select th:field="*{partnerId}">
        <option th:each="p : ${partners}"
                th:value="${p.partnerId}"
                th:text="${p.name}">
        </option>
    </select>

    <button type="submit">발주 작성</button>
</form>
```

### E. Fragment 재사용

```html
<!-- fragments/header.html -->
<header th:____="header">
    <h1>SCM 시스템</h1>
    <nav>
        <a th:href="@{/items}">품목</a>
        <a th:href="@{/partners}">거래처</a>
        <a th:href="@{/purchase-orders}">발주</a>
        <a th:href="@{/sales-orders}">수주</a>
        <a th:href="@{/notices}">공지</a>
    </nav>
</header>

<!-- 사용처 -->
<div th:____="~{fragments/header :: header}"></div>
```

`th:replace` 와 `th:insert` 의 차이를 한 줄로 적어 보세요.

> A:

---

## 3. 발주서 작성 화면: 라인 동적 추가 (SCM 특화)

```html
<form th:action="@{/api/purchase-orders}" method="post" th:object="${form}">
    <input type="hidden" th:name="${_csrf.parameterName}" th:value="${_csrf.token}"/>

    <h2>발주서 작성</h2>

    <div>
        <label>공급사</label>
        <select th:field="*{partnerId}">
            <option th:each="p : ${suppliers}"
                    th:value="${p.partnerId}"
                    th:text="${p.name}"></option>
        </select>
    </div>

    <div>
        <label>발주일</label>
        <input type="date" th:field="*{orderDate}"/>
        <label>납기일</label>
        <input type="date" th:field="*{dueDate}"/>
    </div>

    <table id="line-table">
        <thead>
            <tr><th>품목</th><th>수량</th><th>단가</th><th>합계</th><th></th></tr>
        </thead>
        <tbody id="line-rows">
            <!-- TODO 06: 동적 인덱스(__${idx.index}__) 가 필요한 이유는? -->
            <tr th:each="line, idx : *{lines}">
                <td>
                    <select th:field="*{lines[__${idx.index}__].itemId}">
                        <option th:each="it : ${items}"
                                th:value="${it.itemId}"
                                th:text="${it.name}"></option>
                    </select>
                </td>
                <td><input type="number" min="1" th:field="*{lines[__${idx.index}__].quantity}"/></td>
                <td><input type="number" step="0.01" th:field="*{lines[__${idx.index}__].unitPrice}"/></td>
                <td><span class="row-total">0</span></td>
                <td><button type="button" onclick="removeRow(this)">삭제</button></td>
            </tr>
        </tbody>
    </table>

    <button type="button" onclick="addLineRow()">라인 추가</button>
    <button type="submit">발주 요청</button>
</form>
```

채워 보세요:

- JS 없이 서버 라운드트립으로만 라인을 추가하면 어떤 UX 문제? ____
- 동적 인덱스(`__${idx.index}__`) 가 없으면 Spring 바인더가 어떤 에러를? ____

---

## 4. Controller 가 view 를 반환하는 흐름

```java
@Controller
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemViewController {

    private final ItemService itemService;

    @GetMapping
    public String list(Model model,
                       @RequestParam(required = false) String keyword,
                       Pageable pageable) {
        Page<ItemResponse> page = itemService.search(keyword, null, pageable);
        // TODO 07: 템플릿에서 ${items}, ${page} 로 접근하기 위해 어떻게 담을까?
        model.____("items", page.getContent());
        model.addAttribute("page", page);
        return "items/list"; // → templates/items/list.html
    }

    @GetMapping("/{itemId}")
    public String detail(@PathVariable Long itemId, Model model) {
        model.addAttribute("item", itemService.getDetail(itemId));
        return "items/detail";
    }
}
```

`@RestController` 와 `@Controller` 의 차이를 한 줄로 적어 보세요.

> A:

---

## 5. JSON API 와 View 동시 운영

REST API 와 Thymeleaf 페이지를 같은 프로젝트에 두는 패턴:

| 경로 | 역할 | 컨트롤러 |
|---|---|---|
| `/api/**` | JSON 응답 | `@RestController` |
| `/items`, `/partners`, `/purchase-orders`, ... | 화면 응답 | `@Controller` (View 이름 반환) |

이 구조는 PRD 2.7 화면 요구사항과 TRD 3.7 API 명세를 자연스럽게 동시에 만족시킵니다.

- 같은 도메인을 둘 다 만들면 어떤 중복이 생기는가? ____
- API 와 화면을 점진적으로 분리하는 전략은? ____

---

## 6. 자주 빠뜨리는 보안 포인트

- [ ] `th:text` 가 아닌 `th:utext` 를 쓰면 HTML escape 가 사라집니다. 어떤 경우에만 사용 가능한가요?
- [ ] 로그인하지 않은 사용자가 보호 화면(/admin/purchase-orders) 에 접근할 때 어디서 막아야 하나요?
- [ ] 폼 제출에 CSRF 토큰이 빠지면 어떤 상황에서 문제가 되나요?
- [ ] 응답에 비밀번호 같은 민감 정보를 model 에 담아 두지 않았는지 점검했나요?
- [ ] 발주서 상세에서 다른 사용자의 발주를 URL 만 바꿔 접근하는 IDOR 공격을 어디서 막나요?

---

## 7. 학습 질문

- Q1. SPA(React 등) 대신 Thymeleaf 를 선택했을 때의 장점/단점을 한 줄씩 적어 보세요.
- Q2. JSP 와 Thymeleaf 의 가장 큰 차이는?
- Q3. Thymeleaf 의 `th:object`, `th:field` 가 일반 form 과 다른 점은?
- Q4. 페이지가 깨졌을 때 Spring Boot 기본 에러 페이지를 커스텀하려면 어디에 어떤 템플릿을 두면 될까요? (templates/error/*.html)
- Q5. 발주서 라인을 동적으로 추가하는 UX 를 Thymeleaf 만으로 vs JS 보조로 만들 때의 차이는?
- Q6. 권한이 다른 사용자(USER/ADMIN/MANAGER)에게 다른 메뉴를 보여줄 때, Thymeleaf 표현식은? (힌트: `sec:authorize`)
