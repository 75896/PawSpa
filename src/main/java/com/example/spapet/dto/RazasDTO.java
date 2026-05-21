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
public class RazasDTO implements Serializable {

    private UUID id;

    @NotBlank(message = "El nombre de la raza es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    @NotBlank(message = "La especie es obligatoria")
    @Pattern(regexp = "^(perro|gato|conejo|ave|otro)$", message = "La especie debe ser 'perro', 'gato', 'conejo', 'ave' u 'otro'")
    private String especie;

    @Pattern(regexp = "^(pequeño|mediano|grande|gigante)$", message = "El tamaño debe ser 'pequeño', 'mediano', 'grande' o 'gigante'")
    private String tamanio;

    @NotNull(message = "El ajuste de duración es obligatorio")
    @Min(value = 0, message = "El ajuste de duración no puede ser negativo")
    private Short ajusteDuracion;
}