// 실제 구현 위치 예: src/test/java/com/example/scm/PurchaseFlowTest.java
// 목표: 발주 생명주기를 MockMvc 통합 테스트로 검증하세요. TRD 3.12 참고.

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PurchaseFlowTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @Test
    @DisplayName("USER 발주 작성 → submit → ADMIN 승인 → receive 전체 흐름")
    void purchaseOrder_full_flow() throws Exception {

        // 0. 테스트 데이터 준비 (User USER/ADMIN, Partner SUPPLIER, Category, Item) 는 @BeforeEach 또는 fixture 로 미리 준비

        // 1. USER 로그인
        MockHttpSession userSession = new MockHttpSession();
        // TODO 01: 세션에 USER_ID, USER_ROLE 을 어떻게 세팅할지 채우세요.
        userSession.setAttribute("USER_ID", ____L);
        userSession.setAttribute("USER_ROLE", "USER");

        // 2. 발주 작성
        String createBody = """
            {
              "partnerId": 1,
              "orderDate": "2026-06-01",
              "dueDate": "2026-06-15",
              "lines": [
                { "itemId": 10, "quantity": 100, "unitPrice": 5000 }
              ]
            }
            """;

        String createResponse = mockMvc.perform(
                post("/api/purchase-orders")
                    .session(userSession)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(createBody))
            // TODO 02: 정상 응답 status 는?
            .andExpect(status().is____())
            .andReturn()
            .getResponse()
            .getContentAsString();

        // TODO 03: 응답 JSON 에서 purchaseOrderId 를 어떻게 꺼낼까요?
        Long poId = JsonPath.read(createResponse, "$.____");

        // 3. submit (DRAFT → REQUESTED)
        mockMvc.perform(patch("/api/purchase-orders/{poId}/submit", poId)
                .session(userSession))
            .andExpect(status().isOk());

        // 4. ADMIN 로그인 후 승인
        MockHttpSession adminSession = new MockHttpSession();
        adminSession.setAttribute("USER_ID", 100L);
        adminSession.setAttribute("USER_ROLE", "ADMIN");

        mockMvc.perform(patch("/api/admin/purchase-orders/{poId}/approve", poId)
                .session(adminSession))
            .andExpect(status().isOk())
            // TODO 04: 응답 status 가 APPROVED 인지 확인하는 jsonPath 를 채우세요.
            .andExpect(jsonPath("$.status").value("____"));

        // 5. receive (APPROVED → RECEIVED)
        mockMvc.perform(patch("/api/admin/purchase-orders/{poId}/receive", poId)
                .session(adminSession))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("USER가 ADMIN/MANAGER 운영 API 호출 시 403")
    void user_calling_admin_api_should_be_forbidden() throws Exception {
        MockHttpSession userSession = new MockHttpSession();
        userSession.setAttribute("USER_ID", 1L);
        userSession.setAttribute("USER_ROLE", "USER");

        mockMvc.perform(patch("/api/admin/purchase-orders/{poId}/approve", 999L)
                .session(userSession))
            // TODO 05: 권한 부족 시 status 는?
            .andExpect(status().is____());
    }

    @Test
    @DisplayName("REQUESTED 가 아닌 발주를 승인하면 INVALID_STATUS")
    void approving_non_requested_should_fail() throws Exception {
        // TODO 06: DRAFT 상태 발주를 직접 만들고 승인 호출 → status().isBadRequest() 와 code 검증.
    }

    @Test
    @DisplayName("MANAGER도 REQUESTED 발주를 승인할 수 있다")
    void manager_can_approve_requested_order() throws Exception {
        // TODO 07: MANAGER 세션으로 승인 성공을 검증하세요. 역할 문자열만 검사하지 말고 실제 사용자 역할도 준비합니다.
    }

    @Test
    @DisplayName("작성자는 APPROVED 발주는 취소할 수 있지만 RECEIVED 발주는 취소할 수 없다")
    void owner_cancel_boundary() throws Exception {
        // TODO 08: APPROVED → CANCELED 성공과 RECEIVED → INVALID_STATUS 실패를 각각 검증하세요.
    }

    @Test
    @DisplayName("입고 성공 시 모든 라인의 재고가 같은 트랜잭션에서 증가한다")
    void receive_increases_stock_atomically() throws Exception {
        // TODO 09: 두 라인의 재고 증가를 확인하고, 한 라인 실패 시 상태/재고가 모두 롤백되는 사례를 설계하세요.
    }
}

// 학습 질문:
// Q1. @Transactional 테스트는 어떤 동작을 보장하는가?
//     A:
// Q2. MockMvc 와 WebTestClient 의 차이는?
//     A:
// Q3. JsonPath 와 Jackson ObjectMapper 의 트레이드오프는?
//     A:
