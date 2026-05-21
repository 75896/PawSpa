package com.example.spapet.service;

import com.example.spapet.dto.RazasDTO;

import java.util.List;
import java.util.UUID;

public interface RazasService {

    List<RazasDTO> obtenerTodos();

    RazasDTO obtenerPorId(UUID id);

    RazasDTO crear(RazasDTO razasDTO);

    RazasDTO actualizar(UUID id, RazasDTO razasDTO);

    void eliminar(UUID id);
}