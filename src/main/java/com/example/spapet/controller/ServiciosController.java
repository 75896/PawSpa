package com.example.spapet.controller;

import com.example.spapet.dto.ServiciosDTO;
import com.example.spapet.service.ServiciosService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/servicios")
@RequiredArgsConstructor
public class ServiciosController {

    private final ServiciosService serviciosService;

    @GetMapping
    public ResponseEntity<List<ServiciosDTO>> obtenerTodos() {
        return ResponseEntity.ok(
                serviciosService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiciosDTO> obtenerPorId(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                serviciosService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<ServiciosDTO> crear(
            @RequestBody ServiciosDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(serviciosService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiciosDTO> actualizar(
            @PathVariable UUID id,
            @RequestBody ServiciosDTO dto) {

        return ResponseEntity.ok(
                serviciosService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable UUID id) {

        serviciosService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}