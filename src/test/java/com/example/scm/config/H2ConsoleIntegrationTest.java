package com.example.scm.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

// H2는 DispatcherServlet 밖에서 실행되므로 실제 서버로 필터 적용 여부까지 확인한다.
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        "spring.h2.console.enabled=true",
        "spring.h2.console.path=/test-h2-console",
        "spring.datasource.url=jdbc:h2:mem:console-test;MODE=MySQL;DB_CLOSE_DELAY=-1"
})
@DisplayName("H2 콘솔 실제 HTTP 접근 제어")
class H2ConsoleIntegrationTest {

    @LocalServerPort
    private int port;

    @Test
    @DisplayName("비로그인 사용자는 콘솔에 접근할 수 없다")
    void anonymous_isForbidden() throws Exception {
        assertThat(consoleResponse(newClient()).statusCode()).isEqualTo(403);
    }

    @ParameterizedTest
    @ValueSource(strings = {"user@scm.com", "manager@scm.com"})
    @DisplayName("USER·MANAGER 세션은 콘솔에 접근할 수 없다")
    void nonAdmin_isForbidden(String email) throws Exception {
        HttpClient client = newClient();
        login(client, email);
        assertThat(consoleResponse(client).statusCode()).isEqualTo(403);
    }

    @Test
    @DisplayName("ADMIN 세션으로 활성화된 콘솔의 로그인 화면을 열 수 있다")
    void admin_canOpenConsole() throws Exception {
        HttpClient client = newClient();
        login(client, "admin@scm.com");

        HttpResponse<String> response = consoleResponse(client);
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body()).contains("H2 Console");
    }

    private HttpClient newClient() {
        return HttpClient.newBuilder()
                .cookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ALL))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(5))
                .build();
    }

    private void login(HttpClient client, String email) throws Exception {
        HttpRequest request = HttpRequest.newBuilder(uri("/api/auth/login"))
                .timeout(Duration.ofSeconds(10))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(
                        "{\"email\":\"" + email + "\",\"password\":\"password1!\"}"))
                .build();
        assertThat(client.send(request, HttpResponse.BodyHandlers.ofString()).statusCode())
                .isEqualTo(200);
    }

    private HttpResponse<String> consoleResponse(HttpClient client) throws Exception {
        return client.send(HttpRequest.newBuilder(uri("/test-h2-console/"))
                        .timeout(Duration.ofSeconds(10)).GET().build(),
                HttpResponse.BodyHandlers.ofString());
    }

    private URI uri(String path) {
        return URI.create("http://127.0.0.1:" + port + path);
    }
}
