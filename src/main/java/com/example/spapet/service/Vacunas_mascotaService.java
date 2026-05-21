package com.example.spapet.service;

import com.example.spapet.dto.Vacunas_mascotaDTO;

import java.util.List;
import java.util.UUID;

public interface Vacunas_mascotaService {

    List<Vacunas_mascotaDTO> obtenerTodos();

    Vacunas_mascotaDTO obtenerPorId(UUID id);

    Vacunas_mascotaDTO crear(Vacunas_mascotaDTO vacunasMascotaDTO);

    Vacunas_mascotaDTO actualizar(UUID id, Vacunas_mascotaDTO vacunasMascotaDTO);

    void eliminar(UUID id);
}