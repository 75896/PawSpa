package com.example.spapet.service;

import com.example.spapet.dto.FacturasDTO;
import com.example.spapet.dto.ReciboDTO;

import java.util.List;
import java.util.UUID;

public interface FacturasService {

    List<FacturasDTO> obtenerTodos();

    FacturasDTO obtenerPorId(UUID id);

    FacturasDTO crear(FacturasDTO dto);

    FacturasDTO actualizar(UUID id, FacturasDTO dto);

    void eliminar(UUID id);

    FacturasDTO generarDesdeCita(UUID citaId);

    FacturasDTO generarDesdePedido(UUID pedidoId);

    ReciboDTO generarRecibo(UUID facturaId);
}