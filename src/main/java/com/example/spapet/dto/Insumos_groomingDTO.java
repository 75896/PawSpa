package com.example.spapet.dto;

import lombok.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Insumos_groomingDTO implements Serializable {
    private UUID id;
    private UUID fichaId;
    private UUID varianteId;
    private String varianteNombre;
    private String productoNombre;
    private String unidad;
    private BigDecimal cantidad;
    private Integer stockDisponible;
    private OffsetDateTime registradoEn;
}