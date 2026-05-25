package com.example.spapet.service;

import com.example.spapet.dto.Insumos_groomingDTO;
import com.example.spapet.dto.ProductosDTO;

import java.util.List;
import java.util.UUID;

public interface Insumos_groomingService {

    List<Insumos_groomingDTO> listarPorFicha(UUID fichaId);

    Insumos_groomingDTO registrar(Insumos_groomingDTO dto);

    void eliminar(UUID id);

    List<ProductosDTO> listarProductosDisponibles();

    List<ProductosDTO> listarStockBajo();
}