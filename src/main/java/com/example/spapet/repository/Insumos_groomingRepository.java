package com.example.spapet.repository;

import com.example.spapet.model.Insumos_grooming;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface Insumos_groomingRepository extends JpaRepository<Insumos_grooming, UUID> {
    List<Insumos_grooming> findByFichaId(UUID fichaId);
}