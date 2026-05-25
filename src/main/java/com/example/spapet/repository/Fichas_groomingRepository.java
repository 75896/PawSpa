package com.example.spapet.repository;

// Rewritten to resolve Maven bad source file issues
import com.example.spapet.model.Fichas_grooming;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface Fichas_groomingRepository extends JpaRepository<Fichas_grooming, UUID> {
    @Query("SELECT f FROM Fichas_grooming f WHERE f.groomers.id = :groomerId ORDER BY f.creadoEn DESC")
    List<Fichas_grooming> findByGroomersId(@Param("groomerId") UUID groomerId);

    @Query("SELECT f FROM Fichas_grooming f WHERE f.citas.id = :citaId")
    Optional<Fichas_grooming> findByCitasId(@Param("citaId") UUID citaId);
}