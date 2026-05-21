package com.example.spapet.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Checklist_itemsDTO implements Serializable {

    private UUID id;

    @NotBlank(message = "El nombre del item no puede estar vacío")
    @Size(max = 100, message = "El nombre no puede tener más de 100 caracteres")
    private String nombre;

    @Size(max = 1000, message = "La descripción no puede tener más de 1000 caracteres")
    private String descripcion;

    @NotNull(message = "El campo obligatorio es requerido")
    private Boolean obligatorio;

    @NotNull(message = "El orden es obligatorio")
    @Min(value = 0, message = "El orden no puede ser negativo")
    private Short orden;
}