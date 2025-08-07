package com.renanloureiro.feature_flags.application.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.renanloureiro.feature_flags.application.dtos.ListConstraints;
import com.renanloureiro.feature_flags.application.dtos.NumberConstraints;
import com.renanloureiro.feature_flags.application.dtos.StringConstraints;
import com.renanloureiro.feature_flags.domain.FeatureFlagType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SchemaGeneratorServiceTest {

  private SchemaGeneratorService schemaGeneratorService;
  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    objectMapper = new ObjectMapper();
    schemaGeneratorService = new SchemaGeneratorService(objectMapper);
  }

  @Test
  void shouldGenerateBooleanSchema() {
    JsonNode schema = schemaGeneratorService.generateSchema(FeatureFlagType.BOOLEAN, null);

    assertEquals("boolean", schema.get("type").asText());
    assertEquals(1, schema.size());
  }

  @Test
  void shouldGenerateNumberSchemaWithConstraints() {
    NumberConstraints constraints = NumberConstraints.builder()
        .minimum(1.0)
        .maximum(10.0)
        .exclusiveMinimum(false)
        .exclusiveMaximum(false)
        .build();

    JsonNode schema = schemaGeneratorService.generateSchema(FeatureFlagType.NUMBER, constraints);

    assertEquals("number", schema.get("type").asText());
    assertEquals(1.0, schema.get("minimum").asDouble());
    assertEquals(10.0, schema.get("maximum").asDouble());
    assertEquals(false, schema.get("exclusiveMinimum").asBoolean());
    assertEquals(false, schema.get("exclusiveMaximum").asBoolean());
  }

  @Test
  void shouldGenerateNumberSchemaWithoutConstraints() {
    JsonNode schema = schemaGeneratorService.generateSchema(FeatureFlagType.NUMBER, null);

    assertEquals("number", schema.get("type").asText());
    assertEquals(1, schema.size());
  }

  @Test
  void shouldGenerateStringSchemaWithEnum() {
    StringConstraints constraints = StringConstraints.builder()
        .enumValues(java.util.List.of("v1", "v2", "v3"))
        .minLength(1)
        .maxLength(10)
        .build();

    JsonNode schema = schemaGeneratorService.generateSchema(FeatureFlagType.STRING, constraints);

    assertEquals("string", schema.get("type").asText());
    assertTrue(schema.has("enum"));
    assertEquals(3, schema.get("enum").size());
    assertEquals("v1", schema.get("enum").get(0).asText());
    assertEquals(1, schema.get("minLength").asInt());
    assertEquals(10, schema.get("maxLength").asInt());
  }

  @Test
  void shouldGenerateStringSchemaWithPattern() {
    StringConstraints constraints = StringConstraints.builder()
        .pattern("^[a-zA-Z0-9]+$")
        .build();

    JsonNode schema = schemaGeneratorService.generateSchema(FeatureFlagType.STRING, constraints);

    assertEquals("string", schema.get("type").asText());
    assertEquals("^[a-zA-Z0-9]+$", schema.get("pattern").asText());
  }

  @Test
  void shouldGenerateListSchemaWithConstraints() {
    ListConstraints constraints = ListConstraints.builder()
        .itemType("string")
        .minItems(1)
        .maxItems(10)
        .uniqueItems(true)
        .build();

    JsonNode schema = schemaGeneratorService.generateSchema(FeatureFlagType.LIST, constraints);

    assertEquals("array", schema.get("type").asText());
    assertEquals("string", schema.get("items").get("type").asText());
    assertEquals(1, schema.get("minItems").asInt());
    assertEquals(10, schema.get("maxItems").asInt());
    assertEquals(true, schema.get("uniqueItems").asBoolean());
  }

  @Test
  void shouldThrowExceptionForUnsupportedType() {
    assertThrows(IllegalArgumentException.class, () -> {
      schemaGeneratorService.generateSchema(null, null);
    });
  }
}