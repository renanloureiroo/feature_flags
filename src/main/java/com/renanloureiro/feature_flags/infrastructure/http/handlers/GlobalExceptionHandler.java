package com.renanloureiro.feature_flags.infrastructure.http.handlers;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.renanloureiro.feature_flags.application.exceptions.AppError;
import com.renanloureiro.feature_flags.application.exceptions.BaseException;
import com.renanloureiro.feature_flags.application.exceptions.ResourceNotFoundException;
import com.renanloureiro.feature_flags.application.exceptions.SlugAlreadyExists;
import com.renanloureiro.feature_flags.application.exceptions.ValidationException;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(BaseException.class)
  public ResponseEntity<AppError> handleBaseException(BaseException ex, WebRequest request) {
    log.error("Erro customizado capturado: {}", ex.getMessage(), ex);

    AppError error = AppError.builder()
        .message(ex.getMessage())
        .timestamp(ex.getTimestamp())
        .status(ex.getStatus())
        .build();

    return ResponseEntity.status(ex.getStatus()).body(error);
  }

  @ExceptionHandler(SlugAlreadyExists.class)
  public ResponseEntity<AppError> handleSlugAlreadyExists(SlugAlreadyExists ex, WebRequest request) {
    log.error("Slug já existe: {}", ex.getMessage(), ex);

    AppError error = AppError.builder()
        .message(ex.getMessage())
        .timestamp(ex.getTimestamp())
        .status(ex.getStatus())
        .build();

    return ResponseEntity.status(ex.getStatus()).body(error);
  }

  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<AppError> handleValidationException(ValidationException ex, WebRequest request) {
    log.error("Erro de validação: {}", ex.getMessage(), ex);

    AppError error = AppError.builder()
        .message(ex.getMessage())
        .timestamp(ex.getTimestamp())
        .status(ex.getStatus())
        .build();

    return ResponseEntity.status(ex.getStatus()).body(error);
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<AppError> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
    log.error("Recurso não encontrado: {}", ex.getMessage(), ex);

    AppError error = AppError.builder()
        .message(ex.getMessage())
        .timestamp(ex.getTimestamp())
        .status(ex.getStatus())
        .build();

    return ResponseEntity.status(ex.getStatus()).body(error);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<AppError> handleValidationErrors(MethodArgumentNotValidException ex, WebRequest request) {
    log.error("Erro de validação de argumentos: {}", ex.getMessage(), ex);

    String errorMessage = ex.getBindingResult().getFieldErrors().stream()
        .map(error -> error.getField() + ": " + error.getDefaultMessage())
        .findFirst()
        .orElse("Erro de validação");

    AppError error = AppError.builder()
        .message(errorMessage)
        .timestamp(LocalDateTime.now())
        .status(400)
        .build();

    return ResponseEntity.badRequest().body(error);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<AppError> handleGenericException(Exception ex, WebRequest request) {
    log.error("Erro não tratado capturado: {}", ex.getMessage(), ex);

    AppError error = AppError.builder()
        .message("Erro interno do servidor")
        .timestamp(LocalDateTime.now())
        .status(500)
        .build();

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
  }
}