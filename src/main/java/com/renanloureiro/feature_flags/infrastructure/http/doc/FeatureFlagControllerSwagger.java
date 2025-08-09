package com.renanloureiro.feature_flags.infrastructure.http.doc;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.renanloureiro.feature_flags.application.dtos.CreateFeatureFlagDTO;
import com.renanloureiro.feature_flags.application.exceptions.AppError;
import com.renanloureiro.feature_flags.infrastructure.http.presenters.featureflag.FeatureFlagResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@Tag(name = "Feature Flags", description = "Endpoints para gerenciamento de feature flags")
public interface FeatureFlagControllerSwagger {

        @Operation(summary = "Create a new feature flag", deprecated = true)
        @ApiResponse(responseCode = "201", description = "Feature flag created successfully", content = @Content(schema = @Schema(implementation = FeatureFlagResponseDTO.class), mediaType = MediaType.APPLICATION_JSON_VALUE))
        @ApiResponse(responseCode = "400", description = "Invalid request body", content = @Content(schema = @Schema(implementation = AppError.class), mediaType = MediaType.APPLICATION_JSON_VALUE))
        @ApiResponse(responseCode = "409", description = "Feature flag with the same slug already exists", content = @Content(schema = @Schema(implementation = AppError.class), mediaType = MediaType.APPLICATION_JSON_VALUE))
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = AppError.class), mediaType = MediaType.APPLICATION_JSON_VALUE))
        @PostMapping("/v1/feature-flags")
        public ResponseEntity<FeatureFlagResponseDTO> createFeatureFlag(
                        @Parameter(description = "Dados da feature flag a ser criada", required = true) @RequestBody CreateFeatureFlagDTO dto);

        @Operation(summary = "List all feature flags")
        @ApiResponse(responseCode = "200", description = "Feature flags listed successfully", content = @Content(schema = @Schema(implementation = FeatureFlagResponseDTO.class), mediaType = MediaType.APPLICATION_JSON_VALUE))
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = AppError.class), mediaType = MediaType.APPLICATION_JSON_VALUE))
        @GetMapping("/v1/feature-flags")
        public ResponseEntity<List<FeatureFlagResponseDTO>> listFeatureFlags();

        @Operation(summary = "Get a feature flag by slug")
        @ApiResponse(responseCode = "200", description = "Feature flag fetched successfully", content = @Content(schema = @Schema(implementation = FeatureFlagResponseDTO.class), mediaType = MediaType.APPLICATION_JSON_VALUE))
        @ApiResponse(responseCode = "400", description = "Invalid slug", content = @Content(schema = @Schema(implementation = AppError.class), mediaType = MediaType.APPLICATION_JSON_VALUE))
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = AppError.class), mediaType = MediaType.APPLICATION_JSON_VALUE))
        @GetMapping("/v1/feature-flags/{slug}")
        public ResponseEntity<FeatureFlagResponseDTO> getFeatureFlagBySlug(
                        @Parameter(description = "Slug da feature flag a ser buscada", required = true) @PathVariable String slug);
}
