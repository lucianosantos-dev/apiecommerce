package com.lucianodev.apiecommerce.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CategoriaUpdateDto(
        @NotBlank(message = "O nome da categoria é obrigatório.")
        String nome
) {
}
