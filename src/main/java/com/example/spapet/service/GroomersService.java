package com.example.spapet.service;

import com.example.spapet.dto.CitasDTO;
import com.example.spapet.dto.GroomersDTO;

import java.util.List;
import java.util.UUID;

public interface GroomersService {
    List<GroomersDTO> obtenerTodos();

    GroomersDTO obtenerPorId(UUID id);

    GroomersDTO crear(GroomersDTO groomersDTO);

    GroomersDTO actualizar(UUID id, GroomersDTO groomersDTO);

    void eliminar(UUID id);

    List<CitasDTO> listarCitasPorCorreo(String correo);

    List<CitasDTO> listarCitasHoy(String correo);
}