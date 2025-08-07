package com.renanloureiro.feature_flags.application.dtos;

import com.fasterxml.jackson.databind.JsonNode;
import com.renanloureiro.feature_flags.domain.FeatureFlagType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@Schema(description = "DTO para criação de uma nova feature flag")
public class CreateFeatureFlagDTO {

  @NotBlank(message = "Name is required")
  @Schema(description = "Nome da feature flag", example = "new-user-interface")
  private String name;

  @NotBlank(message = "Description is required")
  @Schema(description = "Descrição da feature flag", example = "Habilita a nova interface do usuário")
  private String description;

  @NotNull(message = "Type is required")
  @Schema(description = "Tipo da feature flag", example = "BOOLEAN")
  private FeatureFlagType type;

  @Schema(description = "Constraints para valores numéricos")
  private NumberConstraints numberConstraints;

  @Schema(description = "Constraints para valores de string")
  private StringConstraints stringConstraints;

  @Schema(description = "Constraints para valores de lista")
  private ListConstraints listConstraints;

  @Schema(description = "Schema JSON que define a estrutura dos valores da feature flag (DEPRECATED - use constraints específicos)", deprecated = true)
  private JsonNode schema;

}
