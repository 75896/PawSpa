package com.example.spapet.dto;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditoriaDTO {
    private UUID id;
    private String correo;
    private String usuarioNombre;
    private String usuarioApellido;
    private String accion;
    private String resultado;
    private String ip;
    private String navegador;
    private String detalle;
    private OffsetDateTime creadoEn;
}