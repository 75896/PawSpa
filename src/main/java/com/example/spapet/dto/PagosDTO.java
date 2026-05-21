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
public class PagosDTO implements Serializable {

    private UUID id;

    @NotNull(message = "La factura es obligatoria")
    private UUID facturaId;

    private String facturaNumero;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El monto debe ser mayor a 0")
    private BigDecimal monto;

    @NotBlank(message = "El medio de pago es obligatorio")
    @Pattern(regexp = "^(efectivo|tarjeta|transferencia|qr)$", message = "El medio debe ser efectivo, tarjeta, transferencia o qr")
    @Size(max = 20, message = "El medio no puede tener más de 20 caracteres")
    private String medio;

    @Size(max = 200, message = "La referencia no puede tener más de 200 caracteres")
    private String referencia;

    @NotBlank(message = "El estado es obligatorio")
    @Pattern(regexp = "^(pagado|pendiente|fallido|reembolsado)$", message = "El estado debe ser pagado, pendiente, fallido o reembolsado")
    @Size(max = 20, message = "El estado no puede tener más de 20 caracteres")
    private String estado;

    private UUID registradoPorId;

    private String registradoPorNombre;

    private OffsetDateTime pagadoEn;
}