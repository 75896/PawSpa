package com.example.spapet.service;

import com.example.spapet.dto.PedidosDTO;

import java.util.List;
import java.util.UUID;

public interface PedidosService {

    List<PedidosDTO> obtenerTodos();

    PedidosDTO obtenerPorId(UUID id);

    PedidosDTO crear(PedidosDTO pedidosDTO);

    PedidosDTO actualizar(UUID id, PedidosDTO pedidosDTO);

    void eliminar(UUID id);
}