package com.example.spapet.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "notificaciones", indexes = {
        @Index(name = "idx_notificaciones_estado", columnList = "estado, programada_para"),
        @Index(name = "idx_notificaciones_usuario", columnList = "usuario_id")
})
public class Notificaciones {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuarios usuarios;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cita_id")
    private Citas citas;

    @Column(nullable = false, length = 30)
    @NotBlank(message = "El tipo de notificación no puede estar vacío")
    private String tipo;

    @Column(nullable = false, length = 20)
    @NotBlank(message = "El canal no puede estar vacío")
    private String canal;

    @Column(nullable = false, length = 20)
    @Builder.Default
    private String estado = "pendiente";

    @Column(length = 200)
    private String asunto;

    @Column(columnDefinition = "TEXT")
    private String cuerpo;

    @Column(name = "programada_para", nullable = false)
    private OffsetDateTime programadaPara;

    @Column(name = "enviada_en")
    private OffsetDateTime enviadaEn;

    @Column(name = "error_detalle", columnDefinition = "TEXT")
    private String errorDetalle;

    @Column(name = "creado_en", nullable = false, updatable = false)
    private OffsetDateTime creadoEn;

    @PrePersist
    protected void onCreate() {
        this.creadoEn = OffsetDateTime.now();
        if (this.programadaPara == null) {
            this.programadaPara = OffsetDateTime.now();
        }
    }
}