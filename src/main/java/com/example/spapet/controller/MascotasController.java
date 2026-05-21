package com.example.spapet.controller;

import com.example.spapet.dto.MascotasDTO;
import com.example.spapet.service.MascotasService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/mascotas")
@RequiredArgsConstructor
public class MascotasController {

    private final MascotasService mascotasService;

    @GetMapping
    public ResponseEntity<List<MascotasDTO>> obtenerTodos() {
        return ResponseEntity.ok(
                mascotasService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MascotasDTO> obtenerPorId(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                mascotasService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<MascotasDTO> crear(
            @RequestBody MascotasDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mascotasService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MascotasDTO> actualizar(
            @PathVariable UUID id,
            @RequestBody MascotasDTO dto) {

        return ResponseEntity.ok(
                mascotasService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable UUID id) {

        mascotasService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}