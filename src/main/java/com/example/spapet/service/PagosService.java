package com.example.spapet.service;

import com.example.spapet.dto.PagosDTO;

import java.util.List;
import java.util.UUID;

public interface PagosService {

    List<PagosDTO> obtenerTodos();

    PagosDTO obtenerPorId(UUID id);

    PagosDTO crear(PagosDTO pagosDTO);

    PagosDTO actualizar(UUID id, PagosDTO pagosDTO);

    void eliminar(UUID id);
}