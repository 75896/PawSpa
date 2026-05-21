package com.example.spapet.controller;

import com.example.spapet.dto.Categorias_productoDTO;
import com.example.spapet.service.Categorias_productoService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categorias-producto")
@RequiredArgsConstructor
public class Categorias_productoController {

    private final Categorias_productoService categoriasProductoService;

    @GetMapping
    public ResponseEntity<List<Categorias_productoDTO>> obtenerTodos() {

        return ResponseEntity.ok(
                categoriasProductoService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categorias_productoDTO> obtenerPorId(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                categoriasProductoService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<Categorias_productoDTO> crear(
            @RequestBody Categorias_productoDTO categoriasProductoDTO) {

        Categorias_productoDTO nuevaCategoria = categoriasProductoService.crear(categoriasProductoDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(nuevaCategoria);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Categorias_productoDTO> actualizar(
            @PathVariable UUID id,
            @RequestBody Categorias_productoDTO categoriasProductoDTO) {

        return ResponseEntity.ok(
                categoriasProductoService.actualizar(id, categoriasProductoDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable UUID id) {

        categoriasProductoService.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}