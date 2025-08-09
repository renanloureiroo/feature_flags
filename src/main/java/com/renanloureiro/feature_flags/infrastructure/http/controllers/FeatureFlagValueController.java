package com.renanloureiro.feature_flags.infrastructure.http.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.renanloureiro.feature_flags.application.dtos.CreateFeatureFlagValueDTO;
import com.renanloureiro.feature_flags.application.usecases.CreateFeatureFlagValueUseCase;
import com.renanloureiro.feature_flags.infrastructure.http.doc.FeatureFlagValueControllerSwagger;
import com.renanloureiro.feature_flags.infrastructure.http.presenters.FeatureFlagValuePresenter;
import com.renanloureiro.feature_flags.infrastructure.http.presenters.featureFlagValue.FeatureFlagValueResponseDTO;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/v1/feature-flags/{flagId}/values")
@Tag(name = "Feature Flag Values")
public class FeatureFlagValueController implements FeatureFlagValueControllerSwagger {

  @Autowired
  private CreateFeatureFlagValueUseCase createFeatureFlagValueUseCase;

  @PostMapping
  public ResponseEntity<FeatureFlagValueResponseDTO> createFeatureFlagValue(
      @PathVariable UUID flagId, @RequestBody CreateFeatureFlagValueDTO dto) {
    var featureFlagValue = createFeatureFlagValueUseCase.execute(flagId, dto);
    return ResponseEntity.status(201).body(FeatureFlagValuePresenter.toHttp(featureFlagValue));
  }

}
