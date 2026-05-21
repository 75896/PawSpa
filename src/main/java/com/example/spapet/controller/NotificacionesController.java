package com.example.spapet.controller;

import com.example.spapet.dto.NotificacionesDTO;
import com.example.spapet.service.NotificacionesService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notificaciones")
@RequiredArgsConstructor
public class NotificacionesController {

    private final NotificacionesService notificacionesService;

    @GetMapping
    public ResponseEntity<List<NotificacionesDTO>> obtenerTodos() {
        return ResponseEntity.ok(
                notificacionesService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificacionesDTO> obtenerPorId(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                notificacionesService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<NotificacionesDTO> crear(
            @RequestBody NotificacionesDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(notificacionesService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NotificacionesDTO> actualizar(
            @PathVariable UUID id,
            @RequestBody NotificacionesDTO dto) {

        return ResponseEntity.ok(
                notificacionesService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable UUID id) {

        notificacionesService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}