package com.example.spapet.service;

// Rewritten to resolve Maven bad source file issues
import com.example.spapet.dto.Disponibilidad_groomerDTO;

import java.util.List;
import java.util.UUID;

public interface Disponibilidad_groomerService {

    List<Disponibilidad_groomerDTO> obtenerTodos();

    Disponibilidad_groomerDTO obtenerPorId(UUID id);

    Disponibilidad_groomerDTO crear(Disponibilidad_groomerDTO disponibilidadGroomerDTO);

    Disponibilidad_groomerDTO actualizar(UUID id, Disponibilidad_groomerDTO disponibilidadGroomerDTO);

    void eliminar(UUID id);
}