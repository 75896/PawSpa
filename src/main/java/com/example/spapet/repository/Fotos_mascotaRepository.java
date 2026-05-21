package com.example.spapet.repository;

// Rewritten to resolve Maven bad source file issues
import com.example.spapet.model.Fotos_mascota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface Fotos_mascotaRepository extends JpaRepository<Fotos_mascota, UUID> {
}