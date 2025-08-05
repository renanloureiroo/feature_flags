package com.renanloureiro.feature_flags.infrastructure.repositories;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.renanloureiro.feature_flags.application.repositories.FeatureFlagValueRepository;
import com.renanloureiro.feature_flags.domain.FeatureFlag;
import com.renanloureiro.feature_flags.domain.FeatureFlagValue;

public class FeatureFlagValueRepositoryInMemoryImpl implements FeatureFlagValueRepository {

  private final Map<UUID, FeatureFlagValue> featureFlagValues = new HashMap<>();
  private final Map<String, FeatureFlagValue> featureFlagValuesByFlagAndVersion = new HashMap<>();

  @Override
  public FeatureFlagValue save(FeatureFlagValue featureFlagValue) {
    if (featureFlagValue.getId() == null) {
      featureFlagValue.setId(UUID.randomUUID());
    }

    featureFlagValues.put(featureFlagValue.getId(), featureFlagValue);

    String key = featureFlagValue.getFlag().getId() + "_" + featureFlagValue.getVersion();
    featureFlagValuesByFlagAndVersion.put(key, featureFlagValue);

    return featureFlagValue;
  }

  @Override
  public Optional<FeatureFlagValue> findByFlagAndVersion(FeatureFlag flag, Integer version) {
    String key = flag.getId() + "_" + version;
    return Optional.ofNullable(featureFlagValuesByFlagAndVersion.get(key));
  }

  @Override
  public boolean existsByFlagAndVersion(FeatureFlag flag, Integer version) {
    String key = flag.getId() + "_" + version;
    return featureFlagValuesByFlagAndVersion.containsKey(key);
  }

  public void clear() {
    featureFlagValues.clear();
    featureFlagValuesByFlagAndVersion.clear();
  }
}