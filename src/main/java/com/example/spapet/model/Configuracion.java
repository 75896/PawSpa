package com.example.spapet.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "configuracion")
public class Configuracion {

    @Id
    @Column(name = "clave", length = 100)
    @NotBlank(message = "La clave no puede estar vacía")
    private String clave;

    @Column(nullable = false, columnDefinition = "TEXT")
    @NotBlank(message = "El valor no puede estar vacío")
    private String valor;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "actualizado_en", nullable = false)
    private OffsetDateTime actualizadoEn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actualizado_por")
    private Usuarios actualizadoPor;

    @PrePersist
    protected void onCreate() {
        this.actualizadoEn = OffsetDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.actualizadoEn = OffsetDateTime.now();
    }
}