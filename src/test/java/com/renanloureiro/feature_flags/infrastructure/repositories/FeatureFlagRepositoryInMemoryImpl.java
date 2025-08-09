package com.renanloureiro.feature_flags.infrastructure.repositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.renanloureiro.feature_flags.application.repositories.FeatureFlagRepository;
import com.renanloureiro.feature_flags.domain.FeatureFlag;

public class FeatureFlagRepositoryInMemoryImpl implements FeatureFlagRepository {

  private final Map<UUID, FeatureFlag> featureFlags = new HashMap<>();

  @Override
  public FeatureFlag save(FeatureFlag featureFlag) {
    if (featureFlag.getId() == null) {
      featureFlag.setId(UUID.randomUUID());
    }

    featureFlags.put(featureFlag.getId(), featureFlag);

    return featureFlag;
  }

  @Override
  public Optional<FeatureFlag> findBySlug(String slug) {
    return featureFlags.values().stream()
        .filter(featureFlag -> featureFlag.getSlug().equals(slug))
        .findFirst();
  }

  @Override
  public boolean existsBySlug(String slug) {
    return featureFlags.values().stream()
        .anyMatch(featureFlag -> featureFlag.getSlug().equals(slug));
  }

  @Override
  public List<FeatureFlag> findAll() {
    return new ArrayList<FeatureFlag>(featureFlags.values());
  }

  public void clear() {
    featureFlags.clear();
  }

  @Override
  public Optional<FeatureFlag> findById(UUID id) {
    return Optional.ofNullable(featureFlags.get(id));
  }
}