package com.example.spapet.service;

import com.example.spapet.dto.CitasDTO;

import java.util.List;
import java.util.UUID;

public interface CitasService {

    List<CitasDTO> obtenerTodos();

    CitasDTO obtenerPorId(UUID id);

    CitasDTO crear(CitasDTO citasDTO);

    CitasDTO actualizar(UUID id, CitasDTO citasDTO);

    void eliminar(UUID id);

    List<CitasDTO> listarPorCorreo(String correo);
}