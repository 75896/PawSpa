package com.example.spapet.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "mascotas")
public class Mascotas {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Clientes clientes;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "El nombre de la mascota no puede estar vacío")
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "raza_id")
    private Razas razas;

    @Column(nullable = false, length = 50)
    @NotBlank(message = "La especie no puede estar vacía")
    @Builder.Default
    private String especie = "perro";

    @Column(name = "fecha_nac")
    private LocalDate fechaNac;

    @Column(nullable = false, length = 15)
    @Builder.Default
    private String sexo = "desconocido";

    @Column(name = "peso_kg", precision = 5, scale = 2)
    @DecimalMin(value = "0.0", inclusive = false, message = "El peso debe ser mayor a 0")
    private BigDecimal pesoKg;

    @Column(name = "color_pelaje", length = 100)
    private String colorPelaje;

    @Column(nullable = false, length = 20)
    @Builder.Default
    private String temperamento = "desconocido";

    @Column(columnDefinition = "TEXT")
    private String alergias;

    @Column(columnDefinition = "TEXT")
    private String restricciones;

    @Column(nullable = false)
    @Builder.Default
    private Boolean activa = true;

    @Column(name = "foto_url", columnDefinition = "TEXT")
    private String fotoUrl;

    @Column(name = "creado_en", nullable = false, updatable = false)
    private OffsetDateTime creadoEn;

    @Column(name = "actualizado_en", nullable = false)
    private OffsetDateTime actualizadoEn;

    @PrePersist
    protected void onCreate() {
        this.creadoEn = OffsetDateTime.now();
        this.actualizadoEn = OffsetDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.actualizadoEn = OffsetDateTime.now();
    }
}