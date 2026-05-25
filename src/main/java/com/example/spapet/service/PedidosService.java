package com.example.spapet.service;

import com.example.spapet.dto.*;
import java.util.List;
import java.util.UUID;

public interface PedidosService {

    // CRUD existente
    List<PedidosDTO> obtenerTodos();

    PedidosDTO obtenerPorId(UUID id);

    PedidosDTO crear(PedidosDTO dto);

    PedidosDTO actualizar(UUID id, PedidosDTO dto);

    void eliminar(UUID id);

    // Tienda
    CarritoDTO crearCarrito(String correo);

    CarritoDTO obtenerCarrito(String correo);

    List<PedidosDTO> obtenerMisPedidos(String correo);

    CarritoDTO agregarItem(String correo, UUID pedidoId, AgregarItemDTO dto);

    CarritoDTO actualizarItem(String correo, UUID pedidoId, UUID itemId, Integer cantidad);

    CarritoDTO eliminarItem(String correo, UUID pedidoId, UUID itemId);

    PedidosDTO confirmarPedido(String correo, UUID pedidoId);

    MensajePedidoDTO generarMensaje(String correo, UUID pedidoId);
}