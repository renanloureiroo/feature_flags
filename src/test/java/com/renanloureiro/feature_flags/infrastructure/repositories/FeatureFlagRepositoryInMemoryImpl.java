package com.renanloureiro.feature_flags.infrastructure.repositories;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.renanloureiro.feature_flags.application.repositories.FeatureFlagRepository;
import com.renanloureiro.feature_flags.domain.FeatureFlag;

public class FeatureFlagRepositoryInMemoryImpl implements FeatureFlagRepository {

  private final Map<UUID, FeatureFlag> featureFlags = new HashMap<>();
  private final Map<String, FeatureFlag> featureFlagsBySlug = new HashMap<>();

  @Override
  public FeatureFlag save(FeatureFlag featureFlag) {
    if (featureFlag.getId() == null) {
      featureFlag.setId(UUID.randomUUID());
    }

    featureFlags.put(featureFlag.getId(), featureFlag);
    featureFlagsBySlug.put(featureFlag.getSlug(), featureFlag);

    return featureFlag;
  }

  @Override
  public Optional<FeatureFlag> findBySlug(String slug) {
    return Optional.ofNullable(featureFlagsBySlug.get(slug));
  }

  @Override
  public boolean existsBySlug(String slug) {
    return featureFlagsBySlug.containsKey(slug);
  }

  public void clear() {
    featureFlags.clear();
    featureFlagsBySlug.clear();
  }
}