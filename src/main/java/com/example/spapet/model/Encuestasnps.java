package com.example.spapet.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
@Table(name = "encuestas_nps")
public class Encuestasnps {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cita_id", nullable = false, unique = true)
    private Citas citas;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Clientes clientes;

    @Column(nullable = false)
    @Min(value = 1, message = "La puntuación mínima es 1")
    @Max(value = 10, message = "La puntuación máxima es 10")
    private Short puntuacion;

    @Column(columnDefinition = "TEXT")
    private String comentario;

    @Column(name = "respondida_en", nullable = false, updatable = false)
    private OffsetDateTime respondidaEn;

    @PrePersist
    protected void onCreate() {
        this.respondidaEn = OffsetDateTime.now();
    }
}