package com.renanloureiro.feature_flags.application.exceptions;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@Schema(description = "Modelo de erro da API")
public class AppError {
  @Schema(description = "Mensagem de erro", example = "Feature flag with slug 'new-user-interface' already exists")
  private String message;

  @Schema(description = "Timestamp do erro", example = "2024-01-15T10:30:00")
  private LocalDateTime timestamp;

  @Schema(description = "Código de status HTTP", example = "409")
  private int status;
}
