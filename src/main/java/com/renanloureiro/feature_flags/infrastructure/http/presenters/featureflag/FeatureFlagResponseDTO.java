package com.renanloureiro.feature_flags.infrastructure.http.presenters.featureflag;

import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;
import com.renanloureiro.feature_flags.domain.FeatureFlagType;

import lombok.Builder;
import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@Schema(description = "DTO de resposta para feature flag")
public class FeatureFlagResponseDTO {
  @Schema(description = "ID único da feature flag", example = "123e4567-e89b-12d3-a456-426614174000")
  private UUID id;

  @Schema(description = "Nome da feature flag", example = "new-user-interface")
  private String name;

  @Schema(description = "Slug único da feature flag", example = "new-user-interface")
  private String slug;

  @Schema(description = "Tipo da feature flag", example = "BOOLEAN")
  private FeatureFlagType type;

  @Schema(description = "Schema JSON da feature flag")
  private JsonNode schema;

  @Schema(description = "Descrição da feature flag", example = "Habilita a nova interface do usuário")
  private String description;
}
