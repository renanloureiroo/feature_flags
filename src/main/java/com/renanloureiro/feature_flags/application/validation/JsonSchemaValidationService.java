package com.renanloureiro.feature_flags.application.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.renanloureiro.feature_flags.application.exceptions.ValidationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JsonSchemaValidationService {
  private final ObjectMapper objectMapper;
  private final JsonSchemaFactory schemaFactory;

  public JsonSchemaValidationService(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
    this.schemaFactory = JsonSchemaFactory.byDefault();
  }

  /**
   * Valida se o schema JSON é válido
   * 
   * @param schema O schema JSON a ser validado
   * @throws ValidationException se o schema for inválido
   */
  public void validateSchema(JsonNode schema) {
    try {
      schemaFactory.getJsonSchema(schema);
    } catch (ProcessingException e) {
      throw new ValidationException("Erro ao processar schema JSON: " + e.getMessage());
    }
  }

  /**
   * Valida se um documento JSON está de acordo com o schema fornecido
   * 
   * @param schema   O schema JSON
   * @param document O documento JSON a ser validado
   * @throws ValidationException se o documento não estiver de acordo com o schema
   */
  public void validateDocument(JsonNode schema, JsonNode document) {
    try {
      JsonSchema jsonSchema = schemaFactory.getJsonSchema(schema);
      ProcessingReport report = jsonSchema.validate(document);

      if (!report.isSuccess()) {
        List<String> errors = new ArrayList<>();
        report.forEach(processingMessage -> errors.add(processingMessage.getMessage()));
        throw new ValidationException("Documento não está de acordo com o schema: " + String.join(", ", errors));
      }

    } catch (ProcessingException e) {
      throw new ValidationException("Erro ao validar documento contra schema: " + e.getMessage());
    }
  }

  /**
   * Valida se o schema é compatível com o tipo de feature flag especificado
   * 
   * @param schema          O schema JSON
   * @param featureFlagType O tipo da feature flag
   * @throws ValidationException se o schema não for compatível com o tipo
   */
  public void validateSchemaForType(JsonNode schema, String featureFlagType) {
    // Validações específicas por tipo
    switch (featureFlagType) {
      case "BOOLEAN":
        validateBooleanSchema(schema);
        break;
      case "NUMBER":
        validateNumberSchema(schema);
        break;
      case "STRING":
        validateStringSchema(schema);
        break;
      case "LIST":
        validateListSchema(schema);
        break;
      default:
        throw new ValidationException("Tipo de feature flag não suportado: " + featureFlagType);
    }
  }

  private void validateBooleanSchema(JsonNode schema) {
    if (!schema.has("type") || !"boolean".equals(schema.get("type").asText())) {
      throw new ValidationException("Schema para BOOLEAN deve ser do tipo 'boolean'");
    }
  }

  private void validateNumberSchema(JsonNode schema) {
    if (!schema.has("type") || !"number".equals(schema.get("type").asText())) {
      throw new ValidationException("Schema para NUMBER deve ser do tipo 'number'");
    }
  }

  private void validateStringSchema(JsonNode schema) {
    if (!schema.has("type") || !"string".equals(schema.get("type").asText())) {
      throw new ValidationException("Schema para STRING deve ser do tipo 'string'");
    }
  }

  private void validateListSchema(JsonNode schema) {
    if (!schema.has("type") || !"array".equals(schema.get("type").asText())) {
      throw new ValidationException("Schema para LIST deve ser do tipo 'array'");
    }

    if (!schema.has("items")) {
      throw new ValidationException("Schema para LIST deve definir o tipo dos itens");
    }
  }
}