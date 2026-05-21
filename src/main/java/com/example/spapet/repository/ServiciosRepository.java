package com.example.spapet.repository;

import org.springframework.stereotype.Repository;
import com.example.spapet.model.Servicios;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ServiciosRepository extends JpaRepository<Servicios, UUID> {
    List<Servicios> findByActivoTrue();
}