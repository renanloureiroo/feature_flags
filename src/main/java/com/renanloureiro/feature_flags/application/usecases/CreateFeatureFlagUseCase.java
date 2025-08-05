package com.renanloureiro.feature_flags.application.usecases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.renanloureiro.feature_flags.application.dtos.CreateFeatureFlagDTO;
import com.renanloureiro.feature_flags.application.exceptions.SlugAlreadyExists;
import com.renanloureiro.feature_flags.application.repositories.FeatureFlagRepository;
import com.renanloureiro.feature_flags.application.validation.JsonSchemaValidationService;
import com.renanloureiro.feature_flags.domain.FeatureFlag;

@Service
@Transactional
public class CreateFeatureFlagUseCase {

  @Autowired
  private FeatureFlagRepository featureFlagRepository;

  @Autowired
  private JsonSchemaValidationService schemaValidationService;

  public FeatureFlag execute(CreateFeatureFlagDTO dto) {

    // Valida o schema JSON
    schemaValidationService.validateSchema(dto.getSchema());
    schemaValidationService.validateSchemaForType(dto.getSchema(), dto.getType().name());

    var slugAlreadyExists = featureFlagRepository.existsBySlug(FeatureFlag.createSlugByName(dto.getName()));

    if (slugAlreadyExists) {
      throw new SlugAlreadyExists();
    }

    FeatureFlag featureFlag = FeatureFlag.builder()
        .name(dto.getName())
        .slug(FeatureFlag.createSlugByName(dto.getName()))
        .type(dto.getType())
        .schema(dto.getSchema())
        .description(dto.getDescription())
        .build();

    return featureFlagRepository.save(featureFlag);
  }

}