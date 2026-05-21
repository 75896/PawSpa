package com.example.spapet.controller;

import com.example.spapet.dto.EncuestasnpsDTO;
import com.example.spapet.service.EncuestasnpsService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/encuestas-nps")
@RequiredArgsConstructor
public class EncuestasnpsController {

    private final EncuestasnpsService encuestasnpsService;

    @GetMapping
    public ResponseEntity<List<EncuestasnpsDTO>> obtenerTodos() {

        return ResponseEntity.ok(
                encuestasnpsService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EncuestasnpsDTO> obtenerPorId(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                encuestasnpsService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<EncuestasnpsDTO> crear(
            @RequestBody EncuestasnpsDTO dto) {

        EncuestasnpsDTO nuevaEncuesta = encuestasnpsService.crear(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(nuevaEncuesta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EncuestasnpsDTO> actualizar(
            @PathVariable UUID id,
            @RequestBody EncuestasnpsDTO dto) {

        return ResponseEntity.ok(
                encuestasnpsService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable UUID id) {

        encuestasnpsService.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}