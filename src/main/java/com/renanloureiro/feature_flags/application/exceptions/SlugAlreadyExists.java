package com.renanloureiro.feature_flags.application.exceptions;

public class SlugAlreadyExists extends BaseException {

  public SlugAlreadyExists() {
    super("Already exists a feature flag with this slug, please try another name", 409);
  }
}
