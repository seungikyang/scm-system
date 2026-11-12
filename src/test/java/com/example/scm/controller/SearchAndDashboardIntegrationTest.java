package com.example.scm.controller;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.scm.common.auth.LoginUser;
import com.example.scm.common.auth.SessionConst;
import com.example.scm.domain.enums.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

/**
 * 검색어 처리와 대시보드 기간 오류 처리를 실제 화면·API 경로로 확인한다.
 * 품목·거래처·재고·공지 검색이 모두 대소문자를 무시하고, 검색어 속 %·_를
 * 와일드카드가 아닌 글자 그대로 찾는지 본다. 기대 건수는 시드 데이터(품목 8개) 기준이다.
 */
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@DisplayName("검색어·대시보드 기간 처리 통합 테스트")
class SearchAndDashboardIntegrationTest {

    private static final LoginUser ADMIN =
            new LoginUser(1L, "관리자", "admin@scm.com", UserRole.ADMIN);
    private static final LoginUser USER =
            new LoginUser(3L, "홍길동", "user@scm.com", UserRole.USER);

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("품목 검색은 대소문자를 구분하지 않는다")
    void itemSearchIgnoresCase() throws Exception {
        mockMvc.perform(get("/api/items").param("itemCode", "itm-00")
                        .sessionAttr(SessionConst.LOGIN_USER, USER))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(3));
    }

    @Test
    @DisplayName("검색어의 %와 _는 와일드카드가 아니라 글자 그대로 찾는다")
    void wildcardCharactersAreLiteral() throws Exception {
        mockMvc.perform(get("/api/items").param("itemCode", "_")
                        .sessionAttr(SessionConst.LOGIN_USER, USER))
                .andExpect(jsonPath("$.totalElements").value(0));
        mockMvc.perform(get("/api/items").param("name", "%")
                        .sessionAttr(SessionConst.LOGIN_USER, USER))
                .andExpect(jsonPath("$.totalElements").value(0));
        mockMvc.perform(get("/api/partners").param("businessNumber", "_")
                        .sessionAttr(SessionConst.LOGIN_USER, ADMIN))
                .andExpect(jsonPath("$.totalElements").value(0));
        mockMvc.perform(get("/api/stocks").param("keyword", "%")
                        .sessionAttr(SessionConst.LOGIN_USER, USER))
                .andExpect(jsonPath("$.totalElements").value(0));
        // 탈출 문자 !도 글자 그대로 취급해야 한다.
        mockMvc.perform(get("/api/stocks").param("keyword", "!")
                        .sessionAttr(SessionConst.LOGIN_USER, USER))
                .andExpect(jsonPath("$.totalElements").value(0));
    }

    @Test
    @DisplayName("공지 제목 검색은 대소문자를 무시하고 %가 들어간 제목도 정확히 찾는다")
    void noticeSearchMatchesLiteralPercent() throws Exception {
        mockMvc.perform(post("/api/notices").with(csrf())
                        .sessionAttr(SessionConst.LOGIN_USER, ADMIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Sale 50% Notice\",\"content\":\"내용\"}"))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/api/notices").with(csrf())
                        .sessionAttr(SessionConst.LOGIN_USER, ADMIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Sale 500 Notice\",\"content\":\"내용\"}"))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/notices").param("keyword", "50%")
                        .sessionAttr(SessionConst.LOGIN_USER, USER))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].title").value("Sale 50% Notice"));
        mockMvc.perform(get("/api/notices").param("keyword", "SALE")
                        .sessionAttr(SessionConst.LOGIN_USER, USER))
                .andExpect(jsonPath("$.totalElements").value(2));
    }

    @Test
    @DisplayName("통계 기간이 뒤집혀도 대시보드는 열리고 안내 문구와 기본 기간을 보여 준다")
    void dashboardFallsBackOnInvalidStatisticsRange() throws Exception {
        mockMvc.perform(get("/").param("from", "2026-12-01").param("to", "2026-01-01")
                        .sessionAttr(SessionConst.LOGIN_USER, ADMIN))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("기본 기간으로 표시합니다")))
                .andExpect(content().string(containsString("발주·수주 통계")));
    }
}
