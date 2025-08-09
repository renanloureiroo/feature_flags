package com.renanloureiro.feature_flags.application.exceptions;

public class FeatureFlagNotFound extends BaseException {

  public FeatureFlagNotFound() {
    super("Feature flag not found", 400);
  }

}
