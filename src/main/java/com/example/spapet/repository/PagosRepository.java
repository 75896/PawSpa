package com.example.spapet.repository;

import com.example.spapet.model.Pagos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface PagosRepository extends JpaRepository<Pagos, UUID> {

    List<Pagos> findByPagadoEnBetween(OffsetDateTime desde, OffsetDateTime hasta);

    @Query("SELECT p FROM Pagos p WHERE p.factura.id = :facturaId")
    List<Pagos> findByFacturaId(@Param("facturaId") UUID facturaId);
}