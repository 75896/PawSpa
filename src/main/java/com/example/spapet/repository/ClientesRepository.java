package com.example.spapet.repository;

import com.example.spapet.model.Clientes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ClientesRepository extends JpaRepository<Clientes, UUID> {
    Optional<Clientes> findByUsuariosId(UUID usuariosId);
}