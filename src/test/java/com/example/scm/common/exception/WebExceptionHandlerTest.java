package com.example.scm.common.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.ui.ExtendedModelMap;

@DisplayName("WebExceptionHandler 테스트")
class WebExceptionHandlerTest {

    private final WebExceptionHandler handler = new WebExceptionHandler();

    @Test
    @DisplayName("비즈니스 예외의 HTTP 상태를 오류 화면 응답에 반영한다")
    void businessException_setsHttpStatus() {
        MockHttpServletResponse response = new MockHttpServletResponse();
        ExtendedModelMap model = new ExtendedModelMap();

        String view = handler.handleBusiness(
                new BusinessException(ErrorCode.ITEM_NOT_FOUND), model, response);

        assertThat(view).isEqualTo("error");
        assertThat(response.getStatus()).isEqualTo(404);
        assertThat(model.get("code")).isEqualTo("ITEM_NOT_FOUND");
    }

    @Test
    @DisplayName("예상하지 못한 예외는 HTTP 500으로 응답한다")
    void unexpectedException_setsHttpStatus() {
        MockHttpServletResponse response = new MockHttpServletResponse();

        String view = handler.handleException(
                new IllegalStateException("failure"), new ExtendedModelMap(), response);

        assertThat(view).isEqualTo("error");
        assertThat(response.getStatus()).isEqualTo(500);
    }
}
