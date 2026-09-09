package com.example.scm.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

// 별도 MySQL 서버 없이도 Boot의 자동 마이그레이션과 JPA 검증 순서가 연결되는지 확인한다.
// MySQL 고유 동작은 mysqlSchemaTest가 실제 MySQL에서 별도로 검증한다.
@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:flyway-startup;MODE=MySQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1",
        "spring.flyway.enabled=true",
        "spring.flyway.locations=classpath:db/migration/mysql",
        "spring.jpa.hibernate.ddl-auto=validate",
        "scm.seed.enabled=false"
})
@DisplayName("Flyway 자동 실행 및 JPA 스키마 검증")
class FlywayStartupIntegrationTest {

    @Autowired
    private Flyway flyway;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("시작 시 V1을 자동 적용한 뒤 JPA가 매핑을 검증한다")
    void startup_migratesBeforeJpaValidation() {
        assertThat(flyway.info().current().getVersion().getVersion()).isEqualTo("1");
        assertThat(flyway.info().pending()).isEmpty();
        assertThat(jdbcTemplate.queryForObject("select count(*) from users", Long.class)).isZero();
        assertThat(jdbcTemplate.queryForObject("select count(*) from purchase_orders", Long.class)).isZero();
        assertThat(jdbcTemplate.queryForObject("select count(*) from stocks", Long.class)).isZero();
    }
}
