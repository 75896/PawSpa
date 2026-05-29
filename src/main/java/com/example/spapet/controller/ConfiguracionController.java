package com.example.spapet.controller;

import com.example.spapet.model.Configuracion;
import com.example.spapet.model.Usuarios;
import com.example.spapet.repository.ConfiguracionRepository;
import com.example.spapet.repository.UsuariosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/configuracion")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ConfiguracionController {

    private final ConfiguracionRepository configuracionRepository;
    private final UsuariosRepository usuariosRepository;

    // Obtener una clave pública (para mostrar QR sin auth)
    @GetMapping("/{clave}")
    public ResponseEntity<Map<String, String>> obtener(@PathVariable String clave) {
        return configuracionRepository.findById(clave)
                .map(c -> ResponseEntity.ok(Map.of("clave", c.getClave(), "valor", c.getValor())))
                .orElse(ResponseEntity.notFound().build());
    }

    // Actualizar o crear una clave (solo admin/recepcion)
    @PutMapping("/{clave}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION')")
    public ResponseEntity<Map<String, String>> actualizar(
            @PathVariable String clave,
            @RequestBody Map<String, String> body,
            @AuthenticationPrincipal String correo) {

        Usuarios usuario = usuariosRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Configuracion config = configuracionRepository.findById(clave)
                .orElse(Configuracion.builder()
                        .clave(clave)
                        .descripcion(body.getOrDefault("descripcion", ""))
                        .build());

        config.setValor(body.get("valor"));
        config.setActualizadoPor(usuario);
        configuracionRepository.save(config);

        return ResponseEntity.ok(Map.of("clave", clave, "valor", config.getValor()));
    }
}