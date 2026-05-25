package com.example.spapet.service;

import com.example.spapet.dto.Variantes_productosDTO;

import java.util.List;
import java.util.UUID;

public interface InventarioService {
    List<Variantes_productosDTO> listarVariantes();

    List<Variantes_productosDTO> listarStockBajo();

    Variantes_productosDTO reponer(UUID id, Integer cantidad);

    Variantes_productosDTO actualizarStockMinimo(UUID id, Integer stockMinimo);
}