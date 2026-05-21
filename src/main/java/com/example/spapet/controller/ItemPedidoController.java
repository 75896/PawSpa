package com.example.spapet.controller;

import com.example.spapet.dto.Items_pedidoDTO;
import com.example.spapet.service.Items_pedidoService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/items-pedido")
@RequiredArgsConstructor
public class ItemPedidoController {

    private final Items_pedidoService itemsPedidoService;

    @GetMapping
    public ResponseEntity<List<Items_pedidoDTO>> obtenerTodos() {
        return ResponseEntity.ok(
                itemsPedidoService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Items_pedidoDTO> obtenerPorId(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                itemsPedidoService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<Items_pedidoDTO> crear(
            @RequestBody Items_pedidoDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(itemsPedidoService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Items_pedidoDTO> actualizar(
            @PathVariable UUID id,
            @RequestBody Items_pedidoDTO dto) {

        return ResponseEntity.ok(
                itemsPedidoService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable UUID id) {

        itemsPedidoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}