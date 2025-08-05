package com.renanloureiro.feature_flags.application.exceptions;

public class ValidationException extends BaseException {

  public ValidationException(String message) {
    super(message, 400);
  }
}