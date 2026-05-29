package com.example.spapet.controller;

import com.example.spapet.dto.CierreCajaDTO;
import com.example.spapet.dto.CobrarDTO;
import com.example.spapet.dto.PagosDTO;
import com.example.spapet.service.PagosService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class PagosController {

    private final PagosService pagosService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION')")
    public ResponseEntity<List<PagosDTO>> obtenerTodos() {
        return ResponseEntity.ok(pagosService.obtenerTodos());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION')")
    public ResponseEntity<PagosDTO> obtenerPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(pagosService.obtenerPorId(id));
    }

    // Punto de venta — cobrar factura
    @PostMapping("/cobrar")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION')")
    public ResponseEntity<PagosDTO> cobrar(
            @Valid @RequestBody CobrarDTO dto,
            @AuthenticationPrincipal String correo) {
        return ResponseEntity.ok(pagosService.cobrar(dto, correo));
    }

    // Cierre de caja del día
    @GetMapping("/cierre-caja")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION')")
    public ResponseEntity<CierreCajaDTO> cierreCaja(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        if (fecha == null)
            fecha = LocalDate.now();
        return ResponseEntity.ok(pagosService.cierreCaja(fecha));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        pagosService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}