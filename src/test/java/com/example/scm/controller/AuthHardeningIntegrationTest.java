package com.example.scm.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.scm.common.auth.LoginUser;
import com.example.scm.common.auth.SessionConst;
import com.example.scm.domain.User;
import com.example.scm.repository.UserRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

/**
 * 판매 준비 점검(docs/diagnostics/sale-audit-20261026-074523)의 Q08 항목을 보완한 동작을 확인합니다.
 * 로그인 반복 제한, 비밀번호 변경 후 다른 세션 종료, 주문 줄 수 상한을 실제 HTTP 흐름으로 검사합니다.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthHardeningIntegrationTest {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserRepository users;

    @Test
    @DisplayName("같은 이메일로 5번 틀리면 6번째 로그인은 429로 거절한다")
    void repeatedLoginFailures_areThrottled() throws Exception {
        // 테스트끼리 Spring 컨텍스트(제한기 기록)를 공유하므로, 다른 테스트가 쓰지 않는 이메일을 사용합니다.
        String email = "throttle-check@scm.com";
        for (int i = 0; i < 5; i++) {
            mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                            .content(json(Map.of("email", email, "password", "wrong-password"))))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("INVALID_INPUT"));
        }
        mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content(json(Map.of("email", email, "password", "wrong-password"))))
                .andExpect(status().isTooManyRequests())
                .andExpect(jsonPath("$.code").value("TOO_MANY_LOGIN_ATTEMPTS"));
    }

    @Test
    @DisplayName("비밀번호를 바꾸면 같은 사용자의 다른 세션은 끝나고 현재 세션은 유지된다")
    void passwordChange_invalidatesOtherSessions() throws Exception {
        MockHttpSession current = login("manager@scm.com", "password1!");
        MockHttpSession other = login("manager@scm.com", "password1!");

        mvc.perform(patch("/api/users/me/password").session(current)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(Map.of("currentPassword", "password1!", "newPassword", "newPassword1!"))))
                .andExpect(status().isNoContent());

        assertThat(other.isInvalid()).isTrue();
        assertThat(current.isInvalid()).isFalse();
        mvc.perform(get("/api/users/me").session(current)).andExpect(status().isOk());
    }

    @Test
    @DisplayName("주문 줄이 100건을 넘으면 저장 전에 400으로 거절한다")
    void orderLines_areCappedAt100() throws Exception {
        User user = users.findByEmail("user@scm.com").orElseThrow();
        LoginUser loginUser = new LoginUser(user.getId(), user.getName(), user.getEmail(), user.getRole());
        List<Map<String, Object>> lines = new ArrayList<>();
        for (int i = 0; i < 101; i++) {
            lines.add(Map.of("itemId", 1, "quantity", 1));
        }
        String body = json(Map.of("partnerId", 1, "orderDate", LocalDate.now().toString(), "lines", lines));

        for (String path : List.of("/api/purchase-orders", "/api/sales-orders")) {
            mvc.perform(post(path).sessionAttr(SessionConst.LOGIN_USER, loginUser)
                            .contentType(MediaType.APPLICATION_JSON).content(body))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("INVALID_INPUT"));
        }
    }

    private MockHttpSession login(String email, String password) throws Exception {
        return (MockHttpSession) mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content(json(Map.of("email", email, "password", password))))
                .andExpect(status().isOk())
                .andReturn().getRequest().getSession(false);
    }

    private String json(Object value) throws Exception {
        return objectMapper.writeValueAsString(value);
    }
}
