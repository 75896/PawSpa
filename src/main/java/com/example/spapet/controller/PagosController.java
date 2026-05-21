package com.example.spapet.controller;

import com.example.spapet.dto.PagosDTO;
import com.example.spapet.service.PagosService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class PagosController {

    private final PagosService pagosService;

    @GetMapping
    public ResponseEntity<List<PagosDTO>> obtenerTodos() {
        return ResponseEntity.ok(
                pagosService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagosDTO> obtenerPorId(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                pagosService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<PagosDTO> crear(
            @RequestBody PagosDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(pagosService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PagosDTO> actualizar(
            @PathVariable UUID id,
            @RequestBody PagosDTO dto) {

        return ResponseEntity.ok(
                pagosService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable UUID id) {

        pagosService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}