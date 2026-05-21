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
public class Insumos_groomingDTO implements Serializable {

    private UUID id;

    @NotNull(message = "La ficha es obligatoria")
    private UUID fichaId;

    @NotNull(message = "La variante es obligatoria")
    private UUID varianteId;

    private String varianteNombre;

    @NotNull(message = "La cantidad es obligatoria")
    @DecimalMin(value = "0.0", inclusive = false, message = "La cantidad debe ser mayor a 0")
    private BigDecimal cantidad;

    @NotBlank(message = "La unidad no puede estar vacía")
    @Size(max = 20, message = "La unidad no puede tener más de 20 caracteres")
    private String unidad;

    private OffsetDateTime registradoEn;
}