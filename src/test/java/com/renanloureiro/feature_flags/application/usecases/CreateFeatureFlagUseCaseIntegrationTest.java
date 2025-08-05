package com.renanloureiro.feature_flags.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.renanloureiro.feature_flags.application.dtos.CreateFeatureFlagDTO;
import com.renanloureiro.feature_flags.application.repositories.FeatureFlagRepository;
import com.renanloureiro.feature_flags.application.validation.JsonSchemaValidationService;
import com.renanloureiro.feature_flags.domain.FeatureFlag;
import com.renanloureiro.feature_flags.domain.FeatureFlagType;

@SpringBootTest(classes = com.renanloureiro.feature_flags.infrastructure.FeatureFlagsApplication.class)
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Testcontainers
class CreateFeatureFlagUseCaseIntegrationTest {

  @Container
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
      .withDatabaseName("feature_flags_integration_test")
      .withUsername("test_user")
      .withPassword("test_password")
      .withInitScript("init-test-db.sql");

  @Autowired
  private CreateFeatureFlagUseCase useCase;

  @Autowired
  private FeatureFlagRepository featureFlagRepository;

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
    registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);
  }

  @Autowired
  private JsonSchemaValidationService schemaValidationService;

  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    objectMapper = new ObjectMapper();
  }

  @Test
  @DisplayName("Deve criar feature flag com repositório real")
  void shouldCreateFeatureFlagWithRealRepository() throws Exception {
    // Arrange
    String name = "Integration Test Flag";
    String description = "Integration test description";
    FeatureFlagType type = FeatureFlagType.BOOLEAN;
    JsonNode schema = objectMapper.readTree("{\"type\": \"boolean\"}");

    CreateFeatureFlagDTO dto = CreateFeatureFlagDTO.builder()
        .name(name)
        .description(description)
        .type(type)
        .schema(schema)
        .build();

    // Act
    FeatureFlag result = useCase.execute(dto);

    // Assert
    assertNotNull(result);
    assertEquals(name, result.getName());
    assertEquals("integration-test-flag", result.getSlug());
    assertEquals(description, result.getDescription());
    assertEquals(type, result.getType());
    assertEquals(schema, result.getSchema());

    // Verificar se foi salvo no repositório
    var savedFlag = featureFlagRepository.findBySlug("integration-test-flag");
    assertNotNull(savedFlag);
    assertNotNull(savedFlag.get());
    assertEquals(result.getId(), savedFlag.get().getId());
  }
}