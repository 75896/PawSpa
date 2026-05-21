package com.example.spapet.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfiguracionDTO implements Serializable {

    @NotBlank(message = "La clave no puede estar vacía")
    @Size(max = 100, message = "La clave no puede tener más de 100 caracteres")
    private String clave;

    @NotBlank(message = "El valor no puede estar vacío")
    private String valor;

    @Size(max = 1000, message = "La descripción no puede tener más de 1000 caracteres")
    private String descripcion;

    private OffsetDateTime actualizadoEn;

    private UUID actualizadoPorId;

    private String actualizadoPorNombre;
}