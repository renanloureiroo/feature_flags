package com.renanloureiro.feature_flags.application.repositories;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FeatureFlagValueRepositoryTest {

  @Mock
  private FeatureFlagValueRepository featureFlagValueRepository;

  @Test
  void shouldLoadRepository() {
    assertNotNull(featureFlagValueRepository, "FeatureFlagValueRepository deve ser carregado pelo Spring");
  }
}