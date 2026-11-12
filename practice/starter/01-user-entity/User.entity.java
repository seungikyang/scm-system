// User 엔티티 (TRD 3.3.1, 3.6.1) · 4지 선다
// 브라우저에서 풀기: ../../sw-quiz/index.html?bank=workbook&module=01
// 각 문제에서 1~4번 중 하나를 고릅니다. 정답·해설 보기: practice/answers.md

// 선택형 [W005] JPA 엔티티와 기본키 선언의 역할 조합은?
//    1. Entity는 DTO, Table은 URL, Id는 세션이다
//    2. Id는 모든 필드를 암호화한다
//    3. Entity는 영속 객체, Table은 테이블, Id는 키, GeneratedValue는 키 생성 전략이다
//    4. GeneratedValue는 모든 문자열을 자동 채운다
//    정답·해설: practice/workbook-questions.md#w005
//    정답: 3번 · 매핑 어노테이션은 객체와 테이블·기본키의 관계를 정의합니다.

// 선택형 [W006] 로그인 계정 저장 정책으로 올바른 것은?
//    1. 이메일은 매번 중복 등록하고 비밀번호는 평문 저장한다
//    2. 이메일은 중복을 막고 비밀번호는 BCrypt 해시로 저장한다
//    3. 비밀번호 해시를 로그인 응답에 공개한다
//    4. 이메일 unique가 비밀번호를 암호화한다
//    정답·해설: practice/workbook-questions.md#w006
//    정답: 2번 · 식별자 중복 방지와 비밀번호 보호는 서로 다른 보안·무결성 책임입니다.

// 선택형 [W007] UserRole을 EnumType.STRING으로 저장하는 이유는?
//    1. enum 선언 순서가 바뀌어도 저장된 이름의 의미를 유지하기 위해서다
//    2. 권한 검사가 자동으로 생기기 때문이다
//    3. DB 용량이 항상 ORDINAL보다 작기 때문이다
//    4. 알 수 없는 enum 값도 모두 허용하기 때문이다
//    정답·해설: practice/workbook-questions.md#w007
//    정답: 1번 · STRING은 이름을 저장합니다. enum 이름 변경에는 별도 마이그레이션이 필요합니다.

// 선택형 [W008] createdAt과 updatedAt을 자동 기록하려면?
//    1. 필드 이름만 있으면 DB가 항상 자동 기록한다
//    2. 모든 조회에서 createdAt을 현재 시각으로 바꾼다
//    3. 컨트롤러가 비밀번호로 날짜를 계산한다
//    4. 감사 어노테이션·리스너와 JPA Auditing 설정을 연결한다
//    정답·해설: practice/workbook-questions.md#w008
//    정답: 4번 · 참조 구현은 BaseTimeEntity와 JpaAuditingConfig로 생성·수정 시각을 관리합니다.

// 선택형 [W234] User와 Partner를 분리하는 이유는?
//    1. 모든 거래처가 반드시 로그인해야 하기 때문이다
//    2. 로그인·권한 계정과 공급·고객 거래처의 책임·생명주기가 다르기 때문이다
//    3. User가 있으면 Partner는 저장할 수 없기 때문이다
//    4. 사용자 비밀번호를 거래처 번호로 쓰기 위해서다
//    정답·해설: practice/workbook-questions.md#w234
//    정답: 2번 · 거래처 마스터는 로그인 계정과 다른 업무 개념입니다.

// 선택형 [W253] USER·ADMIN·MANAGER를 정의하는 프로젝트 타입은?
//    1. UserRole enum
//    2. PartnerType enum
//    3. ItemStatus enum
//    4. PurchaseOrderStatus enum
//    정답·해설: practice/workbook-questions.md#w253
//    정답: 1번 · 사용자 역할과 거래처 유형·품목 상태·주문 상태를 별도 enum으로 구분합니다.

// 선택형 [W373] User 클래스를 JPA 엔티티로 선언하는 애너테이션은?
//    1. @Service
//    2. @Entity
//    3. @RestController
//    4. @Configuration
//    정답·해설: practice/workbook-questions.md#w373
//    정답: 2번 · @Entity는 JPA 관리 대상 타입을 선언합니다. 서비스·컨트롤러·설정 클래스 표시는 목적이 다릅니다.
