package com.example.spapet.registro.auth;

import com.example.spapet.dto.auth.AuthResponse;
import com.example.spapet.dto.auth.CambioPasswordRequest;
import com.example.spapet.dto.auth.LoginRequest;
import com.example.spapet.dto.auth.RegisterRequest;
import com.example.spapet.dto.auth.SolicitarCambioRequest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody RegisterRequest request,
            HttpServletRequest httpRequest) {
        return ResponseEntity.ok(authService.register(request, httpRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest) {
        return ResponseEntity.ok(authService.login(request, httpRequest));
    }

    @GetMapping("/activar")
    public ResponseEntity<String> activar(@RequestParam String token) {
        authService.activarCuenta(token);
        return ResponseEntity.ok("Cuenta activada correctamente");
    }

    @PostMapping("/reenviar-activacion")
    public ResponseEntity<String> reenviarActivacion(@RequestParam String correo) {
        authService.reenviarActivacion(correo);
        return ResponseEntity.ok("Correo de activación reenviado");
    }

    @PostMapping("/solicitar-cambio-password")
    public ResponseEntity<String> solicitarCambio(
            @Valid @RequestBody SolicitarCambioRequest request) {
        authService.solicitarCambioPassword(request);
        return ResponseEntity.ok("Correo de cambio de contraseña enviado");
    }

    @PostMapping("/cambiar-password")
    public ResponseEntity<String> cambiarPassword(
            @Valid @RequestBody CambioPasswordRequest request,
            HttpServletRequest httpRequest) {
        authService.cambiarPassword(request, httpRequest);
        return ResponseEntity.ok("Contraseña cambiada correctamente");
    }
}