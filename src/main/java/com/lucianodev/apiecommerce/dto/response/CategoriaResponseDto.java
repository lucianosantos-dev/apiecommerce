package com.lucianodev.apiecommerce.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record CategoriaResponseDto(
        UUID id,
        String nome,
        Boolean ativo,
        LocalDateTime criadoEm
) {
}
