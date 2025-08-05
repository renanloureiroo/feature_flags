package com.renanloureiro.feature_flags.application.repositories;

import java.util.Optional;

import com.renanloureiro.feature_flags.domain.FeatureFlag;

public interface FeatureFlagRepository {

  FeatureFlag save(FeatureFlag featureFlag);

  Optional<FeatureFlag> findBySlug(String slug);

  boolean existsBySlug(String slug);
}