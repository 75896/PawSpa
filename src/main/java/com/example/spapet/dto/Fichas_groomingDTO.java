package com.example.spapet.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Fichas_groomingDTO implements Serializable {

    private UUID id;

    @NotNull(message = "La cita es obligatoria")
    private UUID citaId;

    private String mascotaNombre;
    private String mascotaEspecie;
    private String mascotaTemperamento;
    private String mascotaAlergias;
    private String mascotaRestricciones;

    @NotNull(message = "El groomer es obligatorio")
    private UUID groomerId;

    private String groomerNombre;
    private String groomerApellido;

    @NotBlank(message = "El estado es obligatorio")
    @Pattern(regexp = "^(abierta|en_proceso|cerrada)$", message = "El estado debe ser 'abierta', 'en_proceso' o 'cerrada'")
    private String estado;

    @NotNull(message = "El campo tiene nudos es obligatorio")
    private Boolean tieneNudos;

    @NotNull(message = "El campo tiene pulgas es obligatorio")
    private Boolean tienePulgas;

    @NotNull(message = "El campo tiene heridas es obligatorio")
    private Boolean tieneHeridas;

    @Pattern(regexp = "^(leve|moderado|severo)$", message = "El nivel de nudos debe ser 'leve', 'moderado' o 'severo'")
    private String nivelNudos;

    @Size(max = 500, message = "Las observaciones de ingreso no pueden tener más de 500 caracteres")
    private String observacionesIngreso;

    @Size(max = 500, message = "Las observaciones de salida no pueden tener más de 500 caracteres")
    private String observacionesSalida;

    @Size(max = 500, message = "Las recomendaciones no pueden tener más de 500 caracteres")
    private String recomendaciones;

    @DecimalMin(value = "0.0", inclusive = false, message = "El peso debe ser mayor a 0")
    @Digits(integer = 3, fraction = 2, message = "El peso debe tener máximo 3 enteros y 2 decimales")
    private BigDecimal pesoKgActual;

    private OffsetDateTime abiertaEn;

    private OffsetDateTime cerradaEn;

    private OffsetDateTime creadoEn;

    private OffsetDateTime actualizadoEn;
}