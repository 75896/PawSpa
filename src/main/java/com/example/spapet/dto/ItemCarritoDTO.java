package com.example.spapet.dto;

import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemCarritoDTO {

    private UUID itemId;
    private UUID varianteId;
    private String varianteNombre;
    private String productoNombre;
    private String productoImagenUrl;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
}