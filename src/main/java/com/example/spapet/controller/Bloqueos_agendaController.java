package com.example.spapet.controller;

import com.example.spapet.dto.Bloqueos_agendaDTO;
import com.example.spapet.service.Bloqueos_agendaService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/bloqueos-agenda")
@RequiredArgsConstructor
public class Bloqueos_agendaController {

    private final Bloqueos_agendaService bloqueosAgendaService;

    @GetMapping
    public ResponseEntity<List<Bloqueos_agendaDTO>> obtenerTodos() {

        return ResponseEntity.ok(
                bloqueosAgendaService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bloqueos_agendaDTO> obtenerPorId(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                bloqueosAgendaService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<Bloqueos_agendaDTO> crear(
            @RequestBody Bloqueos_agendaDTO bloqueosAgendaDTO) {

        Bloqueos_agendaDTO nuevoBloqueo = bloqueosAgendaService.crear(bloqueosAgendaDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(nuevoBloqueo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Bloqueos_agendaDTO> actualizar(
            @PathVariable UUID id,
            @RequestBody Bloqueos_agendaDTO bloqueosAgendaDTO) {

        return ResponseEntity.ok(
                bloqueosAgendaService.actualizar(id, bloqueosAgendaDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable UUID id) {

        bloqueosAgendaService.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}