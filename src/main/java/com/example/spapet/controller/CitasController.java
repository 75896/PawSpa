package com.example.spapet.controller;

import com.example.spapet.dto.CitasDTO;
import com.example.spapet.service.CitasService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/citas")
@RequiredArgsConstructor
public class CitasController {

    private final CitasService citasService;

    @GetMapping
    public ResponseEntity<List<CitasDTO>> obtenerTodos() {

        return ResponseEntity.ok(
                citasService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CitasDTO> obtenerPorId(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                citasService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<CitasDTO> crear(
            @RequestBody CitasDTO citasDTO) {

        CitasDTO nuevaCita = citasService.crear(citasDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(nuevaCita);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CitasDTO> actualizar(
            @PathVariable UUID id,
            @RequestBody CitasDTO citasDTO) {

        return ResponseEntity.ok(
                citasService.actualizar(id, citasDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable UUID id) {

        citasService.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}