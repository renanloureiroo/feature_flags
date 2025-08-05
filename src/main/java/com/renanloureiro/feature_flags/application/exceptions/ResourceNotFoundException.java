package com.renanloureiro.feature_flags.application.exceptions;

public class ResourceNotFoundException extends BaseException {

  public ResourceNotFoundException(String message) {
    super(message, 404);
  }
}