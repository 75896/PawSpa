package com.example.spapet.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CobrarDTO {

    @NotNull(message = "La factura es obligatoria")
    private UUID facturaId;

    @NotBlank(message = "El medio de pago es obligatorio")
    @Pattern(regexp = "^(efectivo|tarjeta|transferencia|qr)$")
    private String medio;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
    private BigDecimal monto;

    private String referencia;
}