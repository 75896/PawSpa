package com.example.spapet.controller;

import com.example.spapet.dto.CitasDTO;
import com.example.spapet.dto.Fichas_groomingDTO;
import com.example.spapet.dto.GroomersDTO;
import com.example.spapet.dto.Insumos_groomingDTO;
import com.example.spapet.dto.ProductosDTO;
import com.example.spapet.service.*;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/groomers")
@RequiredArgsConstructor
public class GroomersController {

    // private final GroomersService groomersService;
    private final GroomersService groomersService;
    private final Fichas_groomingService fichasService;
    private final Insumos_groomingService insumosService;

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

    @GetMapping("/citas/hoy")
    @PreAuthorize("hasAnyRole('ADMIN', 'GROOMER')")
    public ResponseEntity<List<CitasDTO>> misCitasHoy(
            @AuthenticationPrincipal String correo) {
        return ResponseEntity.ok(groomersService.listarCitasHoy(correo));
    }

    @GetMapping("/citas")
    @PreAuthorize("hasAnyRole('ADMIN', 'GROOMER')")
    public ResponseEntity<List<CitasDTO>> misCitas(
            @AuthenticationPrincipal String correo) {
        return ResponseEntity.ok(groomersService.listarCitasPorCorreo(correo));
    }

    @GetMapping("/fichas")
    @PreAuthorize("hasAnyRole('ADMIN', 'GROOMER')")
    public ResponseEntity<List<Fichas_groomingDTO>> misFichas(
            @AuthenticationPrincipal String correo) {
        return ResponseEntity.ok(fichasService.listarPorCorreo(correo));
    }

    @PostMapping("/fichas/cita/{citaId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GROOMER')")
    public ResponseEntity<Fichas_groomingDTO> abrirFicha(
            @PathVariable UUID citaId,
            @AuthenticationPrincipal String correo) {
        return ResponseEntity.ok(fichasService.abrirFicha(citaId, correo));
    }

    @PutMapping("/fichas/{fichaId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GROOMER')")
    public ResponseEntity<Fichas_groomingDTO> actualizarFicha(
            @PathVariable UUID fichaId,
            @RequestBody Fichas_groomingDTO dto) {
        return ResponseEntity.ok(fichasService.actualizar(fichaId, dto));
    }

    @PatchMapping("/fichas/{fichaId}/cerrar")
    @PreAuthorize("hasAnyRole('ADMIN', 'GROOMER')")
    public ResponseEntity<Fichas_groomingDTO> cerrarFicha(
            @PathVariable UUID fichaId) {
        return ResponseEntity.ok(fichasService.cerrar(fichaId));
    }

    @GetMapping("/fichas/cita/{citaId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GROOMER')")
    public ResponseEntity<Fichas_groomingDTO> fichaPorCita(@PathVariable UUID citaId) {
        return fichasService.buscarPorCitaId(citaId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/insumos/disponibles")
    @PreAuthorize("hasAnyRole('ADMIN', 'GROOMER')")
    public ResponseEntity<List<ProductosDTO>> productosDisponibles() {
        return ResponseEntity.ok(insumosService.listarProductosDisponibles());
    }

    @GetMapping("/fichas/{fichaId}/insumos")
    @PreAuthorize("hasAnyRole('ADMIN', 'GROOMER')")
    public ResponseEntity<List<Insumos_groomingDTO>> insumosPorFicha(
            @PathVariable UUID fichaId) {
        return ResponseEntity.ok(insumosService.listarPorFicha(fichaId));
    }

    @PostMapping("/fichas/{fichaId}/insumos")
    @PreAuthorize("hasAnyRole('ADMIN', 'GROOMER')")
    public ResponseEntity<Insumos_groomingDTO> registrarInsumo(
            @PathVariable UUID fichaId,
            @RequestBody Insumos_groomingDTO dto) {
        dto.setFichaId(fichaId);
        return ResponseEntity.ok(insumosService.registrar(dto));
    }

    @DeleteMapping("/insumos/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GROOMER')")
    public ResponseEntity<String> eliminarInsumo(@PathVariable UUID id) {
        insumosService.eliminar(id);
        return ResponseEntity.ok("Insumo eliminado y stock restaurado");
    }

    @GetMapping("/alertas/stock-bajo")
    @PreAuthorize("hasAnyRole('ADMIN', 'GROOMER')")
    public ResponseEntity<List<ProductosDTO>> alertasStockBajo() {
        return ResponseEntity.ok(insumosService.listarStockBajo());
    }

}