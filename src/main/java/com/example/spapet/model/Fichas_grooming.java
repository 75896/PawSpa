package com.example.spapet.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "fichas_grooming")
public class Fichas_grooming {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cita_id", nullable = false, unique = true)
    private Citas citas;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "groomer_id", nullable = false)
    private Groomers groomers;

    @Column(nullable = false, length = 20)
    @Builder.Default
    private String estado = "abierta";

    @Column(name = "tiene_nudos", nullable = false)
    @Builder.Default
    private Boolean tieneNudos = false;

    @Column(name = "tiene_pulgas", nullable = false)
    @Builder.Default
    private Boolean tienePulgas = false;

    @Column(name = "tiene_heridas", nullable = false)
    @Builder.Default
    private Boolean tieneHeridas = false;

    @Column(name = "nivel_nudos", length = 20)
    private String nivelNudos;

    @Column(name = "observaciones_ingreso", columnDefinition = "TEXT")
    private String observacionesIngreso;

    @Column(name = "observaciones_salida", columnDefinition = "TEXT")
    private String observacionesSalida;

    @Column(columnDefinition = "TEXT")
    private String recomendaciones;

    @Column(name = "peso_kg_actual", precision = 5, scale = 2)
    @DecimalMin(value = "0.0", inclusive = false, message = "El peso debe ser mayor a 0")
    private BigDecimal pesoKgActual;

    @Column(name = "abierta_en", nullable = false, updatable = false)
    private OffsetDateTime abiertaEn;

    @Column(name = "cerrada_en")
    private OffsetDateTime cerradaEn;

    @Column(name = "creado_en", nullable = false, updatable = false)
    private OffsetDateTime creadoEn;

    @Column(name = "actualizado_en", nullable = false)
    private OffsetDateTime actualizadoEn;

    @PrePersist
    protected void onCreate() {
        this.abiertaEn = OffsetDateTime.now();
        this.creadoEn = OffsetDateTime.now();
        this.actualizadoEn = OffsetDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.actualizadoEn = OffsetDateTime.now();
    }
}