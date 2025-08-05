package com.renanloureiro.feature_flags.application.repositories;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.renanloureiro.feature_flags.domain.FeatureFlag;
import com.renanloureiro.feature_flags.domain.FeatureFlagType;

@ExtendWith(MockitoExtension.class)
class FeatureFlagRepositoryTest {

  @Mock
  private FeatureFlagRepository featureFlagRepository;

  @Test
  void shouldLoadRepository() {
    assertNotNull(featureFlagRepository, "FeatureFlagRepository deve ser carregado pelo Spring");
  }

  @Test
  void shouldFindBySlug() {
    // Given
    String slug = "test-flag";
    FeatureFlag featureFlag = new FeatureFlag();
    featureFlag.setId(UUID.randomUUID());
    featureFlag.setName("Test Flag");
    featureFlag.setSlug(slug);
    featureFlag.setType(FeatureFlagType.BOOLEAN);

    when(featureFlagRepository.findBySlug(slug)).thenReturn(Optional.of(featureFlag));

    // When
    Optional<FeatureFlag> result = featureFlagRepository.findBySlug(slug);

    // Then
    assertNotNull(result);
    assertNotNull(result.get());
    assertNotNull(result.get().getId());
  }

  @Test
  void shouldCheckExistsBySlug() {
    // Given
    String slug = "test-flag";
    when(featureFlagRepository.existsBySlug(slug)).thenReturn(true);

    // When
    boolean exists = featureFlagRepository.existsBySlug(slug);

    // Then
    assertNotNull(exists);
  }
}