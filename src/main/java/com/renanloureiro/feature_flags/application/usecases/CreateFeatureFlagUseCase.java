package com.renanloureiro.feature_flags.application.usecases;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.renanloureiro.feature_flags.application.dtos.CreateFeatureFlagDTO;
import com.renanloureiro.feature_flags.application.exceptions.SlugAlreadyExists;
import com.renanloureiro.feature_flags.application.repositories.FeatureFlagRepository;
import com.renanloureiro.feature_flags.application.services.SchemaGeneratorService;
import com.renanloureiro.feature_flags.application.validation.JsonSchemaValidationService;
import com.renanloureiro.feature_flags.domain.FeatureFlag;

@Service
@Transactional
public class CreateFeatureFlagUseCase {

  private final FeatureFlagRepository featureFlagRepository;

  private final JsonSchemaValidationService schemaValidationService;

  private final SchemaGeneratorService schemaGeneratorService;

  public CreateFeatureFlagUseCase(FeatureFlagRepository featureFlagRepository,
      JsonSchemaValidationService schemaValidationService, SchemaGeneratorService schemaGeneratorService) {
    this.featureFlagRepository = featureFlagRepository;
    this.schemaValidationService = schemaValidationService;
    this.schemaGeneratorService = schemaGeneratorService;
  }

  public FeatureFlag execute(CreateFeatureFlagDTO dto) {

    validateConstraints(dto);

    JsonNode schema = generateSchemaFromConstraints(dto);

    schemaValidationService.validateSchema(schema);
    schemaValidationService.validateSchemaForType(schema, dto.getType().name());

    var slugAlreadyExists = featureFlagRepository.existsBySlug(FeatureFlag.createSlugByName(dto.getName()));

    if (slugAlreadyExists) {
      throw new SlugAlreadyExists();
    }

    FeatureFlag featureFlag = FeatureFlag.builder()
        .name(dto.getName())
        .slug(FeatureFlag.createSlugByName(dto.getName()))
        .type(dto.getType())
        .schema(schema)
        .description(dto.getDescription())
        .build();

    return featureFlagRepository.save(featureFlag);
  }

  private void validateConstraints(CreateFeatureFlagDTO dto) {
    switch (dto.getType()) {
      case NUMBER:
        if (dto.getNumberConstraints() != null) {
          validateNumberConstraints(dto.getNumberConstraints());
        }
        break;
      case STRING:
        if (dto.getStringConstraints() != null) {
          validateStringConstraints(dto.getStringConstraints());
        }
        break;
      case LIST:
        if (dto.getListConstraints() != null) {
          validateListConstraints(dto.getListConstraints());
        }
        break;
      case BOOLEAN:
        break;
    }
  }

  private void validateNumberConstraints(
      com.renanloureiro.feature_flags.application.dtos.NumberConstraints constraints) {
    if (constraints.getMinimum() != null && constraints.getMaximum() != null) {
      if (constraints.getMinimum() > constraints.getMaximum()) {
        throw new IllegalArgumentException("Valor mínimo não pode ser maior que o valor máximo");
      }
    }
  }

  private void validateStringConstraints(
      com.renanloureiro.feature_flags.application.dtos.StringConstraints constraints) {
    if (constraints.getMinLength() != null && constraints.getMaxLength() != null) {
      if (constraints.getMinLength() > constraints.getMaxLength()) {
        throw new IllegalArgumentException("Comprimento mínimo não pode ser maior que o comprimento máximo");
      }
    }
  }

  private void validateListConstraints(com.renanloureiro.feature_flags.application.dtos.ListConstraints constraints) {
    if (constraints.getMinItems() != null && constraints.getMaxItems() != null) {
      if (constraints.getMinItems() > constraints.getMaxItems()) {
        throw new IllegalArgumentException("Número mínimo de itens não pode ser maior que o número máximo");
      }
    }

    if (constraints.getItemType() != null) {
      String itemType = constraints.getItemType().toLowerCase();
      if (!itemType.equals("string") && !itemType.equals("number") && !itemType.equals("boolean")) {
        throw new IllegalArgumentException("Tipo de item deve ser 'string', 'number' ou 'boolean'");
      }
    }
  }

  private JsonNode generateSchemaFromConstraints(CreateFeatureFlagDTO dto) {
    if (dto.getSchema() != null) {
      return dto.getSchema();
    }

    Object constraints = getConstraintsForType(dto);
    return schemaGeneratorService.generateSchema(dto.getType(), constraints);
  }

  private Object getConstraintsForType(CreateFeatureFlagDTO dto) {
    switch (dto.getType()) {
      case NUMBER:
        return dto.getNumberConstraints();
      case STRING:
        return dto.getStringConstraints();
      case LIST:
        return dto.getListConstraints();
      case BOOLEAN:
        return null;
      default:
        throw new IllegalArgumentException("Tipo não suportado: " + dto.getType());
    }
  }

}