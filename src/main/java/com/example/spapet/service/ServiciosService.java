package com.example.spapet.service;

import com.example.spapet.dto.ServiciosDTO;

import java.util.List;
import java.util.UUID;

public interface ServiciosService {

    List<ServiciosDTO> obtenerTodos();

    ServiciosDTO obtenerPorId(UUID id);

    ServiciosDTO crear(ServiciosDTO serviciosDTO);

    ServiciosDTO actualizar(UUID id, ServiciosDTO serviciosDTO);

    void eliminar(UUID id);
}