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
public class CitasDTO implements Serializable {

    private UUID id;

    @NotNull(message = "La mascota es obligatoria")
    private UUID mascotaId;

    private String mascotaNombre;
    private String mascotaEspecie;
    private String mascotaTemperamento;

    @NotNull(message = "El groomer es obligatorio")
    private UUID groomerId;

    private String groomerNombre;
    private String groomerApellido;

    @NotNull(message = "El servicio es obligatorio")
    private UUID servicioId;

    private String servicioNombre;
    private Short servicioDuracionMin;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private OffsetDateTime fechaInicio;

    private OffsetDateTime fechaFin;

    @NotBlank(message = "El estado es obligatorio")
    @Pattern(regexp = "^(pendiente|confirmada|en_proceso|completada|cancelada)$", message = "El estado debe ser pendiente, confirmada, en_proceso, completada o cancelada")
    private String estado;

    @DecimalMin(value = "0.0", inclusive = true, message = "El precio acordado no puede ser negativo")
    private BigDecimal precioAcordado;

    @Size(max = 1000, message = "Las notas del cliente no pueden tener más de 1000 caracteres")
    private String notasCliente;

    @Size(max = 1000, message = "El motivo de cancelación no puede tener más de 1000 caracteres")
    private String cancelacionMotivo;

    private UUID agendadoPorId;
    private String agendadoPorNombre;

    private OffsetDateTime creadoEn;
    private OffsetDateTime actualizadoEn;
}