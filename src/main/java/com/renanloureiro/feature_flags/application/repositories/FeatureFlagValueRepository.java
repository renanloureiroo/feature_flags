package com.renanloureiro.feature_flags.application.repositories;

import java.util.Optional;
import java.util.UUID;

import com.renanloureiro.feature_flags.domain.FeatureFlag;
import com.renanloureiro.feature_flags.domain.FeatureFlagValue;

public interface FeatureFlagValueRepository {

  FeatureFlagValue save(FeatureFlagValue featureFlagValue);

  Optional<FeatureFlagValue> findByFlagAndVersion(FeatureFlag flag, Integer version);

  boolean existsByFlagAndVersion(FeatureFlag flag, Integer version);

  Optional<Integer> findLatestVersionByFlagId(UUID flagId);
}