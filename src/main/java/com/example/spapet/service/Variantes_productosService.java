package com.example.spapet.service;

import com.example.spapet.dto.Variantes_productosDTO;

import java.util.List;
import java.util.UUID;

public interface Variantes_productosService {

    List<Variantes_productosDTO> obtenerTodos();

    Variantes_productosDTO obtenerPorId(UUID id);

    Variantes_productosDTO crear(Variantes_productosDTO variantesProductosDTO);

    Variantes_productosDTO actualizar(UUID id, Variantes_productosDTO variantesProductosDTO);

    void eliminar(UUID id);
}