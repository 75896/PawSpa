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
public class Variantes_productosDTO implements Serializable {

    private UUID id;

    @NotNull(message = "El producto es obligatorio")
    private UUID productoId;

    private String productoNombre;

    @NotBlank(message = "El tipo de variante es obligatorio")
    @Pattern(regexp = "^(tamano|color|peso|sabor|otro)$", message = "El tipo de variante debe ser tamano, color, peso, sabor u otro")
    @Size(max = 20, message = "El tipo de variante no puede tener más de 20 caracteres")
    private String tipoVariante;

    @NotBlank(message = "El nombre de la variante no puede estar vacío")
    @Size(max = 100, message = "El nombre no puede tener más de 100 caracteres")
    private String nombre;

    @Size(max = 100, message = "El SKU no puede tener más de 100 caracteres")
    private String sku;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El precio no puede ser negativo")
    private BigDecimal precio;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;

    @NotNull(message = "El stock mínimo es obligatorio")
    @Min(value = 0, message = "El stock mínimo no puede ser negativo")
    private Integer stockMinimo;

    @NotBlank(message = "El nivel de alerta es obligatorio")
    @Pattern(regexp = "^(ok|bajo|critico)$", message = "El nivel de alerta debe ser ok, bajo o critico")
    @Size(max = 20, message = "El nivel de alerta no puede tener más de 20 caracteres")
    private String nivelAlerta;

    @NotNull(message = "El estado activa es obligatorio")
    private Boolean activa;

    private OffsetDateTime creadoEn;

    private OffsetDateTime actualizadoEn;
}