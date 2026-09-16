package com.lucianodev.apiecommerce.exception;

public class CategoriaNaoEncontradaException extends RuntimeException {
    public CategoriaNaoEncontradaException(String message) {
        super("Categoria não encontrada com id: " + message);
    }
}
