package com.example.spapet.service;

import com.example.spapet.dto.EncuestasnpsDTO;

import java.util.List;
import java.util.UUID;

public interface EncuestasnpsService {

    List<EncuestasnpsDTO> obtenerTodos();

    EncuestasnpsDTO obtenerPorId(UUID id);

    EncuestasnpsDTO crear(EncuestasnpsDTO encuestasnpsDTO);

    EncuestasnpsDTO actualizar(UUID id, EncuestasnpsDTO encuestasnpsDTO);

    void eliminar(UUID id);
}