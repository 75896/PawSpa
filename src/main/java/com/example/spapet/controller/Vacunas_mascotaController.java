package com.example.spapet.controller;

import com.example.spapet.dto.Vacunas_mascotaDTO;
import com.example.spapet.service.Vacunas_mascotaService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/vacunas-mascota")
@RequiredArgsConstructor
public class Vacunas_mascotaController {

    private final Vacunas_mascotaService vacunasMascotaService;

    @GetMapping
    public ResponseEntity<List<Vacunas_mascotaDTO>> obtenerTodos() {
        return ResponseEntity.ok(
                vacunasMascotaService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vacunas_mascotaDTO> obtenerPorId(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                vacunasMascotaService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<Vacunas_mascotaDTO> crear(
            @RequestBody Vacunas_mascotaDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(vacunasMascotaService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vacunas_mascotaDTO> actualizar(
            @PathVariable UUID id,
            @RequestBody Vacunas_mascotaDTO dto) {

        return ResponseEntity.ok(
                vacunasMascotaService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable UUID id) {

        vacunasMascotaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}