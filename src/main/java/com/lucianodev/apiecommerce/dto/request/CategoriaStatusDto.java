package com.lucianodev.apiecommerce.dto.request;

import jakarta.validation.constraints.NotNull;

public record CategoriaStatusDto(
        @NotNull(message = "O campo 'ativo' é obrigatório.")
        Boolean ativo
) {
}
