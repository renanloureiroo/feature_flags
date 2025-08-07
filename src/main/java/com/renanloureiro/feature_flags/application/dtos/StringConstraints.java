package com.renanloureiro.feature_flags.application.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Constraints para valores de string")
public class StringConstraints {

    @Schema(description = "Lista de valores permitidos", example = "[\"v1\", \"v2\", \"v3\"]")
    private List<String> enumValues;

    @Schema(description = "Padrão regex para validação", example = "^[a-zA-Z0-9]+$")
    private String pattern;

    @Schema(description = "Comprimento mínimo da string", example = "1")
    private Integer minLength;

    @Schema(description = "Comprimento máximo da string", example = "100")
    private Integer maxLength;
} 