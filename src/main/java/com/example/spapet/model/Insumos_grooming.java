package com.example.spapet.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
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
@Table(name = "insumos_grooming")
public class Insumos_grooming {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ficha_id", nullable = false)
    private Fichas_grooming ficha;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variante_id", nullable = false)
    private Variantes_productos variante;

    @Column(nullable = false, precision = 8, scale = 3)
    @DecimalMin(value = "0.0", inclusive = false, message = "La cantidad debe ser mayor a 0")
    private BigDecimal cantidad;

    @Column(nullable = false, length = 20)
    @NotBlank(message = "La unidad no puede estar vacía")
    @Builder.Default
    private String unidad = "unidad";

    @Column(name = "registrado_en", nullable = false, updatable = false)
    private OffsetDateTime registradoEn;

    @PrePersist
    protected void onCreate() {
        this.registradoEn = OffsetDateTime.now();
    }
}