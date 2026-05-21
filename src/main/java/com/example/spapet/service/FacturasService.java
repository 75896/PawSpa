package com.example.spapet.service;

import com.example.spapet.dto.FacturasDTO;

import java.util.List;
import java.util.UUID;

public interface FacturasService {

    List<FacturasDTO> obtenerTodos();

    FacturasDTO obtenerPorId(UUID id);

    FacturasDTO crear(FacturasDTO facturasDTO);

    FacturasDTO actualizar(UUID id, FacturasDTO facturasDTO);

    void eliminar(UUID id);
}