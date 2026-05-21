package com.example.spapet.repository;

import java.util.Optional;
import org.springframework.stereotype.Repository;
import com.example.spapet.model.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.*;

@Repository
public interface UsuariosRepository extends JpaRepository<Usuarios, UUID> {
    Optional<Usuarios> findByCorreo(String correo);

    boolean existsByCorreo(String correo);

    List<Usuarios> findByRol(String rol);
}