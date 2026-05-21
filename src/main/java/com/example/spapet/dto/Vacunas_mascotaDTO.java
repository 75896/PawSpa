package com.example.spapet.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vacunas_mascotaDTO implements Serializable {

    private UUID id;

    @NotNull(message = "La mascota es obligatoria")
    private UUID mascotaId;

    private String mascotaNombre;

    @NotBlank(message = "El nombre de la vacuna es obligatorio")
    @Size(min = 2, max = 150, message = "El nombre de la vacuna debe tener entre 2 y 150 caracteres")
    private String nombreVacuna;

    @NotNull(message = "La fecha de aplicación es obligatoria")
    @PastOrPresent(message = "La fecha de aplicación debe ser anterior o igual a hoy")
    private LocalDate fechaAplicacion;

    @Future(message = "La fecha próxima debe ser posterior a hoy")
    private LocalDate fechaProxima;

    @Size(max = 150, message = "El nombre del veterinario no puede tener más de 150 caracteres")
    private String veterinario;

    @Size(max = 500, message = "Las observaciones no pueden tener más de 500 caracteres")
    private String observaciones;

    private OffsetDateTime creadoEn;
}