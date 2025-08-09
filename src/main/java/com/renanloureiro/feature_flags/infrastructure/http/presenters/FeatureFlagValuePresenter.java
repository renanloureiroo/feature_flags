package com.renanloureiro.feature_flags.infrastructure.http.presenters;

import com.renanloureiro.feature_flags.domain.FeatureFlagValue;
import com.renanloureiro.feature_flags.infrastructure.http.presenters.featureFlagValue.FeatureFlagValueResponseDTO;

public class FeatureFlagValuePresenter {

  public static FeatureFlagValueResponseDTO toHttp(FeatureFlagValue featureFlagValue) {
    return FeatureFlagValueResponseDTO.builder()
        .id(featureFlagValue.getId())
        .flagId(featureFlagValue.getFlag().getId())
        .version(featureFlagValue.getVersion())
        .type(featureFlagValue.getFlag().getType())
        .value(featureFlagValue.getValue())
        .valueAsString(featureFlagValue.getValue().toString())
        .build();
  }
}
