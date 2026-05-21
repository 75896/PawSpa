package com.example.spapet.controller;

import com.example.spapet.dto.CitasDTO;
import com.example.spapet.dto.MascotasDTO;
import com.example.spapet.service.CitasService;
import com.example.spapet.service.MascotasService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/cliente")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ClientesController {

    private final MascotasService mascotasService;
    private final CitasService citasService;

    @GetMapping("/mascotas")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<List<MascotasDTO>> misMascotas(
            @AuthenticationPrincipal String correo) {
        return ResponseEntity.ok(mascotasService.listarPorCorreo(correo));
    }

    @PostMapping("/mascotas")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<MascotasDTO> agregarMascota(
            @AuthenticationPrincipal String correo,
            @RequestBody MascotasDTO dto) {
        return ResponseEntity.ok(mascotasService.crearParaCliente(correo, dto));
    }

    @GetMapping("/citas")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<List<CitasDTO>> misCitas(
            @AuthenticationPrincipal String correo) {
        return ResponseEntity.ok(citasService.listarPorCorreo(correo));
    }

    @PutMapping("/mascotas/{id}")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<MascotasDTO> editarMascota(
            @PathVariable UUID id,
            @RequestBody MascotasDTO dto) {
        return ResponseEntity.ok(mascotasService.actualizar(id, dto));
    }

    @GetMapping("/{clienteId}/mascotas")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION')")
    public ResponseEntity<List<MascotasDTO>> mascotasPorCliente(
            @PathVariable UUID clienteId) {
        return ResponseEntity.ok(mascotasService.listarPorClienteId(clienteId));
    }
}