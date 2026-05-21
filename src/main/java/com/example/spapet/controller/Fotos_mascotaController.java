package com.example.spapet.controller;

import com.example.spapet.dto.Fotos_mascotaDTO;
import com.example.spapet.service.Fotos_mascotaService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/fotos-mascota")
@RequiredArgsConstructor
public class Fotos_mascotaController {

    private final Fotos_mascotaService fotosMascotaService;

    @GetMapping
    public ResponseEntity<List<Fotos_mascotaDTO>> obtenerTodos() {
        return ResponseEntity.ok(
                fotosMascotaService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Fotos_mascotaDTO> obtenerPorId(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                fotosMascotaService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<Fotos_mascotaDTO> crear(
            @RequestBody Fotos_mascotaDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(fotosMascotaService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Fotos_mascotaDTO> actualizar(
            @PathVariable UUID id,
            @RequestBody Fotos_mascotaDTO dto) {

        return ResponseEntity.ok(
                fotosMascotaService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable UUID id) {

        fotosMascotaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}