package com.example.spapet.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CierreCajaDTO {

    private LocalDate fecha;
    private BigDecimal totalEfectivo;
    private BigDecimal totalQr;
    private BigDecimal totalTransferencia;
    private BigDecimal totalTarjeta;
    private BigDecimal totalGeneral;
    private Integer cantidadPagos;
    private List<CierreCajaDetalleDTO> detalle;

    @Getter
    @Setter
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CierreCajaDetalleDTO {
        private String numeroFactura;
        private String clienteNombre;
        private String tipo; // "servicio" o "pedido"
        private String medio;
        private BigDecimal monto;
        private String pagadoEn;
    }
}