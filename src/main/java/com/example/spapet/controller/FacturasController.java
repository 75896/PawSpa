package com.example.spapet.controller;

import com.example.spapet.dto.FacturasDTO;
import com.example.spapet.dto.ReciboDTO;
import com.example.spapet.service.FacturasService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/facturas")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class FacturasController {

    private final FacturasService facturasService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION')")
    public ResponseEntity<List<FacturasDTO>> obtenerTodos() {
        return ResponseEntity.ok(facturasService.obtenerTodos());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION')")
    public ResponseEntity<FacturasDTO> obtenerPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(facturasService.obtenerPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION')")
    public ResponseEntity<FacturasDTO> crear(@RequestBody FacturasDTO dto) {
        return ResponseEntity.ok(facturasService.crear(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION')")
    public ResponseEntity<FacturasDTO> actualizar(@PathVariable UUID id, @RequestBody FacturasDTO dto) {
        return ResponseEntity.ok(facturasService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        facturasService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // Generar factura automática desde cita completada
    @PostMapping("/generar/cita/{citaId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION', 'GROOMER')")
    public ResponseEntity<FacturasDTO> generarDesdeCita(@PathVariable UUID citaId) {
        return ResponseEntity.ok(facturasService.generarDesdeCita(citaId));
    }

    // Generar factura automática desde pedido confirmado
    @PostMapping("/generar/pedido/{pedidoId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION')")
    public ResponseEntity<FacturasDTO> generarDesdePedido(@PathVariable UUID pedidoId) {
        return ResponseEntity.ok(facturasService.generarDesdePedido(pedidoId));
    }

    // Obtener recibo de una factura pagada
    @GetMapping("/{id}/recibo")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION')")
    public ResponseEntity<ReciboDTO> recibo(@PathVariable UUID id) {
        return ResponseEntity.ok(facturasService.generarRecibo(id));
    }
}