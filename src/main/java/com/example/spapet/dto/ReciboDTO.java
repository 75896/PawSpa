package com.example.spapet.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReciboDTO {

    private UUID facturaId;
    private String numeroFactura;
    private String clienteNombre;
    private String clienteTelefono;

    // Servicio o Tienda
    private String tipo; // "servicio" o "pedido"

    // Si es servicio
    private String servicioNombre;
    private String mascotaNombre;
    private OffsetDateTime fechaCita;

    // Si es pedido
    private List<ReciboItemDTO> items;

    private BigDecimal subtotal;
    private BigDecimal descuento;
    private BigDecimal total;
    private String medioPago;
    private String referenciaPago;
    private OffsetDateTime pagadoEn;

    @Getter
    @Setter
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ReciboItemDTO {
        private String productoNombre;
        private String varianteNombre;
        private Integer cantidad;
        private BigDecimal precioUnitario;
        private BigDecimal subtotal;
    }
}