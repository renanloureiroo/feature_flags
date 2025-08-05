package com.renanloureiro.feature_flags.infrastructure.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.renanloureiro.feature_flags.application.repositories.FeatureFlagValueRepository;
import com.renanloureiro.feature_flags.domain.FeatureFlag;
import com.renanloureiro.feature_flags.domain.FeatureFlagValue;

@Repository
public interface FeatureFlagValueJpaRepository extends
    JpaRepository<FeatureFlagValue, UUID>,
    FeatureFlagValueRepository {

  Optional<FeatureFlagValue> findByFlagAndVersion(FeatureFlag flag, Integer version);

  boolean existsByFlagAndVersion(FeatureFlag flag, Integer version);
}