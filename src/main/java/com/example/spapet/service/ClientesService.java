package com.example.spapet.service;

import com.example.spapet.dto.ClientesDTO;

import java.util.List;
import java.util.UUID;

public interface ClientesService {

    List<ClientesDTO> obtenerTodos();

    ClientesDTO obtenerPorId(UUID id);

    ClientesDTO crear(ClientesDTO clientesDTO);

    ClientesDTO actualizar(UUID id, ClientesDTO clientesDTO);

    void eliminar(UUID id);
}