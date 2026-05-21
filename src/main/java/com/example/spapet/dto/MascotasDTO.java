package com.example.spapet.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MascotasDTO implements Serializable {

    private UUID id;

    @NotNull(message = "El cliente es obligatorio")
    private UUID clienteId;

    private String clienteNombre;
    private String clienteApellido;

    @NotBlank(message = "El nombre de la mascota es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    private UUID razaId;

    private String razaNombre;

    @NotBlank(message = "La especie es obligatoria")
    @Pattern(regexp = "^(perro|gato|conejo|ave|otro)$", message = "La especie debe ser 'perro', 'gato', 'conejo', 'ave' u 'otro'")
    private String especie;

    @Past(message = "La fecha de nacimiento debe ser anterior a hoy")
    private LocalDate fechaNac;

    @NotBlank(message = "El sexo es obligatorio")
    @Pattern(regexp = "^(macho|hembra|desconocido)$", message = "El sexo debe ser 'macho', 'hembra' o 'desconocido'")
    private String sexo;

    @DecimalMin(value = "0.0", inclusive = false, message = "El peso debe ser mayor a 0")
    @Digits(integer = 3, fraction = 2, message = "El peso debe tener máximo 3 enteros y 2 decimales")
    private BigDecimal pesoKg;

    @Size(max = 100, message = "El color de pelaje no puede tener más de 100 caracteres")
    private String colorPelaje;

    @NotBlank(message = "El temperamento es obligatorio")
    @Pattern(regexp = "^(tranquilo|normal|nervioso|agresivo|desconocido)$", message = "El temperamento debe ser 'tranquilo', 'normal', 'nervioso', 'agresivo' o 'desconocido'")
    private String temperamento;

    @Size(max = 500, message = "Las alergias no pueden tener más de 500 caracteres")
    private String alergias;

    @Size(max = 500, message = "Las restricciones no pueden tener más de 500 caracteres")
    private String restricciones;

    private Boolean activa;

    private String fotoUrl;

    private OffsetDateTime creadoEn;

    private OffsetDateTime actualizadoEn;
}