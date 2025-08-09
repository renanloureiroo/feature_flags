package com.renanloureiro.feature_flags.application.dtos;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@Schema(description = "DTO para criação de um novo valor de feature flag")
public class CreateFeatureFlagValueDTO {

  @NotNull(message = "Value is required")
  @JsonRawValue
  @Schema(description = "Valor da feature flag. Deve ser compatível com o tipo da feature flag", examples = {
      "true",
      "42",
      "\"hello\"",
      "[\"item1\", \"item2\"]"
  })
  private JsonNode value;

}
