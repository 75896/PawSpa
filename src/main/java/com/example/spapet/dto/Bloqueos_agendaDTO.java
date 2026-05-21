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
public class Bloqueos_agendaDTO implements Serializable {

    private UUID id;

    private UUID groomerId;

    private String groomerNombre;
    private String groomerApellido;

    @NotNull(message = "La fecha de inicio es obligatoria")
    @FutureOrPresent(message = "La fecha de inicio debe ser presente o futura")
    private OffsetDateTime fechaInicio;

    @NotNull(message = "La fecha de fin es obligatoria")
    @FutureOrPresent(message = "La fecha de fin debe ser presente o futura")
    private OffsetDateTime fechaFin;

    @Size(max = 200, message = "El motivo no puede tener más de 200 caracteres")
    private String motivo;

    private UUID creadoPorId;

    private String creadoPorNombre;

    private OffsetDateTime creadoEn;
}