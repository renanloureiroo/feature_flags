package com.renanloureiro.feature_flags.infrastructure.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.renanloureiro.feature_flags.application.repositories.FeatureFlagRepository;
import com.renanloureiro.feature_flags.domain.FeatureFlag;

@Repository
public interface FeatureFlagJpaRepository extends JpaRepository<FeatureFlag, UUID>, FeatureFlagRepository {

  @Override
  Optional<FeatureFlag> findBySlug(String slug);

  @Override
  boolean existsBySlug(String slug);

  @Override
  List<FeatureFlag> findAll();

}