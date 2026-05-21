package com.example.spapet.controller;

import com.example.spapet.dto.Fichas_checklistDTO;
import com.example.spapet.service.Fichas_checklistService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/fichas-checklist")
@RequiredArgsConstructor
public class Fichas_checklistController {

    private final Fichas_checklistService fichasChecklistService;

    @GetMapping
    public ResponseEntity<List<Fichas_checklistDTO>> obtenerTodos() {

        return ResponseEntity.ok(
                fichasChecklistService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Fichas_checklistDTO> obtenerPorId(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                fichasChecklistService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<Fichas_checklistDTO> crear(
            @RequestBody Fichas_checklistDTO dto) {

        Fichas_checklistDTO nuevaFicha = fichasChecklistService.crear(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(nuevaFicha);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Fichas_checklistDTO> actualizar(
            @PathVariable UUID id,
            @RequestBody Fichas_checklistDTO dto) {

        return ResponseEntity.ok(
                fichasChecklistService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable UUID id) {

        fichasChecklistService.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}