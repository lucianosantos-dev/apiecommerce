package com.lucianodev.apiecommerce.mapper;

import com.lucianodev.apiecommerce.dto.request.CategoriaCreateDto;
import com.lucianodev.apiecommerce.dto.request.CategoriaUpdateDto;
import com.lucianodev.apiecommerce.dto.response.CategoriaResponseDto;
import com.lucianodev.apiecommerce.entity.Categoria;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CategoriaMapper {

    Categoria toEntity(CategoriaCreateDto dto);
    CategoriaResponseDto toResponse(Categoria entity);

    void update(CategoriaUpdateDto dto, @MappingTarget Categoria entity);
}
