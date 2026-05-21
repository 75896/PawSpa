package com.example.spapet.repository;

import com.example.spapet.model.Groomers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
import java.util.*;

@Repository
public interface GroomersRepository extends JpaRepository<Groomers, UUID> {
    Optional<Groomers> findByUsuariosId(UUID usuariosId);

    List<Groomers> findByActivoTrue();
}