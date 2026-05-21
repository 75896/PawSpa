package com.example.spapet.controller;

import com.example.spapet.dto.Fichas_groomingDTO;
import com.example.spapet.service.Fichas_groomingService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/fichas-grooming")
@RequiredArgsConstructor
public class Fichas_groomingController {

    private final Fichas_groomingService fichasGroomingService;

    @GetMapping
    public ResponseEntity<List<Fichas_groomingDTO>> obtenerTodos() {

        return ResponseEntity.ok(
                fichasGroomingService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Fichas_groomingDTO> obtenerPorId(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                fichasGroomingService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<Fichas_groomingDTO> crear(
            @RequestBody Fichas_groomingDTO dto) {

        Fichas_groomingDTO nuevaFicha = fichasGroomingService.crear(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(nuevaFicha);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Fichas_groomingDTO> actualizar(
            @PathVariable UUID id,
            @RequestBody Fichas_groomingDTO dto) {

        return ResponseEntity.ok(
                fichasGroomingService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable UUID id) {

        fichasGroomingService.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}