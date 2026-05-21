package com.example.spapet.repository;

import com.example.spapet.model.Auditoria_acceso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface Auditoria_accesoRepository extends JpaRepository<Auditoria_acceso, UUID> {

    List<Auditoria_acceso> findAllByOrderByCreadoEnDesc();

    List<Auditoria_acceso> findByCorreoOrderByCreadoEnDesc(String correo);

    @Query("""
            SELECT a FROM Auditoria_acceso a
            WHERE a.creadoEn >= :inicio
            ORDER BY a.creadoEn DESC
            """)
    List<Auditoria_acceso> findByRangoFechas(@Param("inicio") OffsetDateTime inicio);

    List<Auditoria_acceso> findByAccionOrderByCreadoEnDesc(String accion);

    List<Auditoria_acceso> findByResultadoOrderByCreadoEnDesc(String resultado);
}