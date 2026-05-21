package com.example.spapet.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Items_pedidoDTO implements Serializable {

    private UUID id;

    @NotNull(message = "El pedido es obligatorio")
    private UUID pedidoId;

    @NotNull(message = "La variante es obligatoria")
    private UUID varianteId;

    private String varianteNombre;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;

    @NotNull(message = "El precio unitario es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El precio unitario no puede ser negativo")
    private BigDecimal precioUnitario;

    private BigDecimal subtotal;
}