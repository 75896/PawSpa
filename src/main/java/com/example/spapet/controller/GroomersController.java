package com.example.spapet.controller;

import com.example.spapet.dto.GroomersDTO;
import com.example.spapet.service.GroomersService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/groomers")
@RequiredArgsConstructor
public class GroomersController {

    private final GroomersService groomersService;

    @GetMapping
    public ResponseEntity<List<GroomersDTO>> obtenerTodos() {
        return ResponseEntity.ok(
                groomersService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroomersDTO> obtenerPorId(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                groomersService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<GroomersDTO> crear(
            @RequestBody GroomersDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(groomersService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GroomersDTO> actualizar(
            @PathVariable UUID id,
            @RequestBody GroomersDTO dto) {

        return ResponseEntity.ok(
                groomersService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable UUID id) {

        groomersService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}