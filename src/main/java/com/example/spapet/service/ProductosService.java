package com.example.spapet.service;

import com.example.spapet.dto.ProductosDTO;

import java.util.List;
import java.util.UUID;

public interface ProductosService {

    List<ProductosDTO> obtenerTodos();

    ProductosDTO obtenerPorId(UUID id);

    ProductosDTO crear(ProductosDTO productosDTO);

    ProductosDTO actualizar(UUID id, ProductosDTO productosDTO);

    void eliminar(UUID id);
}