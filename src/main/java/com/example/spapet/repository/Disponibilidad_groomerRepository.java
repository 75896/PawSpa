package com.example.spapet.repository;

// Rewritten to resolve Maven bad source file issues
import com.example.spapet.model.Disponibilidad_groomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface Disponibilidad_groomerRepository extends JpaRepository<Disponibilidad_groomer, UUID> {
}