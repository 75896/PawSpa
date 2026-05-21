package com.example.spapet.service;

import com.example.spapet.dto.Items_pedidoDTO;

import java.util.List;
import java.util.UUID;

public interface Items_pedidoService {

    List<Items_pedidoDTO> obtenerTodos();

    Items_pedidoDTO obtenerPorId(UUID id);

    Items_pedidoDTO crear(Items_pedidoDTO itemsPedidoDTO);

    Items_pedidoDTO actualizar(UUID id, Items_pedidoDTO itemsPedidoDTO);

    void eliminar(UUID id);
}