package com.example.spapet.controller;

import com.example.spapet.dto.ProductoConVariantesDTO;
import com.example.spapet.dto.ProductosDTO;
import com.example.spapet.service.ProductosService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductosController {

    private final ProductosService productosService;

    // =============================================
    // PÚBLICOS — landing page (sin auth)
    // =============================================

    // Catálogo activo, con filtro opcional por categoría
    @GetMapping("/publico")
    public ResponseEntity<List<ProductosDTO>> catalogo(
            @RequestParam(required = false) UUID categoriaId) {
        if (categoriaId != null) {
            return ResponseEntity.ok(productosService.obtenerActivosPorCategoria(categoriaId));
        }
        return ResponseEntity.ok(productosService.obtenerActivos());
    }

    // Detalle de producto con todas sus variantes
    @GetMapping("/publico/{id}")
    public ResponseEntity<ProductoConVariantesDTO> detalle(
            @PathVariable UUID id) {
        return ResponseEntity.ok(productosService.obtenerConVariantes(id));
    }

    // =============================================
    // ADMIN — CRUD completo
    // =============================================

    @GetMapping
    public ResponseEntity<List<ProductosDTO>> obtenerTodos() {
        return ResponseEntity.ok(productosService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductosDTO> obtenerPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(productosService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<ProductosDTO> crear(@RequestBody ProductosDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productosService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductosDTO> actualizar(
            @PathVariable UUID id, @RequestBody ProductosDTO dto) {
        return ResponseEntity.ok(productosService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        productosService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}