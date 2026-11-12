package com.example.scm.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.scm.common.auth.LoginUser;
import com.example.scm.common.auth.SessionConst;
import com.example.scm.domain.enums.UserRole;
import java.util.stream.Stream;
import java.util.regex.Pattern;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class PaginationIntegrationTest {

    private static final LoginUser ADMIN = new LoginUser(1L, "관리자", "admin@scm.com", UserRole.ADMIN);

    @Autowired private MockMvc mvc;

    @ParameterizedTest
    @MethodSource("listPaths")
    void unsupportedOffsetReturnsInputError(String path) throws Exception {
        var result = mvc.perform(get(path).param("page", "107374183").param("size", "20")
                        .sessionAttr(SessionConst.LOGIN_USER, ADMIN))
                .andExpect(status().isBadRequest());
        if (path.startsWith("/api/")) {
            result.andExpect(jsonPath("$.code").value("INVALID_INPUT"));
        } else {
            result.andExpect(model().attribute("code", "INVALID_INPUT"));
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"/api/items", "/api/stocks", "/api/notices"})
    void largestSupportedOffsetStillReturnsEmptyPage(String path) throws Exception {
        mvc.perform(get(path).param("page", "2147483647").param("size", "1")
                        .sessionAttr(SessionConst.LOGIN_USER, ADMIN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"/items", "/partners", "/stocks"})
    void pastEndPageDoesNotRenderEveryMissingPage(String path) throws Exception {
        mvc.perform(get(path).param("page", "200").param("size", "1")
                        .sessionAttr(SessionConst.LOGIN_USER, ADMIN))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    var nav = Pattern.compile("<nav class=\"pagination\">(.*?)</nav>", Pattern.DOTALL)
                            .matcher(result.getResponse().getContentAsString());
                    assertThat(nav.find()).isTrue();
                    assertThat(Pattern.compile("<a ").matcher(nav.group()).results().count())
                            .isLessThanOrEqualTo(6);
                });
    }

    private static Stream<String> listPaths() {
        return Stream.of("/items", "/partners", "/stocks", "/purchase-orders/my",
                        "/admin/purchase-orders", "/sales-orders/my", "/sales-orders/pending",
                        "/admin/sales-orders", "/notices")
                .flatMap(path -> Stream.of(path, "/api" + path));
    }
}
