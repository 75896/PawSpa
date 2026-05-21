package com.example.spapet.repository;

import com.example.spapet.model.Citas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface CitasRepository extends JpaRepository<Citas, UUID> {

        @Query("SELECT c FROM Citas c WHERE c.mascotas.clientes.id = :clienteId")
        List<Citas> findByMascotaClienteId(@Param("clienteId") UUID clienteId);

        @Query("SELECT c FROM Citas c WHERE c.groomers.id = :groomerId ORDER BY c.fechaInicio ASC")
        List<Citas> findByGroomersId(@Param("groomerId") UUID groomerId);

        @Query("""
                        SELECT c FROM Citas c
                        WHERE c.groomers.id = :groomerId
                        AND c.fechaInicio >= :inicio
                        AND c.fechaInicio < :fin
                        ORDER BY c.fechaInicio ASC
                        """)
        List<Citas> findByGroomersIdAndFecha(@Param("groomerId") UUID groomerId,
                        @Param("inicio") OffsetDateTime inicio,
                        @Param("fin") OffsetDateTime fin);

        @Query("""
                        SELECT c FROM Citas c
                        WHERE c.fechaInicio >= :inicio
                        AND c.fechaInicio < :fin
                        ORDER BY c.fechaInicio ASC
                        """)
        List<Citas> findByFechaInicioBetween(@Param("inicio") OffsetDateTime inicio,
                        @Param("fin") OffsetDateTime fin);

        @Query("""
                        SELECT c FROM Citas c
                        WHERE c.groomers.id  = :groomerId
                        AND c.estado NOT IN ('cancelada', 'completada')
                        AND c.fechaInicio     < :fin
                        AND c.fechaFin        > :inicio
                        """)
        List<Citas> findSolapadas(@Param("groomerId") UUID groomerId,
                        @Param("inicio") OffsetDateTime inicio,
                        @Param("fin") OffsetDateTime fin);
}