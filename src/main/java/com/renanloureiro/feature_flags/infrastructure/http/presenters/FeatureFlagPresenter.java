package com.renanloureiro.feature_flags.infrastructure.http.presenters;

import com.renanloureiro.feature_flags.domain.FeatureFlag;
import com.renanloureiro.feature_flags.infrastructure.http.presenters.featureflag.FeatureFlagResponseDTO;

public class FeatureFlagPresenter {

  public static FeatureFlagResponseDTO toHttp(FeatureFlag featureFlag) {
    return FeatureFlagResponseDTO.builder()
        .id(featureFlag.getId())
        .name(featureFlag.getName())
        .slug(featureFlag.getSlug())
        .type(featureFlag.getType())
        .schema(featureFlag.getSchema())
        .description(featureFlag.getDescription())
        .build();
  }

}
