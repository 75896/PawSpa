package com.example.spapet.service;

import com.example.spapet.dto.Fotos_mascotaDTO;

import java.util.List;
import java.util.UUID;

public interface Fotos_mascotaService {

    List<Fotos_mascotaDTO> obtenerTodos();

    Fotos_mascotaDTO obtenerPorId(UUID id);

    Fotos_mascotaDTO crear(Fotos_mascotaDTO fotosMascotaDTO);

    Fotos_mascotaDTO actualizar(UUID id, Fotos_mascotaDTO fotosMascotaDTO);

    void eliminar(UUID id);
}