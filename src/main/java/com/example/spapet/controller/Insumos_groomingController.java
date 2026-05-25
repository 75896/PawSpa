package com.example.spapet.controller;

import com.example.spapet.dto.Insumos_groomingDTO;
import com.example.spapet.service.Insumos_groomingService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/insumos-grooming")
@RequiredArgsConstructor
public class Insumos_groomingController {

    private final Insumos_groomingService insumosGroomingService;

    // @GetMapping
    // public ResponseEntity<List<Insumos_groomingDTO>> obtenerTodos() {
    // return ResponseEntity.ok(
    // insumosGroomingService.obtenerTodos());
    // }

    // @GetMapping("/{id}")
    // public ResponseEntity<Insumos_groomingDTO> obtenerPorId(
    // @PathVariable UUID id) {
    //
    // return ResponseEntity.ok(
    // insumosGroomingService.obtenerPorId(id));
    // }

    // @PostMapping
    // public ResponseEntity<Insumos_groomingDTO> crear(
    // @RequestBody Insumos_groomingDTO dto) {

    // return ResponseEntity
    // .status(HttpStatus.CREATED)
    // .body(insumosGroomingService.crear(dto));
    // }

    // @PutMapping("/{id}")
    // public ResponseEntity<Insumos_groomingDTO> actualizar(
    // @PathVariable UUID id,
    // @RequestBody Insumos_groomingDTO dto) {

    // return ResponseEntity.ok(
    // insumosGroomingService.actualizar(id, dto));
    // }

    // @DeleteMapping("/{id}")
    // public ResponseEntity<Void> eliminar(
    // @PathVariable UUID id) {

    // insumosGroomingService.eliminar(id);
    // return ResponseEntity.noContent().build();
    // }
}