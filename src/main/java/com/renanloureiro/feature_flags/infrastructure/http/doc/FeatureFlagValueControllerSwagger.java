package com.renanloureiro.feature_flags.infrastructure.http.doc;

import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.renanloureiro.feature_flags.application.dtos.CreateFeatureFlagValueDTO;
import com.renanloureiro.feature_flags.application.exceptions.AppError;
import com.renanloureiro.feature_flags.infrastructure.http.presenters.featureFlagValue.FeatureFlagValueResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Feature Flag Values", description = "Endpoints para gerenciar valores de feature flags")
public interface FeatureFlagValueControllerSwagger {

  @Operation(summary = "Cria um novo valor para uma feature flag")
  @ApiResponse(responseCode = "201", description = "Valor da feature flag criado com sucesso", content = @Content(schema = @Schema(implementation = FeatureFlagValueResponseDTO.class), mediaType = MediaType.APPLICATION_JSON_VALUE))
  @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content(schema = @Schema(implementation = AppError.class), mediaType = MediaType.APPLICATION_JSON_VALUE))
  @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(schema = @Schema(implementation = AppError.class), mediaType = MediaType.APPLICATION_JSON_VALUE))
  @PostMapping("/v1/feature-flags/{flagId}/values")
  public ResponseEntity<FeatureFlagValueResponseDTO> createFeatureFlagValue(
      @PathVariable UUID flagId,
      @RequestBody CreateFeatureFlagValueDTO dto);
}
