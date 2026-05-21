package com.example.spapet.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroomersDTO implements Serializable {

    private UUID id;

    @NotNull(message = "El usuario es obligatorio")
    private UUID usuarioId;

    private String usuarioNombre;
    private String usuarioApellido;
    private String usuarioCorreo;

    private String[] especialidades;

    @NotNull(message = "La capacidad máxima es obligatoria")
    @Min(value = 1, message = "La capacidad máxima debe ser al menos 1")
    private Short capacidadMax;

    private Boolean activo;

    private OffsetDateTime creadoEn;

    private OffsetDateTime actualizadoEn;
}
