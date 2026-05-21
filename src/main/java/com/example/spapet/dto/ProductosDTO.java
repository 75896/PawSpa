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
public class ProductosDTO implements Serializable {

    private UUID id;

    private UUID categoriaId;

    private String categoriaNombre;

    @NotBlank(message = "El nombre del producto no puede estar vacío")
    @Size(max = 200, message = "El nombre no puede tener más de 200 caracteres")
    private String nombre;

    @Size(max = 5000, message = "La descripción no puede tener más de 5000 caracteres")
    private String descripcion;

    private String imagenUrl;

    @NotNull(message = "El precio base es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El precio base no puede ser negativo")
    private BigDecimal precioBase;

    @NotNull(message = "El estado activo es obligatorio")
    private Boolean activo;

    private OffsetDateTime creadoEn;

    private OffsetDateTime actualizadoEn;
}