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
public class FacturasDTO implements Serializable {

    private UUID id;

    @NotBlank(message = "El número de factura no puede estar vacío")
    @Size(max = 50, message = "El número no puede tener más de 50 caracteres")
    private String numero;

    @NotNull(message = "El cliente es obligatorio")
    private UUID clienteId;

    private String clienteNombre;

    private UUID citaId;

    private UUID pedidoId;

    @NotNull(message = "El subtotal es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El subtotal no puede ser negativo")
    private BigDecimal subtotal;

    @NotNull(message = "El descuento es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El descuento no puede ser negativo")
    private BigDecimal descuento;

    @NotNull(message = "Los impuestos son obligatorios")
    @DecimalMin(value = "0.0", inclusive = true, message = "Los impuestos no pueden ser negativos")
    private BigDecimal impuestos;

    @NotNull(message = "El total es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El total no puede ser negativo")
    private BigDecimal total;

    @NotBlank(message = "El estado es obligatorio")
    @Pattern(regexp = "^(pendiente|pagada|cancelada|vencida)$", message = "El estado debe ser pendiente, pagada, cancelada o vencida")
    private String estado;

    @Size(max = 1000, message = "Las notas no pueden tener más de 1000 caracteres")
    private String notas;

    private OffsetDateTime emitidaEn;

    private OffsetDateTime venceEn;

    private OffsetDateTime creadoEn;
}