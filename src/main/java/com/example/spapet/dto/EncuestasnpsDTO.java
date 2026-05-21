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
public class EncuestasnpsDTO implements Serializable {

    private UUID id;

    @NotNull(message = "La cita es obligatoria")
    private UUID citaId;

    @NotNull(message = "El cliente es obligatorio")
    private UUID clienteId;

    private String clienteNombre;

    @NotNull(message = "La puntuación es obligatoria")
    @Min(value = 1, message = "La puntuación mínima es 1")
    @Max(value = 10, message = "La puntuación máxima es 10")
    private Short puntuacion;

    @Size(max = 1000, message = "El comentario no puede tener más de 1000 caracteres")
    private String comentario;

    private OffsetDateTime respondidaEn;
}