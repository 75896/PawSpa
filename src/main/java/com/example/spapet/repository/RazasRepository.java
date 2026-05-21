package com.example.spapet.repository;

import org.springframework.stereotype.Repository;
import com.example.spapet.model.Razas;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

@Repository
public interface RazasRepository extends JpaRepository<Razas, UUID> {
}