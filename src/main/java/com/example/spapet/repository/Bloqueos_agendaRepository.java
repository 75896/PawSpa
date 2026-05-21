package com.example.spapet.repository;

import com.example.spapet.model.Bloqueos_agenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface Bloqueos_agendaRepository extends JpaRepository<Bloqueos_agenda, UUID> {

    @Query("""
            SELECT b FROM Bloqueos_agenda b
            WHERE (b.groomers.id = :groomerId OR b.groomers IS NULL)
            AND b.fechaInicio < :fin
            AND b.fechaFin    > :inicio
            """)
    List<Bloqueos_agenda> findBloqueosSolapados(@Param("groomerId") UUID groomerId,
            @Param("inicio") OffsetDateTime inicio,
            @Param("fin") OffsetDateTime fin);

    List<Bloqueos_agenda> findByGroomersId(UUID groomersId);

    @Query("""
            SELECT b FROM Bloqueos_agenda b
            WHERE b.fechaInicio >= :inicio
            AND b.fechaInicio <= :fin
            ORDER BY b.fechaInicio ASC
            """)
    List<Bloqueos_agenda> findByRangoFechas(@Param("inicio") OffsetDateTime inicio,
            @Param("fin") OffsetDateTime fin);
}