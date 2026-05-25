package com.example.spapet.controller;

import com.example.spapet.dto.ProductosDTO;
import com.example.spapet.dto.Variantes_productosDTO;
import com.example.spapet.service.InventarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/inventario")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class InventarioController {

    private final InventarioService inventarioService;

    @GetMapping("/variantes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Variantes_productosDTO>> listarVariantes() {
        return ResponseEntity.ok(inventarioService.listarVariantes());
    }

    @GetMapping("/alertas")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Variantes_productosDTO>> alertasStockBajo() {
        return ResponseEntity.ok(inventarioService.listarStockBajo());
    }

    @PatchMapping("/variantes/{id}/reponer")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Variantes_productosDTO> reponer(
            @PathVariable UUID id,
            @RequestParam Integer cantidad) {
        return ResponseEntity.ok(inventarioService.reponer(id, cantidad));
    }

    @PatchMapping("/variantes/{id}/stock-minimo")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Variantes_productosDTO> actualizarStockMinimo(
            @PathVariable UUID id,
            @RequestParam Integer stockMinimo) {
        return ResponseEntity.ok(inventarioService.actualizarStockMinimo(id, stockMinimo));
    }
}