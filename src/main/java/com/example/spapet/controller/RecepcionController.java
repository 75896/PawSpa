package com.example.spapet.controller;

import com.example.spapet.dto.*;
import com.example.spapet.service.MascotasService;
import com.example.spapet.service.RecepcionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/recepcion")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class RecepcionController {

    private final RecepcionService recepcionService;
    private final MascotasService mascotasService;

    @GetMapping("/citas")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION')")
    public ResponseEntity<List<CitasDTO>> listarCitas() {
        return ResponseEntity.ok(recepcionService.listarTodasCitas());
    }

    @GetMapping("/citas/hoy")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION')")
    public ResponseEntity<List<CitasDTO>> listarCitasHoy() {
        return ResponseEntity.ok(recepcionService.listarCitasHoy());
    }

    @GetMapping("/citas/fecha")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION')")
    public ResponseEntity<List<CitasDTO>> listarCitasPorFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(recepcionService.listarCitasPorFecha(fecha));
    }

    @PostMapping("/citas")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION')")
    public ResponseEntity<CitasDTO> agendarCita(@Valid @RequestBody CitasDTO dto) {
        return ResponseEntity.ok(recepcionService.agendarCita(dto));
    }

    @PatchMapping("/citas/{id}/confirmar")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION')")
    public ResponseEntity<CitasDTO> confirmar(@PathVariable UUID id) {
        return ResponseEntity.ok(recepcionService.cambiarEstado(id, "confirmada"));
    }

    @PatchMapping("/citas/{id}/cancelar")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION')")
    public ResponseEntity<CitasDTO> cancelar(@PathVariable UUID id,
            @RequestParam String motivo) {
        return ResponseEntity.ok(recepcionService.cancelarCita(id, motivo));
    }

    @GetMapping("/servicios")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION', 'CLIENTE')")
    public ResponseEntity<List<ServiciosDTO>> listarServicios() {
        return ResponseEntity.ok(recepcionService.listarServicios());
    }

    @GetMapping("/groomers")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION', 'CLIENTE')")
    public ResponseEntity<List<GroomersDTO>> listarGroomers() {
        return ResponseEntity.ok(recepcionService.listarGroomers());
    }

    @GetMapping("/bloqueos")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION')")
    public ResponseEntity<List<Bloqueos_agendaDTO>> listarBloqueos() {
        return ResponseEntity.ok(recepcionService.listarBloqueos());
    }

    @PostMapping("/bloqueos")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION')")
    public ResponseEntity<Bloqueos_agendaDTO> crearBloqueo(
            @Valid @RequestBody Bloqueos_agendaDTO dto) {
        return ResponseEntity.ok(recepcionService.crearBloqueo(dto));
    }

    @DeleteMapping("/bloqueos/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION')")
    public ResponseEntity<String> eliminarBloqueo(@PathVariable UUID id) {
        recepcionService.eliminarBloqueo(id);
        return ResponseEntity.ok("Bloqueo eliminado");
    }

    @GetMapping("/clientes")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION')")
    public ResponseEntity<List<UsuariosDTO>> listarClientes() {
        return ResponseEntity.ok(recepcionService.listarClientes());
    }

    @GetMapping("/clientes/{usuarioId}/mascotas")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION')")
    public ResponseEntity<List<MascotasDTO>> mascotasPorCliente(
            @PathVariable UUID usuarioId) {
        return ResponseEntity.ok(recepcionService.listarMascotasPorUsuarioId(usuarioId));
    }

    @PatchMapping("/citas/{id}/asignar-groomer")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION')")
    public ResponseEntity<CitasDTO> asignarGroomer(
            @PathVariable UUID id,
            @RequestParam UUID groomerId) {
        return ResponseEntity.ok(recepcionService.asignarGroomer(id, groomerId));
    }
}