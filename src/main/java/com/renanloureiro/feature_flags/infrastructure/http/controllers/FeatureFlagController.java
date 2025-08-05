package com.renanloureiro.feature_flags.infrastructure.http.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.renanloureiro.feature_flags.application.dtos.CreateFeatureFlagDTO;
import com.renanloureiro.feature_flags.application.usecases.CreateFeatureFlagUseCase;
import com.renanloureiro.feature_flags.infrastructure.http.presenters.FeatureFlagPresenter;
import com.renanloureiro.feature_flags.infrastructure.http.presenters.featureflag.FeatureFlagResponseDTO;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/v1/feature-flags")
@Tag(name = "Feature Flags", description = "Endpoints para gerenciamento de feature flags")
public class FeatureFlagController implements FeatureFlagControllerSwagger {

  @Autowired
  private CreateFeatureFlagUseCase createFeatureFlagUseCase;

  @PostMapping
  public ResponseEntity<FeatureFlagResponseDTO> createFeatureFlag(
      @Valid @RequestBody CreateFeatureFlagDTO dto) {
    var httpResponse = FeatureFlagPresenter.toHttp(createFeatureFlagUseCase.execute(dto));
    return ResponseEntity.status(201).body(httpResponse);
  }
}
