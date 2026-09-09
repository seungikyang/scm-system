package com.example.scm.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;

@Tag("mysql")
@ActiveProfiles("mysql")
@SpringBootTest(properties = "scm.seed.enabled=false")
class MySqlMigrationContractTest {

    @Test
    void flywayMigratesSchemaAndJpaValidatesMappings() throws IOException {
        String migration = new ClassPathResource(
                "db/migration/mysql/V1__baseline_schema.sql")
                .getContentAsString(StandardCharsets.UTF_8);

        assertThat(migration)
                .contains("CREATE TABLE users")
                .contains("CREATE TABLE purchase_orders")
                .contains("CREATE TABLE stocks")
                .contains("CONSTRAINT uk_po_order_number UNIQUE")
                .contains("CONSTRAINT uk_stock_item UNIQUE")
                .contains("CONSTRAINT fk_pol_po FOREIGN KEY");
    }
}
