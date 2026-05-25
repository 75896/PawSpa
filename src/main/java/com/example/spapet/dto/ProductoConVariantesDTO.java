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
public class ProductoConVariantesDTO {

    private UUID id;
    private String nombre;
    private String descripcion;
    private String imagenUrl;
    private BigDecimal precioBase;
    private UUID categoriaId;
    private String categoriaNombre;
    private List<Variantes_productosDTO> variantes;
}