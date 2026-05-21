package com.example.spapet.dto.auth;

import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {

    private String token;
    private String tipo = "Bearer";
    private UUID usuarioId;
    private String nombre;
    private String apellido;
    private String correo;
    private String rol;
    private Boolean requiere2fa;
}