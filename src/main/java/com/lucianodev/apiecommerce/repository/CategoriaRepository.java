package com.lucianodev.apiecommerce.repository;

import com.lucianodev.apiecommerce.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, UUID> {
    boolean existsByNomeIgnoreCase(String nome);
    boolean existsByNomeIgnoreCaseAndIdNot(String nome, UUID id);
    List<Categoria> findByNomeContainingIgnoreCase(String nome);
    List<Categoria> findAllByOrderById();
}
