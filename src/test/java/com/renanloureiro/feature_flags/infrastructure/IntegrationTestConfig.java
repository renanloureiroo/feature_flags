package com.renanloureiro.feature_flags.infrastructure;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@TestConfiguration
@Testcontainers
public class IntegrationTestConfig {

  @Container
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
      .withDatabaseName("feature_flags_integration_test")
      .withUsername("test_user")
      .withPassword("test_password")
      .withInitScript("init-test-db.sql");

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
    registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);

    // Configurações específicas para testes de integração
    registry.add("spring.jpa.hibernate.ddl-auto", () -> "validate");
    registry.add("spring.flyway.enabled", () -> "true");
    registry.add("spring.flyway.locations", () -> "classpath:db/migration");
    registry.add("spring.flyway.baseline-on-migrate", () -> "true");
    registry.add("spring.flyway.schemas", () -> "feature_flags_test");
    registry.add("spring.flyway.default-schema", () -> "feature_flags_test");

    // Configurações de logging para debug
    registry.add("logging.level.org.springframework.jdbc", () -> "DEBUG");
    registry.add("logging.level.org.flywaydb", () -> "DEBUG");
  }
}