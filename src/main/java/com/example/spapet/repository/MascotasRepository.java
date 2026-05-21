package com.example.spapet.repository;

import com.example.spapet.dto.MascotasDTO;
import com.example.spapet.model.Mascotas;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MascotasRepository extends JpaRepository<Mascotas, UUID> {
    List<Mascotas> findByClientesId(UUID clientesId);

    // List<MascotasDTO> listarPorClienteId(UUID clienteId);
}