package com.example.spapet.service;

import com.example.spapet.dto.Categorias_productoDTO;

import java.util.List;
import java.util.UUID;

public interface Categorias_productoService {

    List<Categorias_productoDTO> obtenerTodos();

    Categorias_productoDTO obtenerPorId(UUID id);

    Categorias_productoDTO crear(Categorias_productoDTO categoriasProductoDTO);

    Categorias_productoDTO actualizar(UUID id, Categorias_productoDTO categoriasProductoDTO);

    void eliminar(UUID id);
}