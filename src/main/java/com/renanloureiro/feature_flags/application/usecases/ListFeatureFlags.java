package com.renanloureiro.feature_flags.application.usecases;

import java.util.List;

import org.springframework.stereotype.Service;

import com.renanloureiro.feature_flags.application.repositories.FeatureFlagRepository;
import com.renanloureiro.feature_flags.domain.FeatureFlag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ListFeatureFlags {

  private final FeatureFlagRepository featureFlagRepository;

  public List<FeatureFlag> execute() {
    return featureFlagRepository.findAll();
  }

}
