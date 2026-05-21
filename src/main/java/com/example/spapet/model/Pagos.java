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
@Table(name = "pagos")
public class Pagos {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "factura_id", nullable = false)
    private Facturas factura;

    @Column(nullable = false, precision = 12, scale = 2)
    @DecimalMin(value = "0.0", inclusive = false, message = "El monto debe ser mayor a 0")
    private BigDecimal monto;

    @Column(nullable = false, length = 20)
    @Builder.Default
    private String medio = "efectivo";

    @Column(length = 200)
    private String referencia;

    @Column(nullable = false, length = 20)
    @Builder.Default
    private String estado = "pagado";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registrado_por")
    private Usuarios registradoPor;

    @Column(name = "pagado_en", nullable = false, updatable = false)
    private OffsetDateTime pagadoEn;

    @PrePersist
    protected void onCreate() {
        this.pagadoEn = OffsetDateTime.now();
    }
}