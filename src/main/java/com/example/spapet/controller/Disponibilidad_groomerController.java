package com.example.spapet.controller;

import com.example.spapet.dto.Disponibilidad_groomerDTO;
import com.example.spapet.service.Disponibilidad_groomerService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/disponibilidad-groomer")
@RequiredArgsConstructor
public class Disponibilidad_groomerController {

    private final Disponibilidad_groomerService disponibilidadGroomerService;

    @GetMapping
    public ResponseEntity<List<Disponibilidad_groomerDTO>> obtenerTodos() {

        return ResponseEntity.ok(
                disponibilidadGroomerService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Disponibilidad_groomerDTO> obtenerPorId(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                disponibilidadGroomerService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<Disponibilidad_groomerDTO> crear(
            @RequestBody Disponibilidad_groomerDTO dto) {

        Disponibilidad_groomerDTO nuevaDisponibilidad = disponibilidadGroomerService.crear(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(nuevaDisponibilidad);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Disponibilidad_groomerDTO> actualizar(
            @PathVariable UUID id,
            @RequestBody Disponibilidad_groomerDTO dto) {

        return ResponseEntity.ok(
                disponibilidadGroomerService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable UUID id) {

        disponibilidadGroomerService.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}