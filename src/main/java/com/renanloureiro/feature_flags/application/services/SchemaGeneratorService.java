package com.renanloureiro.feature_flags.application.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.renanloureiro.feature_flags.application.dtos.ListConstraints;
import com.renanloureiro.feature_flags.application.dtos.NumberConstraints;
import com.renanloureiro.feature_flags.application.dtos.StringConstraints;
import com.renanloureiro.feature_flags.domain.FeatureFlagType;
import org.springframework.stereotype.Service;

@Service
public class SchemaGeneratorService {

  private final ObjectMapper objectMapper;

  public SchemaGeneratorService(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public JsonNode generateSchema(FeatureFlagType type, Object constraints) {
    ObjectNode schema = objectMapper.createObjectNode();

    switch (type) {
      case BOOLEAN:
        return generateBooleanSchema();
      case NUMBER:
        return generateNumberSchema((NumberConstraints) constraints);
      case STRING:
        return generateStringSchema((StringConstraints) constraints);
      case LIST:
        return generateListSchema((ListConstraints) constraints);
      default:
        throw new IllegalArgumentException("Tipo não suportado: " + type);
    }
  }

  private JsonNode generateBooleanSchema() {
    ObjectNode schema = objectMapper.createObjectNode();
    schema.put("type", "boolean");
    return schema;
  }

  private JsonNode generateNumberSchema(NumberConstraints constraints) {
    ObjectNode schema = objectMapper.createObjectNode();
    schema.put("type", "number");

    if (constraints != null) {
      if (constraints.getMinimum() != null) {
        schema.put("minimum", constraints.getMinimum());
      }
      if (constraints.getMaximum() != null) {
        schema.put("maximum", constraints.getMaximum());
      }
      if (constraints.getExclusiveMinimum() != null) {
        schema.put("exclusiveMinimum", constraints.getExclusiveMinimum());
      }
      if (constraints.getExclusiveMaximum() != null) {
        schema.put("exclusiveMaximum", constraints.getExclusiveMaximum());
      }
    }

    return schema;
  }

  private JsonNode generateStringSchema(StringConstraints constraints) {
    ObjectNode schema = objectMapper.createObjectNode();
    schema.put("type", "string");

    if (constraints != null) {
      if (constraints.getEnumValues() != null && !constraints.getEnumValues().isEmpty()) {
        schema.set("enum", objectMapper.valueToTree(constraints.getEnumValues()));
      }
      if (constraints.getPattern() != null) {
        schema.put("pattern", constraints.getPattern());
      }
      if (constraints.getMinLength() != null) {
        schema.put("minLength", constraints.getMinLength());
      }
      if (constraints.getMaxLength() != null) {
        schema.put("maxLength", constraints.getMaxLength());
      }
    }

    return schema;
  }

  private JsonNode generateListSchema(ListConstraints constraints) {
    ObjectNode schema = objectMapper.createObjectNode();
    schema.put("type", "array");

    if (constraints != null) {
      // Schema para os itens
      ObjectNode itemsSchema = objectMapper.createObjectNode();
      itemsSchema.put("type", constraints.getItemType());
      schema.set("items", itemsSchema);

      if (constraints.getMinItems() != null) {
        schema.put("minItems", constraints.getMinItems());
      }
      if (constraints.getMaxItems() != null) {
        schema.put("maxItems", constraints.getMaxItems());
      }
      if (constraints.getUniqueItems() != null) {
        schema.put("uniqueItems", constraints.getUniqueItems());
      }
    }

    return schema;
  }
}