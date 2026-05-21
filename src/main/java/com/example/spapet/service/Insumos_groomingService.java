package com.example.spapet.service;

import com.example.spapet.dto.Insumos_groomingDTO;

import java.util.List;
import java.util.UUID;

public interface Insumos_groomingService {

    List<Insumos_groomingDTO> obtenerTodos();

    Insumos_groomingDTO obtenerPorId(UUID id);

    Insumos_groomingDTO crear(Insumos_groomingDTO insumosGroomingDTO);

    Insumos_groomingDTO actualizar(UUID id, Insumos_groomingDTO insumosGroomingDTO);

    void eliminar(UUID id);
}