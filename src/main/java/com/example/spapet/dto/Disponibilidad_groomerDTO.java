package com.example.spapet.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Disponibilidad_groomerDTO implements Serializable {

    private UUID id;

    @NotNull(message = "El groomer es obligatorio")
    private UUID groomerId;

    private String groomerNombre;
    private String groomerApellido;

    @NotNull(message = "El día de la semana es obligatorio")
    @Min(value = 0, message = "El día mínimo es 0 (domingo)")
    @Max(value = 6, message = "El día máximo es 6 (sábado)")
    private Short diaSemana;

    @NotNull(message = "La hora de inicio es obligatoria")
    private LocalTime horaInicio;

    @NotNull(message = "La hora de fin es obligatoria")
    private LocalTime horaFin;

    private Boolean activo;
}