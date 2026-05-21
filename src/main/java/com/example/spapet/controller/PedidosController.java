package com.example.spapet.controller;

import com.example.spapet.dto.PedidosDTO;
import com.example.spapet.service.PedidosService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidosController {

    private final PedidosService pedidosService;

    @GetMapping
    public ResponseEntity<List<PedidosDTO>> obtenerTodos() {
        return ResponseEntity.ok(
                pedidosService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidosDTO> obtenerPorId(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                pedidosService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<PedidosDTO> crear(
            @RequestBody PedidosDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(pedidosService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PedidosDTO> actualizar(
            @PathVariable UUID id,
            @RequestBody PedidosDTO dto) {

        return ResponseEntity.ok(
                pedidosService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable UUID id) {

        pedidosService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}