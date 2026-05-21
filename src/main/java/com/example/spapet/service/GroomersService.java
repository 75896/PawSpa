package com.example.spapet.service;

// Rewritten to resolve Maven bad source file issues
import com.example.spapet.dto.GroomersDTO;

import java.util.List;
import java.util.UUID;

public interface GroomersService {

    List<GroomersDTO> obtenerTodos();

    GroomersDTO obtenerPorId(UUID id);

    GroomersDTO crear(GroomersDTO groomersDTO);

    GroomersDTO actualizar(UUID id, GroomersDTO groomersDTO);

    void eliminar(UUID id);
}