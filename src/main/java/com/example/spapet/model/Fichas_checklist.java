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
@Table(name = "fichas_checklist", uniqueConstraints = @UniqueConstraint(name = "uk_ficha_item", columnNames = {
        "ficha_id", "item_id" }))
public class Fichas_checklist {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ficha_id", nullable = false)
    private Fichas_grooming ficha;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Checklist_items item;

    @Column(nullable = false)
    @Builder.Default
    private Boolean realizado = false;

    @Column(columnDefinition = "TEXT")
    private String observacion;

    @Column(name = "registrado_en", nullable = false, updatable = false)
    private OffsetDateTime registradoEn;

    @PrePersist
    protected void onCreate() {
        this.registradoEn = OffsetDateTime.now();
    }
}