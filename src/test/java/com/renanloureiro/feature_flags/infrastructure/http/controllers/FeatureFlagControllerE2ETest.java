package com.renanloureiro.feature_flags.infrastructure.http.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.renanloureiro.feature_flags.application.dtos.CreateFeatureFlagDTO;
import com.renanloureiro.feature_flags.domain.FeatureFlagType;
import com.renanloureiro.feature_flags.infrastructure.FeatureFlagsApplication;
import com.renanloureiro.feature_flags.infrastructure.IntegrationTestConfig;

@SpringBootTest(classes = FeatureFlagsApplication.class)
@AutoConfigureWebMvc
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Testcontainers
class FeatureFlagControllerE2ETest {

  @Container
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
      .withDatabaseName("feature_flags_e2e_test")
      .withUsername("test_user")
      .withPassword("test_password")
      .withInitScript("init-test-db.sql");

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Autowired
  private ObjectMapper objectMapper;

  private MockMvc mockMvc;

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
    registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);
  }

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
  }

  @Test
  @DisplayName("E2E: Deve criar feature flag via API REST")
  void shouldCreateFeatureFlagViaAPI() throws Exception {
    // Arrange
    CreateFeatureFlagDTO dto = CreateFeatureFlagDTO.builder()
        .name("E2E Test Flag")
        .description("E2E test description")
        .type(FeatureFlagType.BOOLEAN)
        .schema(objectMapper.readTree("{\"type\": \"boolean\"}"))
        .build();

    String requestBody = objectMapper.writeValueAsString(dto);

    // Act & Assert
    mockMvc.perform(post("/v1/feature-flags")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name").value("E2E Test Flag"))
        .andExpect(jsonPath("$.slug").value("e2e-test-flag"))
        .andExpect(jsonPath("$.type").value("BOOLEAN"))
        .andExpect(jsonPath("$.description").value("E2E test description"))
        .andExpect(jsonPath("$.id").exists());
  }

  @Test
  @DisplayName("E2E: Deve rejeitar feature flag com slug duplicado")
  void shouldRejectDuplicateSlug() throws Exception {
    // Arrange
    CreateFeatureFlagDTO dto = CreateFeatureFlagDTO.builder()
        .name("Duplicate Test Flag")
        .description("Test description")
        .type(FeatureFlagType.STRING)
        .schema(objectMapper.readTree("{\"type\": \"string\"}"))
        .build();

    String requestBody = objectMapper.writeValueAsString(dto);

    // Primeira criação - deve funcionar
    mockMvc.perform(post("/v1/feature-flags")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody))
        .andExpect(status().isCreated());

    // Segunda criação com mesmo nome - deve falhar
    mockMvc.perform(post("/v1/feature-flags")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody))
        .andExpect(status().isConflict())
        .andExpect(
            jsonPath("$.message").value("Already exists a feature flag with this slug, please try another name"));
  }
}