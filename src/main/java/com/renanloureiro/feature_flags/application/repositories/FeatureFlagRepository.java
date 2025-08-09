package com.renanloureiro.feature_flags.application.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.renanloureiro.feature_flags.domain.FeatureFlag;

public interface FeatureFlagRepository {

  FeatureFlag save(FeatureFlag featureFlag);

  Optional<FeatureFlag> findBySlug(String slug);

  boolean existsBySlug(String slug);

  List<FeatureFlag> findAll();

  Optional<FeatureFlag> findById(UUID id);
}