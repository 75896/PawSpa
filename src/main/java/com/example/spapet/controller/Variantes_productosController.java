package com.example.spapet.controller;

import com.example.spapet.dto.Variantes_productosDTO;
import com.example.spapet.service.Variantes_productosService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/variantes-productos")
@RequiredArgsConstructor
public class Variantes_productosController {

    private final Variantes_productosService variantesProductosService;

    @GetMapping
    public ResponseEntity<List<Variantes_productosDTO>> obtenerTodos() {
        return ResponseEntity.ok(
                variantesProductosService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Variantes_productosDTO> obtenerPorId(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                variantesProductosService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<Variantes_productosDTO> crear(
            @RequestBody Variantes_productosDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(variantesProductosService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Variantes_productosDTO> actualizar(
            @PathVariable UUID id,
            @RequestBody Variantes_productosDTO dto) {

        return ResponseEntity.ok(
                variantesProductosService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable UUID id) {

        variantesProductosService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}