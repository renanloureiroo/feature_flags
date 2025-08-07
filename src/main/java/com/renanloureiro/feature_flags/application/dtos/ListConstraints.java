package com.renanloureiro.feature_flags.application.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Constraints para valores de lista")
public class ListConstraints {

    @NotNull(message = "Item type is required")
    @Schema(description = "Tipo dos itens da lista", example = "string", allowableValues = { "string", "number",
            "boolean" })
    private String itemType;

    @Schema(description = "Número mínimo de itens", example = "1")
    private Integer minItems;

    @Schema(description = "Número máximo de itens", example = "10")
    private Integer maxItems;

    @Schema(description = "Se os itens devem ser únicos", example = "true")
    private Boolean uniqueItems;
}