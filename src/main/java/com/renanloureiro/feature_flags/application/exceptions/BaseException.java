package com.renanloureiro.feature_flags.application.exceptions;

import java.time.LocalDateTime;

public abstract class BaseException extends RuntimeException {

  private final LocalDateTime timestamp;
  private final int status;

  protected BaseException(String message, int status) {
    super(message);
    this.timestamp = LocalDateTime.now();
    this.status = status;
  }

  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public int getStatus() {
    return status;
  }
}