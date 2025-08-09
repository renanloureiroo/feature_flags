package com.renanloureiro.feature_flags.infrastructure.repositories;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.renanloureiro.feature_flags.application.repositories.FeatureFlagValueRepository;
import com.renanloureiro.feature_flags.domain.FeatureFlag;
import com.renanloureiro.feature_flags.domain.FeatureFlagValue;

public class FeatureFlagValueRepositoryInMemoryImpl implements FeatureFlagValueRepository {

  private final Map<UUID, FeatureFlagValue> featureFlagValues = new HashMap<>();

  @Override
  public FeatureFlagValue save(FeatureFlagValue featureFlagValue) {
    if (featureFlagValue.getId() == null) {
      featureFlagValue.setId(UUID.randomUUID());
    }

    featureFlagValues.put(featureFlagValue.getId(), featureFlagValue);

    return featureFlagValue;
  }

  @Override
  public Optional<FeatureFlagValue> findByFlagAndVersion(FeatureFlag flag, Integer version) {
    return featureFlagValues.values().stream()
        .filter(featureFlagValue -> featureFlagValue.getFlag().getId().equals(flag.getId())
            && featureFlagValue.getVersion().equals(version))
        .findFirst();
  }

  @Override
  public boolean existsByFlagAndVersion(FeatureFlag flag, Integer version) {
    return featureFlagValues.values().stream()
        .anyMatch(featureFlagValue -> featureFlagValue.getFlag().getId().equals(flag.getId())
            && featureFlagValue.getVersion().equals(version));
  }

  public void clear() {
    featureFlagValues.clear();
  }

  @Override
  public Optional<Integer> findLatestVersionByFlagId(UUID flagId) {
    return featureFlagValues.values().stream()
        .filter(featureFlagValue -> featureFlagValue.getFlag().getId().equals(flagId))
        .map(FeatureFlagValue::getVersion)
        .max(Comparator.naturalOrder());
  }
}