package com.example.spapet.service;

import com.example.spapet.dto.Fichas_groomingDTO;

import java.util.List;
import java.util.UUID;

public interface Fichas_groomingService {

    List<Fichas_groomingDTO> obtenerTodos();

    Fichas_groomingDTO obtenerPorId(UUID id);

    Fichas_groomingDTO crear(Fichas_groomingDTO fichasGroomingDTO);

    Fichas_groomingDTO actualizar(UUID id, Fichas_groomingDTO fichasGroomingDTO);

    void eliminar(UUID id);
}