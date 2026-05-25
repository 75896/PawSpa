package com.example.spapet.dto;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarritoDTO {

    private UUID pedidoId;
    private String estado;
    private List<ItemCarritoDTO> items;
    private BigDecimal subtotal;
    private BigDecimal descuento;
    private BigDecimal total;
    private String direccionEntrega;
    private String notas;
}