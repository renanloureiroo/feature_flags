package com.renanloureiro.feature_flags.infrastructure;

import java.util.Map;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@TestConfiguration
@Testcontainers
public class TestContainersConfig {

  @Container
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
      .withDatabaseName("feature_flags_test")
      .withUsername("test_user")
      .withPassword("test_password")
      .withInitScript("init-test-db.sql");

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
    registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);

    // Configurações específicas para testes
    registry.add("spring.jpa.hibernate.ddl-auto", () -> "validate");
    registry.add("spring.flyway.enabled", () -> "true");
    registry.add("spring.flyway.locations", () -> "classpath:db/migration");
    registry.add("spring.flyway.baseline-on-migrate", () -> "true");
  }

  // Os repositórios agora são interfaces que implementam diretamente
  // FeatureFlagJpaRepository e FeatureFlagValueJpaRepository
  // são injetados automaticamente pelo Spring
}