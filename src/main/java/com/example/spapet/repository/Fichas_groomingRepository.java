package com.example.spapet.repository;

// Rewritten to resolve Maven bad source file issues
import com.example.spapet.model.Fichas_grooming;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface Fichas_groomingRepository extends JpaRepository<Fichas_grooming, UUID> {
}