package com.example.spapet.model;

import jakarta.persistence.*;
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
@Table(name = "auditoria_acceso", indexes = {
        @Index(name = "idx_auditoria_creado", columnList = "creado_en"),
        @Index(name = "idx_auditoria_correo", columnList = "correo")
})
public class Auditoria_acceso {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuarios usuario;

    @Column(length = 255)
    private String correo;

    @Column(nullable = false, length = 50)
    private String accion;

    @Column(nullable = false, length = 20)
    private String resultado;

    @Column(length = 45)
    private String ip;

    @Column(columnDefinition = "TEXT")
    private String navegador;

    @Column(columnDefinition = "TEXT")
    private String detalle;

    @Column(name = "creado_en", nullable = false, updatable = false)
    private OffsetDateTime creadoEn;

    @PrePersist
    protected void onCreate() {
        this.creadoEn = OffsetDateTime.now();
    }
}