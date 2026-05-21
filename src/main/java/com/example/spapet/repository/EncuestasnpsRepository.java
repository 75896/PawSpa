package com.example.spapet.repository;

import org.springframework.stereotype.Repository;
import com.example.spapet.model.Encuestasnps;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

@Repository
public interface EncuestasnpsRepository extends JpaRepository<Encuestasnps, UUID> {
}