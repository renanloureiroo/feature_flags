package com.renanloureiro.feature_flags.application.usecases;

import com.renanloureiro.feature_flags.application.exceptions.FeatureFlagNotFound;
import com.renanloureiro.feature_flags.application.repositories.FeatureFlagRepository;
import com.renanloureiro.feature_flags.domain.FeatureFlag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FetchFeatureFlagBySlug {

  private final FeatureFlagRepository featureFlagRepository;

  public FeatureFlag execute(String slug) {
    return featureFlagRepository.findBySlug(slug)
        .orElseThrow(() -> new FeatureFlagNotFound());
  }
}
