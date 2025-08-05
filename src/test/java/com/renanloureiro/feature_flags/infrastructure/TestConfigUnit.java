package com.renanloureiro.feature_flags.infrastructure;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import com.renanloureiro.feature_flags.application.repositories.FeatureFlagRepository;
import com.renanloureiro.feature_flags.application.repositories.FeatureFlagValueRepository;
import com.renanloureiro.feature_flags.infrastructure.repositories.FeatureFlagRepositoryInMemoryImpl;
import com.renanloureiro.feature_flags.infrastructure.repositories.FeatureFlagValueRepositoryInMemoryImpl;

@TestConfiguration
public class TestConfigUnit {

  @Bean
  @Primary
  public FeatureFlagRepository featureFlagRepository() {
    return new FeatureFlagRepositoryInMemoryImpl();
  }

  @Bean
  @Primary
  public FeatureFlagValueRepository featureFlagValueRepository() {
    return new FeatureFlagValueRepositoryInMemoryImpl();
  }
}