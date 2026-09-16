package com.lucianodev.apiecommerce.service;

import com.lucianodev.apiecommerce.dto.request.CategoriaCreateDto;
import com.lucianodev.apiecommerce.dto.request.CategoriaUpdateDto;
import com.lucianodev.apiecommerce.dto.response.CategoriaResponseDto;
import com.lucianodev.apiecommerce.entity.Categoria;
import com.lucianodev.apiecommerce.exception.CategoriaNaoEncontradaException;
import com.lucianodev.apiecommerce.exception.ConflictException;
import com.lucianodev.apiecommerce.exception.DataBaseException;
import com.lucianodev.apiecommerce.mapper.CategoriaMapper;
import com.lucianodev.apiecommerce.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository repository;
    private final CategoriaMapper mapper;

    @Transactional
    public CategoriaResponseDto create(CategoriaCreateDto dto) {
        if (repository.existsByNomeIgnoreCase(dto.nome())) {
            throw new ConflictException("Já existe uma categoria com esse nome.");
        }

        Categoria cat = mapper.toEntity(dto);
        return mapper.toResponse(repository.save(cat));
    }

    @Transactional
    public CategoriaResponseDto update(UUID idCateg, CategoriaUpdateDto dto) {
        Categoria categoria = repository.findById(idCateg)
                .orElseThrow(() -> new CategoriaNaoEncontradaException(idCateg.toString()));

        if (dto.nome() != null && repository.existsByNomeIgnoreCaseAndIdNot(dto.nome(), idCateg)) {
            throw new ConflictException("Já existe uma categoria com esse nome.");
        }

        mapper.update(dto, categoria);

        Categoria atualizada = repository.save(categoria);
        return mapper.toResponse(atualizada);
    }

    @Transactional(readOnly = true)
    public List<CategoriaResponseDto> findAll() {
        List<Categoria> categorias = repository.findAll();
        return categorias.stream().map(mapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<CategoriaResponseDto> findByNomeContainingIgnoreCase(String nome) {
        if (nome == null || nome.isBlank()) {
            return List.of();
        }
        List<Categoria> list = repository.findByNomeContainingIgnoreCase(nome);
        return list
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CategoriaResponseDto> findAllByOrderById() {
        List<Categoria> list = repository.findAllByOrderById();
        return list
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoriaResponseDto findById(UUID idCateg) {
        Categoria cat = repository.findById(idCateg)
                .orElseThrow(() -> new CategoriaNaoEncontradaException(idCateg.toString()));

        return mapper.toResponse(cat);
    }

    @Transactional
    public void deleteById(UUID idCateg) {
        if (!repository.existsById(idCateg)) {
            throw new CategoriaNaoEncontradaException(idCateg.toString());
        }
        try {
            repository.deleteById(idCateg);
        } catch (DataIntegrityViolationException e) {
            throw new DataBaseException("Falha de integridade referencial");
        }
    }

    @Transactional
    public void alterarStatus(UUID id, boolean ativo) {
        Categoria cat = repository.findById(id)
                .orElseThrow(() -> new CategoriaNaoEncontradaException(id.toString()));

        cat.setAtivo(ativo);
    }
}
