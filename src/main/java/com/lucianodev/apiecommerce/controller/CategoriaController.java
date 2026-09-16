package com.lucianodev.apiecommerce.controller;

import com.lucianodev.apiecommerce.dto.request.CategoriaCreateDto;
import com.lucianodev.apiecommerce.dto.request.CategoriaStatusDto;
import com.lucianodev.apiecommerce.dto.request.CategoriaUpdateDto;
import com.lucianodev.apiecommerce.dto.response.CategoriaResponseDto;
import com.lucianodev.apiecommerce.service.CategoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService service;


    @PostMapping
    public ResponseEntity<CategoriaResponseDto> save(@RequestBody @Valid CategoriaCreateDto dto) {
        CategoriaResponseDto response = service.create(dto);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponseDto> update(@PathVariable UUID id, @RequestBody @Valid CategoriaUpdateDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @GetMapping
    public ResponseEntity<List<CategoriaResponseDto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/search")
    public ResponseEntity<List<CategoriaResponseDto>> buscaPorPalavra(@RequestParam(name = "nome") String palavra) {
        return ResponseEntity.ok(service.findByNomeContainingIgnoreCase(palavra));
    }

    @GetMapping("/byid")
    public ResponseEntity<List<CategoriaResponseDto>> ordenarTodasPeloId() {
        return ResponseEntity.ok(service.findAllByOrderById());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDto> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable UUID id) {
        service.deleteById(id);
    }

    @PatchMapping("/{id}/status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void alterarStatus(@PathVariable UUID id, @RequestBody @Valid CategoriaStatusDto dto){
        service.alterarStatus(id, dto.ativo());
    }
}
