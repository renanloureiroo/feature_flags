package com.renanloureiro.feature_flags.application.usecases;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.renanloureiro.feature_flags.application.dtos.CreateFeatureFlagValueDTO;
import com.renanloureiro.feature_flags.application.exceptions.FeatureFlagNotFound;
import com.renanloureiro.feature_flags.application.repositories.FeatureFlagRepository;
import com.renanloureiro.feature_flags.application.repositories.FeatureFlagValueRepository;
import com.renanloureiro.feature_flags.application.services.FeatureFlagValueValidationService;
import com.renanloureiro.feature_flags.domain.FeatureFlagValue;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CreateFeatureFlagValueUseCase {

  private final FeatureFlagValueRepository featureFlagValueRepository;
  private final FeatureFlagRepository featureFlagRepository;
  private final FeatureFlagValueValidationService validationService;

  public FeatureFlagValue execute(UUID flagId, CreateFeatureFlagValueDTO dto) {
    log.info("Creating new feature flag value for flag ID: {}", flagId);

    var featureFlag = featureFlagRepository.findById(flagId)
        .orElseThrow(() -> new FeatureFlagNotFound());

    var validatedValue = validationService.validateAndProcessValue(featureFlag, dto.getValue());
    log.info("Value validated successfully for feature flag type: {}", featureFlag.getType());

    var nextVersion = featureFlagValueRepository.findLatestVersionByFlagId(flagId)
        .map(version -> version + 1)
        .orElse(1);

    var featureFlagValue = FeatureFlagValue.builder()
        .flag(featureFlag)
        .value(validatedValue)
        .version(nextVersion)
        .updatedBy("api")
        .build();

    var savedValue = featureFlagValueRepository.save(featureFlagValue);
    log.info("Feature flag value created successfully with ID: {} and version: {}",
        savedValue.getId(), savedValue.getVersion());

    return savedValue;
  }
}
