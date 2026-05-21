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
public class Fotos_mascotaDTO implements Serializable {

    private UUID id;

    @NotNull(message = "La mascota es obligatoria")
    private UUID mascotaId;

    private String mascotaNombre;

    private UUID fichaId;

    @NotBlank(message = "La URL de la foto no puede estar vacía")
    private String url;

    @Size(max = 50, message = "La etiqueta no puede tener más de 50 caracteres")
    private String etiqueta;

    @Size(max = 1000, message = "La descripción no puede tener más de 1000 caracteres")
    private String descripcion;

    private UUID subidaPorId;

    private String subidaPorNombre;

    private OffsetDateTime creadoEn;
}