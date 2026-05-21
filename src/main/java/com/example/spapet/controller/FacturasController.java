package com.example.spapet.controller;

import com.example.spapet.dto.FacturasDTO;
import com.example.spapet.service.FacturasService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/facturas")
@RequiredArgsConstructor
public class FacturasController {

    private final FacturasService facturasService;

    @GetMapping
    public ResponseEntity<List<FacturasDTO>> obtenerTodos() {

        return ResponseEntity.ok(
                facturasService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacturasDTO> obtenerPorId(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                facturasService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<FacturasDTO> crear(
            @RequestBody FacturasDTO dto) {

        FacturasDTO nuevaFactura = facturasService.crear(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(nuevaFactura);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FacturasDTO> actualizar(
            @PathVariable UUID id,
            @RequestBody FacturasDTO dto) {

        return ResponseEntity.ok(
                facturasService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable UUID id) {

        facturasService.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}