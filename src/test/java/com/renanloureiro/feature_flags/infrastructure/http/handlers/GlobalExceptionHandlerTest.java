package com.renanloureiro.feature_flags.infrastructure.http.handlers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import com.renanloureiro.feature_flags.application.exceptions.AppError;
import com.renanloureiro.feature_flags.application.exceptions.ResourceNotFoundException;
import com.renanloureiro.feature_flags.application.exceptions.SlugAlreadyExists;
import com.renanloureiro.feature_flags.application.exceptions.ValidationException;

class GlobalExceptionHandlerTest {

  private final GlobalExceptionHandler handler = new GlobalExceptionHandler();
  private final WebRequest webRequest = null; // Mock seria usado em um teste real

  @Test
  void shouldHandleSlugAlreadyExists() {
    // Given
    SlugAlreadyExists exception = new SlugAlreadyExists();

    // When
    ResponseEntity<AppError> response = handler.handleSlugAlreadyExists(exception, webRequest);

    // Then
    assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    assertEquals(409, response.getBody().getStatus());
    assertEquals("Already exists a feature flag with this slug, please try another name",
        response.getBody().getMessage());
    assertNotNull(response.getBody().getTimestamp());
  }

  @Test
  void shouldHandleValidationException() {
    // Given
    ValidationException exception = new ValidationException("Nome deve ter pelo menos 3 caracteres");

    // When
    ResponseEntity<AppError> response = handler.handleValidationException(exception, webRequest);

    // Then
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals(400, response.getBody().getStatus());
    assertEquals("Nome deve ter pelo menos 3 caracteres", response.getBody().getMessage());
    assertNotNull(response.getBody().getTimestamp());
  }

  @Test
  void shouldHandleResourceNotFoundException() {
    // Given
    ResourceNotFoundException exception = new ResourceNotFoundException("Feature flag não encontrada");

    // When
    ResponseEntity<AppError> response = handler.handleResourceNotFoundException(exception, webRequest);

    // Then
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals(404, response.getBody().getStatus());
    assertEquals("Feature flag não encontrada", response.getBody().getMessage());
    assertNotNull(response.getBody().getTimestamp());
  }

  @Test
  void shouldHandleGenericException() {
    // Given
    Exception exception = new RuntimeException("Erro genérico");

    // When
    ResponseEntity<AppError> response = handler.handleGenericException(exception, webRequest);

    // Then
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    assertEquals(500, response.getBody().getStatus());
    assertEquals("Erro interno do servidor", response.getBody().getMessage());
    assertNotNull(response.getBody().getTimestamp());
  }
}