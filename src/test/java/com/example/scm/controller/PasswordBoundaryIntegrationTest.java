package com.example.scm.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.scm.common.auth.LoginUser;
import com.example.scm.common.auth.SessionConst;
import com.example.scm.domain.User;
import com.example.scm.repository.UserRepository;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PasswordBoundaryIntegrationTest {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserRepository users;

    private User user;
    private LoginUser loginUser;

    @BeforeEach
    void setUp() {
        user = users.findByEmail("user@scm.com").orElseThrow();
        loginUser = new LoginUser(user.getId(), user.getName(), user.getEmail(), user.getRole());
    }

    @ParameterizedTest
    @MethodSource("overlongPasswords")
    void passwordChange_rejectsMoreThan72Utf8BytesWithoutChangingPassword(String password) throws Exception {
        String originalHash = user.getPassword();

        mvc.perform(patch("/api/users/me/password")
                        .sessionAttr(SessionConst.LOGIN_USER, loginUser)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "currentPassword", "password1!", "newPassword", password))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_INPUT"));

        assertThat(users.findById(user.getId()).orElseThrow().getPassword()).isEqualTo(originalHash);
    }

    @ParameterizedTest
    @MethodSource("validPasswords")
    void passwordChange_acceptsBoundaryPasswordsAndCanLogInWithThem(String password) throws Exception {
        mvc.perform(patch("/api/users/me/password")
                        .sessionAttr(SessionConst.LOGIN_USER, loginUser)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "currentPassword", "password1!", "newPassword", password))))
                .andExpect(status().isNoContent());

        mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "email", user.getEmail(), "password", password))))
                .andExpect(status().isOk());
    }

    @Test
    void login_rejectsOverlongPasswordAsInvalidInput() throws Exception {
        mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "email", user.getEmail(), "password", "가".repeat(25)))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_INPUT"));
    }

    @Test
    void passwordChange_rejectsOverlongCurrentPasswordAsInvalidInput() throws Exception {
        mvc.perform(patch("/api/users/me/password")
                        .sessionAttr(SessionConst.LOGIN_USER, loginUser)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "currentPassword", "가".repeat(25), "newPassword", "new-password1!"))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_INPUT"));
    }

    private static Stream<String> overlongPasswords() {
        return Stream.of("가".repeat(25), "😀".repeat(19), "가".repeat(24) + "a");
    }

    private static Stream<String> validPasswords() {
        return Stream.of("가".repeat(24), "😀".repeat(18), "a".repeat(64));
    }
}
