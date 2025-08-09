package com.renanloureiro.feature_flags.application.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.renanloureiro.feature_flags.application.exceptions.ValidationException;
import com.renanloureiro.feature_flags.domain.FeatureFlag;
import com.renanloureiro.feature_flags.domain.FeatureFlagType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Serviço responsável pela validação de valores de feature flags contra seus
 * tipos e constraints
 */
@Service
@Slf4j
public class FeatureFlagValueValidationService {

  /**
   * Valida e processa um valor para uma feature flag específica
   * 
   * @param featureFlag A feature flag que contém o tipo e constraints
   * @param value       O valor a ser validado
   * @return O valor validado e processado
   * @throws ValidationException se o valor não for válido para o tipo e
   *                             constraints da feature flag
   */
  public JsonNode validateAndProcessValue(FeatureFlag featureFlag, JsonNode value) {
    log.debug("Validating value for feature flag: {} (type: {})",
        featureFlag.getSlug(), featureFlag.getType());

    if (value == null) {
      throw new ValidationException("Valor não pode ser nulo");
    }

    validateValueByType(featureFlag, value);
    validateValueByConstraints(featureFlag, value);

    log.debug("Value validation successful for feature flag: {}", featureFlag.getSlug());
    return value;
  }

  /**
   * Valida se o valor está de acordo com o tipo da feature flag
   */
  private void validateValueByType(FeatureFlag featureFlag, JsonNode value) {
    FeatureFlagType type = featureFlag.getType();

    switch (type) {
      case BOOLEAN -> {
        if (!value.isBoolean()) {
          throw new ValidationException(
              String.format("Valor deve ser do tipo boolean para feature flag '%s'", featureFlag.getName()));
        }
      }
      case NUMBER -> {
        if (!value.isNumber()) {
          throw new ValidationException(
              String.format("Valor deve ser do tipo number para feature flag '%s'", featureFlag.getName()));
        }
      }
      case STRING -> {
        if (!value.isTextual()) {
          throw new ValidationException(
              String.format("Valor deve ser do tipo string para feature flag '%s'", featureFlag.getName()));
        }
      }
      case LIST -> {
        if (!value.isArray()) {
          throw new ValidationException(
              String.format("Valor deve ser do tipo array para feature flag '%s'", featureFlag.getName()));
        }
      }
      default -> throw new ValidationException("Tipo de feature flag não suportado: " + type);
    }
  }

  /**
   * Valida se o valor está de acordo com as constraints específicas do schema
   */
  private void validateValueByConstraints(FeatureFlag featureFlag, JsonNode value) {
    JsonNode schema = featureFlag.getSchema();
    if (schema == null) {
      return; // Sem constraints para validar
    }

    FeatureFlagType type = featureFlag.getType();

    switch (type) {
      case NUMBER -> validateNumberConstraints(featureFlag, value, schema);
      case STRING -> validateStringConstraints(featureFlag, value, schema);
      case LIST -> validateListConstraints(featureFlag, value, schema);
      case BOOLEAN -> {
        // Boolean não tem constraints específicas além do tipo
      }
    }
  }

  private void validateNumberConstraints(FeatureFlag featureFlag, JsonNode value, JsonNode schema) {
    double numberValue = value.asDouble();

    if (schema.has("minimum")) {
      double minimum = schema.get("minimum").asDouble();
      if (numberValue < minimum) {
        throw new ValidationException(
            String.format("Valor %.2f é menor que o mínimo permitido (%.2f) para feature flag '%s'",
                numberValue, minimum, featureFlag.getName()));
      }
    }

    if (schema.has("maximum")) {
      double maximum = schema.get("maximum").asDouble();
      if (numberValue > maximum) {
        throw new ValidationException(
            String.format("Valor %.2f é maior que o máximo permitido (%.2f) para feature flag '%s'",
                numberValue, maximum, featureFlag.getName()));
      }
    }
  }

  private void validateStringConstraints(FeatureFlag featureFlag, JsonNode value, JsonNode schema) {
    String stringValue = value.asText();

    if (schema.has("minLength")) {
      int minLength = schema.get("minLength").asInt();
      if (stringValue.length() < minLength) {
        throw new ValidationException(
            String.format("String deve ter pelo menos %d caracteres para feature flag '%s'",
                minLength, featureFlag.getName()));
      }
    }

    if (schema.has("maxLength")) {
      int maxLength = schema.get("maxLength").asInt();
      if (stringValue.length() > maxLength) {
        throw new ValidationException(
            String.format("String deve ter no máximo %d caracteres para feature flag '%s'",
                maxLength, featureFlag.getName()));
      }
    }

    if (schema.has("enum")) {
      JsonNode enumNode = schema.get("enum");
      boolean isValidEnum = false;

      for (JsonNode enumValue : enumNode) {
        if (enumValue.asText().equals(stringValue)) {
          isValidEnum = true;
          break;
        }
      }

      if (!isValidEnum) {
        throw new ValidationException(
            String.format("Valor '%s' não está entre os valores permitidos para feature flag '%s'",
                stringValue, featureFlag.getName()));
      }
    }
  }

  private void validateListConstraints(FeatureFlag featureFlag, JsonNode value, JsonNode schema) {
    if (!value.isArray()) {
      throw new ValidationException(
          String.format("Valor deve ser um array para feature flag '%s'", featureFlag.getName()));
    }

    if (schema.has("minItems")) {
      int minItems = schema.get("minItems").asInt();
      if (value.size() < minItems) {
        throw new ValidationException(
            String.format("Array deve ter pelo menos %d itens para feature flag '%s'",
                minItems, featureFlag.getName()));
      }
    }

    if (schema.has("maxItems")) {
      int maxItems = schema.get("maxItems").asInt();
      if (value.size() > maxItems) {
        throw new ValidationException(
            String.format("Array deve ter no máximo %d itens para feature flag '%s'",
                maxItems, featureFlag.getName()));
      }
    }

    // Valida o tipo dos itens se especificado
    if (schema.has("items") && schema.get("items").has("type")) {
      String itemType = schema.get("items").get("type").asText();

      for (int i = 0; i < value.size(); i++) {
        JsonNode item = value.get(i);
        validateItemType(featureFlag, item, itemType, i);
      }
    }
  }

  private void validateItemType(FeatureFlag featureFlag, JsonNode item, String expectedType, int index) {
    boolean isValid = switch (expectedType) {
      case "string" -> item.isTextual();
      case "number" -> item.isNumber();
      case "boolean" -> item.isBoolean();
      default -> true; // Tipo não reconhecido, aceita qualquer valor
    };

    if (!isValid) {
      throw new ValidationException(
          String.format("Item na posição %d deve ser do tipo %s para feature flag '%s'",
              index, expectedType, featureFlag.getName()));
    }
  }
}
