package com.example.spapet.controller;

import com.example.spapet.dto.UsuariosDTO;
import com.example.spapet.service.UsuariosService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/usuarios")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class UsuariosController {

    private final UsuariosService usuarioService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsuariosDTO>> listar() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuariosDTO> obtener(@PathVariable UUID id) {
        return ResponseEntity.ok(usuarioService.obtenerPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuariosDTO> crear(@Valid @RequestBody UsuariosDTO dto) {
        return ResponseEntity.ok(usuarioService.crear(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuariosDTO> actualizar(@PathVariable UUID id,
            @Valid @RequestBody UsuariosDTO dto) {
        return ResponseEntity.ok(usuarioService.actualizar(id, dto));
    }

    @PatchMapping("/{id}/desactivar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> desactivar(@PathVariable UUID id) {
        usuarioService.desactivar(id);
        return ResponseEntity.ok("Usuario desactivado correctamente");
    }

    @PatchMapping("/{id}/activar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> activar(@PathVariable UUID id) {
        usuarioService.activar(id);
        return ResponseEntity.ok("Usuario activado correctamente");
    }

    @GetMapping("/clientes")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION')")
    public ResponseEntity<List<UsuariosDTO>> listarClientes() {
        return ResponseEntity.ok(usuarioService.listarPorRol("cliente"));
    }
}