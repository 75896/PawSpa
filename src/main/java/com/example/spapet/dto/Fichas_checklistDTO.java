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
public class Fichas_checklistDTO implements Serializable {

    private UUID id;

    @NotNull(message = "La ficha es obligatoria")
    private UUID fichaId;

    @NotNull(message = "El item es obligatorio")
    private UUID itemId;

    private String itemNombre;

    @NotNull(message = "El estado realizado es obligatorio")
    private Boolean realizado;

    @Size(max = 1000, message = "La observación no puede tener más de 1000 caracteres")
    private String observacion;

    private OffsetDateTime registradoEn;
}