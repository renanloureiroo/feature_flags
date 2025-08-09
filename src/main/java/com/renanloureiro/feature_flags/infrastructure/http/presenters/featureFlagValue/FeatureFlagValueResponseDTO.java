package com.renanloureiro.feature_flags.infrastructure.http.presenters.featureFlagValue;

import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;
import com.renanloureiro.feature_flags.domain.FeatureFlagType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FeatureFlagValueResponseDTO {
  private UUID id;
  private UUID flagId;
  private int version;
  private FeatureFlagType type;
  private JsonNode value;
  private String valueAsString;
}