package com.renanloureiro.feature_flags.infrastructure.http.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.renanloureiro.feature_flags.application.dtos.CreateFeatureFlagDTO;
import com.renanloureiro.feature_flags.application.usecases.CreateFeatureFlagUseCase;
import com.renanloureiro.feature_flags.application.usecases.FetchFeatureFlagBySlug;
import com.renanloureiro.feature_flags.application.usecases.ListFeatureFlags;
import com.renanloureiro.feature_flags.infrastructure.http.doc.FeatureFlagControllerSwagger;
import com.renanloureiro.feature_flags.infrastructure.http.presenters.FeatureFlagPresenter;
import com.renanloureiro.feature_flags.infrastructure.http.presenters.featureflag.FeatureFlagResponseDTO;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/v1/feature-flags")
@Tag(name = "Feature Flags")
public class FeatureFlagController implements FeatureFlagControllerSwagger {

  @Autowired
  private CreateFeatureFlagUseCase createFeatureFlagUseCase;

  @Autowired
  private FetchFeatureFlagBySlug fetchFeatureFlagBySlugUseCase;

  @Autowired
  private ListFeatureFlags listFeatureFlagsUseCase;

  @PostMapping
  public ResponseEntity<FeatureFlagResponseDTO> createFeatureFlag(
      @Valid @RequestBody CreateFeatureFlagDTO dto) {
    var httpResponse = FeatureFlagPresenter.toHttp(createFeatureFlagUseCase.execute(dto));
    return ResponseEntity.status(201).body(httpResponse);
  }

  @GetMapping("/{slug}")
  public ResponseEntity<FeatureFlagResponseDTO> getFeatureFlagBySlug(@PathVariable String slug) {
    var httpResponse = FeatureFlagPresenter.toHttp(fetchFeatureFlagBySlugUseCase.execute(slug));
    return ResponseEntity.ok(httpResponse);
  }

  @GetMapping
  public ResponseEntity<List<FeatureFlagResponseDTO>> listFeatureFlags() {
    var httpResponse = listFeatureFlagsUseCase.execute().stream()
        .map(FeatureFlagPresenter::toHttp)
        .collect(Collectors.toList());
    return ResponseEntity.ok(httpResponse);
  }
}
