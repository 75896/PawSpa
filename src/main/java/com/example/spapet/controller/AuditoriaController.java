package com.example.spapet.controller;

import com.example.spapet.dto.AuditoriaDTO;
import com.example.spapet.service.AuditoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/auditoria")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AuditoriaController {

    private final AuditoriaService auditoriaService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AuditoriaDTO>> listar() {
        return ResponseEntity.ok(auditoriaService.listarTodos());
    }

    @GetMapping("/correo/{correo}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AuditoriaDTO>> porCorreo(@PathVariable String correo) {
        return ResponseEntity.ok(auditoriaService.listarPorCorreo(correo));
    }

    @GetMapping("/accion/{accion}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AuditoriaDTO>> porAccion(@PathVariable String accion) {
        return ResponseEntity.ok(auditoriaService.listarPorAccion(accion));
    }

    @GetMapping("/resultado/{resultado}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AuditoriaDTO>> porResultado(@PathVariable String resultado) {
        return ResponseEntity.ok(auditoriaService.listarPorResultado(resultado));
    }
}