package com.renanloureiro.feature_flags.application.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Constraints para valores numéricos")
public class NumberConstraints {

  @Schema(description = "Valor mínimo permitido", example = "1.0")
  private Double minimum;

  @Schema(description = "Valor máximo permitido", example = "10.0")
  private Double maximum;

  @JsonProperty("exclusiveMinimum")
  @Schema(description = "Se o valor mínimo é exclusivo", example = "false")
  private Boolean exclusiveMinimum;

  @JsonProperty("exclusiveMaximum")
  @Schema(description = "Se o valor máximo é exclusivo", example = "false")
  private Boolean exclusiveMaximum;
}