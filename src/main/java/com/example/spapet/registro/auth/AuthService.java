package com.example.spapet.registro.auth;

import com.example.spapet.dto.auth.*;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request, HttpServletRequest httpRequest);

    AuthResponse login(LoginRequest request, HttpServletRequest httpRequest);

    void activarCuenta(String token);

    void reenviarActivacion(String correo);

    void solicitarCambioPassword(SolicitarCambioRequest request);

    void cambiarPassword(CambioPasswordRequest request, HttpServletRequest httpRequest);
}