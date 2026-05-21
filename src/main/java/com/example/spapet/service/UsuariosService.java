package com.example.spapet.service;

import com.example.spapet.dto.UsuariosDTO;

import java.util.List;
import java.util.UUID;

public interface UsuariosService {
    List<UsuariosDTO> listarTodos();

    UsuariosDTO obtenerPorId(UUID id);

    UsuariosDTO crear(UsuariosDTO dto);

    UsuariosDTO actualizar(UUID id, UsuariosDTO dto);

    void desactivar(UUID id);

    void activar(UUID id);

    List<UsuariosDTO> listarPorRol(String rol);
}