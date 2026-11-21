package com.example.scm.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.example.scm.common.auth.LoginUser;
import com.example.scm.common.auth.SessionConst;
import com.example.scm.domain.enums.UserRole;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class SortValidationIntegrationTest {

    private static final LoginUser ADMIN = new LoginUser(1L, "관리자", "admin@scm.com", UserRole.ADMIN);
    private static final String[] WEB_PATHS = {"/items", "/partners", "/stocks", "/purchase-orders/my",
            "/admin/purchase-orders", "/sales-orders/my", "/sales-orders/pending", "/admin/sales-orders"};

    @Autowired private MockMvc mvc;

    @ParameterizedTest
    @MethodSource("invalidSortRequests")
    void invalidSortReturnsBadRequestBeforeQuery(String path, String property) throws Exception {
        var result = mvc.perform(get(path).param("sort", "createdAt,desc", property + ",asc")
                        .sessionAttr(SessionConst.LOGIN_USER, ADMIN))
                .andExpect(status().isBadRequest());
        if (path.startsWith("/api/")) {
            result.andExpect(jsonPath("$.code").value("INVALID_INPUT"));
        } else {
            result.andExpect(view().name("error"))
                    .andExpect(model().attribute("code", "INVALID_INPUT"));
        }
    }

    @ParameterizedTest
    @MethodSource("listPaths")
    void validMultipleSortsStillWork(String path) throws Exception {
        mvc.perform(get(path).param("sort", "createdAt,desc", "id,asc")
                        .sessionAttr(SessionConst.LOGIN_USER, ADMIN))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @ValueSource(strings = {"/notices", "/api/notices"})
    void noticeListKeepsItsFixedSort(String path) throws Exception {
        mvc.perform(get(path).param("sort", "missing,asc")
                        .sessionAttr(SessionConst.LOGIN_USER, ADMIN))
                .andExpect(status().isOk());
    }

    private static Stream<String> listPaths() {
        return Stream.of(WEB_PATHS).flatMap(path -> Stream.of(path, "/api" + path));
    }

    private static Stream<Arguments> invalidSortRequests() {
        // 존재하지 않는 속성과 중첩 경로를 모두 거부한다. 두 번째 정렬도 빠짐없이 검사한다.
        return listPaths().flatMap(path -> Stream.of("missing", "name.missing")
                .map(property -> Arguments.of(path, property)));
    }
}
