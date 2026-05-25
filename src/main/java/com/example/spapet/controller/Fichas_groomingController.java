package com.example.spapet.controller;

import com.example.spapet.dto.Fichas_groomingDTO;
import com.example.spapet.service.Fichas_groomingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/groomer/fichas")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class Fichas_groomingController {

    private final Fichas_groomingService fichasService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'GROOMER')")
    public ResponseEntity<List<Fichas_groomingDTO>> misFichas(
            @AuthenticationPrincipal String correo) {
        return ResponseEntity.ok(fichasService.listarPorCorreo(correo));
    }

    @PostMapping("/cita/{citaId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GROOMER')")
    public ResponseEntity<Fichas_groomingDTO> abrirFicha(
            @PathVariable UUID citaId,
            @AuthenticationPrincipal String correo) {
        return ResponseEntity.ok(fichasService.abrirFicha(citaId, correo));
    }

    @PutMapping("/{fichaId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GROOMER')")
    public ResponseEntity<Fichas_groomingDTO> actualizarFicha(
            @PathVariable UUID fichaId,
            @RequestBody Fichas_groomingDTO dto) {
        return ResponseEntity.ok(fichasService.actualizar(fichaId, dto));
    }

    @PatchMapping("/{fichaId}/cerrar")
    @PreAuthorize("hasAnyRole('ADMIN', 'GROOMER')")
    public ResponseEntity<Fichas_groomingDTO> cerrarFicha(
            @PathVariable UUID fichaId) {
        return ResponseEntity.ok(fichasService.cerrar(fichaId));
    }

    // @GetMapping("/cita/{citaId}")
    // @PreAuthorize("hasAnyRole('ADMIN', 'GROOMER')")
    // public ResponseEntity<Fichas_groomingDTO> porCita(
    // @PathVariable UUID citaId) {
    // return ResponseEntity.ok(fichasService.buscarPorCitaId(citaId));
    // }
}