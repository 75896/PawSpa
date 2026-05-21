package com.example.spapet.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "razas")
public class Razas {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, unique = true, length = 100)
    @NotBlank(message = "El nombre de la raza no puede estar vacío")
    private String nombre;

    @Column(nullable = false, length = 50)
    @NotBlank(message = "La especie no puede estar vacía")
    @Builder.Default
    private String especie = "perro";

    @Column(length = 20)
    private String tamanio;

    @Column(name = "ajuste_duracion", nullable = false)
    @Min(value = 0, message = "El ajuste de duración no puede ser negativo")
    @Builder.Default
    private Short ajusteDuracion = 0;
}