# 패키지 구조와 계층 책임 (TRD 3.2.2) · 4지 선다

[이 모듈을 브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&module=36) · [전체 문제집](../../workbook-questions.md)

각 문제에서 1~4번 중 하나를 고르고 정답·해설을 확인합니다.

<a id="w140"></a>
### W140. Entity와 DTO 의존 방향의 올바른 원칙은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=140)

1. Entity가 Controller와 Session을 직접 호출한다
2. Entity가 HTTP 응답 DTO에 의존하지 않게 한다
3. DTO 사용 시 Entity도 반드시 DTO를 import한다
4. Repository가 HTML 템플릿을 생성한다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · Entity가 HTTP 응답 DTO에 의존하지 않게 한다

도메인 모델은 외부 표현 계약의 변경으로부터 독립시키는 것이 좋습니다.

</details>


<a id="w157"></a>
### W157. 계층 우선과 도메인 우선 패키지의 올바른 비교는?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=157)

1. 둘 다 권한 검사를 자동 생성한다
2. 도메인 우선이면 클래스가 필요 없다
3. 전자는 기술 계층별, 후자는 기능별 응집을 우선한다
4. 계층 우선이면 순환 의존이 불가능하다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 전자는 기술 계층별, 후자는 기능별 응집을 우선한다

규모와 변경 단위에 맞춰 선택하며 패키지 전략만으로 좋은 설계가 보장되지는 않습니다.

</details>


<a id="w158"></a>
### W158. 기본 요청 처리 의존 방향은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=158)

1. Domain → Controller → Session
2. Repository → HTML → Controller
3. CSS → PasswordEncoder → SQL
4. Controller → Service → Repository, Service → Domain

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · Controller → Service → Repository, Service → Domain

업무 도메인을 HTTP 표현과 세션으로부터 독립시키는 방향입니다.

</details>


<a id="w159"></a>
### W159. Entity가 피해야 할 의존의 예는?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=159)

1. Controller·HttpSession·응답 DTO
2. 업무 상태 enum
3. BigDecimal
4. LocalDate

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · Controller·HttpSession·응답 DTO

도메인 타입 사용과 외부 표현·인프라 책임의 결합을 구분합니다.

</details>


<a id="w160"></a>
### W160. Controller-fat 안티 패턴의 예는?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=160)

1. Controller가 요청 DTO를 검증한다
2. Controller가 Repository를 직접 호출하며 업무 상태를 변경한다
3. Controller가 Service를 호출한다
4. Controller가 View 이름을 반환한다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · Controller가 Repository를 직접 호출하며 업무 상태를 변경한다

HTTP 표현 책임과 업무 처리 책임을 분리합니다.

</details>


<a id="w177"></a>
### W177. 응답 DTO에서 제외해야 할 정보는?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=177)

1. 비밀번호 해시·내부 토큰 등 민감정보
2. 공개 품목 이름
3. 공개 카테고리 이름
4. 공지 제목

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 비밀번호 해시·내부 토큰 등 민감정보

평문뿐 아니라 비밀번호 해시와 내부 인증 자료도 응답 계약에서 제외합니다.

</details>


<a id="w213"></a>
### W213. Repository가 DTO 타입을 참조할 수 있는 정당한 예는?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=213)

1. HttpSession을 직접 관리하는 경우
2. HTML 폼을 렌더링하는 경우
3. 비밀번호를 응답하는 경우
4. 조회 전용 projection 계약을 사용하는 경우

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 조회 전용 projection 계약을 사용하는 경우

모든 DTO 의존을 기계적으로 금지하지 않고 조회 모델의 목적과 결합을 검토합니다.

</details>


<a id="w214"></a>
### W214. Anemic Domain의 전형적인 특징은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=214)

1. Entity가 상태 전이 가드를 캡슐화한다
2. Entity는 데이터·setter만 갖고 업무 규칙이 Service에 집중된다
3. Service가 Controller HTTP 응답을 몰라도 된다
4. 도메인 메서드를 단위 테스트한다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · Entity는 데이터·setter만 갖고 업무 규칙이 Service에 집중된다

업무 규칙의 표현과 변경 지점을 검토해 의미 있는 도메인 동작으로 옮길 수 있습니다.

</details>


<a id="w215"></a>
### W215. 너무 큰 Service를 리팩터링하는 적절한 방향은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=215)

1. 모든 Service를 하나의 더 큰 클래스에 합친다
2. Controller로 업무 규칙을 모두 옮긴다
3. 응집된 업무·정책 단위로 책임을 분리한다
4. 테스트를 삭제해 파일을 줄인다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 응집된 업무·정책 단위로 책임을 분리한다

도메인 서비스·정책 객체 등으로 응집도와 의존 방향을 개선하고 동작을 검증합니다.

</details>


<a id="w216"></a>
### W216. A Service→B Service→A Service 순환을 개선하는 방향은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=216)

1. 공통 업무 책임과 호출 방향을 재설계한다
2. 필드 이름만 바꾸면 순환이 사라진다
3. 항상 Lazy만 붙이고 설계 검토를 끝낸다
4. 두 Service가 서로의 Controller를 호출하게 한다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 공통 업무 책임과 호출 방향을 재설계한다

순환은 책임·경계 문제의 신호일 수 있어 객체 관계와 흐름을 정리해야 합니다.

</details>


<a id="w217"></a>
### W217. 현재 저장소의 주요 패키지 전략은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=217)

1. 모든 클래스가 단일 파일에 있다
2. controller·service·repository·domain 중심의 계층 우선
3. 각 클래스마다 독립 저장소를 쓴다
4. 모든 기능을 CSS 폴더에 둔다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · controller·service·repository·domain 중심의 계층 우선

기본 계층을 쉽게 찾을 수 있지만 한 기능 변경이 여러 패키지에 걸칠 수 있습니다.

</details>


<a id="w218"></a>
### W218. 기능별 패키지로 옮기는 기준은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=218)

1. 변경 단위·응집도·의존 관계를 검토하고 점진적으로 이동한다
2. 패키지만 옮기면 모든 순환이 자동 해결된다
3. 테스트 없는 대규모 이름 변경만 수행한다
4. Feature-first는 모든 규모에 항상 유일한 정답이다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 변경 단위·응집도·의존 관계를 검토하고 점진적으로 이동한다

패키지 구조는 도구이며 실제 책임과 검증된 동작을 유지해야 합니다.

</details>


<a id="w219"></a>
### W219. 자주 import되는 공통 패키지의 결합도를 평가할 때는?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=219)

1. import 개수만 적으면 무조건 좋은 설계다
2. 많이 사용되면 모두 복사해서 별도 정의한다
3. 공통 패키지에 모든 업무 로직을 넣는다
4. 사용 이유·변경 파급·순환과 불필요한 책임을 확인한다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 사용 이유·변경 파급·순환과 불필요한 책임을 확인한다

공통 타입의 필요성과 과도한 결합을 실제 변경·의존 방향으로 평가합니다.

</details>


<a id="w270"></a>
### W270. Controller가 업무 처리를 위임해야 하는 계층은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=270)

1. HTML 템플릿
2. 브라우저 localStorage
3. Gradle 캐시
4. Service

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · Service

Controller는 HTTP 계약을 처리하고 Service가 업무 흐름·트랜잭션·인가를 조정합니다.

</details>
