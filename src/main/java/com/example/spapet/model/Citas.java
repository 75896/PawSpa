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
@Table(name = "citas", indexes = {
        @Index(name = "idx_citas_groomer_fecha", columnList = "groomer_id, fecha_inicio"),
        @Index(name = "idx_citas_mascota", columnList = "mascota_id"),
        @Index(name = "idx_citas_estado", columnList = "estado")
})
public class Citas {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mascota_id", nullable = false)
    private Mascotas mascotas;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "groomer_id", nullable = false)
    private Groomers groomers;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "servicio_id", nullable = false)
    private Servicios servicios;

    @Column(name = "fecha_inicio", nullable = false)
    private OffsetDateTime fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private OffsetDateTime fechaFin;

    @Column(nullable = false, length = 20)
    @Builder.Default
    private String estado = "pendiente";

    @Column(name = "precio_acordado", precision = 10, scale = 2)
    @DecimalMin(value = "0.0", inclusive = true, message = "El precio acordado no puede ser negativo")
    private BigDecimal precioAcordado;

    @Column(name = "notas_cliente", columnDefinition = "TEXT")
    private String notasCliente;

    @Column(name = "cancelacion_motivo", columnDefinition = "TEXT")
    private String cancelacionMotivo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agendado_por")
    private Usuarios agendadoPor;

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