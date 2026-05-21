package com.example.spapet.service.impl;

import com.example.spapet.dto.Variantes_productosDTO;

import com.example.spapet.model.Productos;
import com.example.spapet.model.Variantes_productos;

import com.example.spapet.repository.ProductosRepository;
import com.example.spapet.repository.Variantes_productosRepository;

import com.example.spapet.service.Variantes_productosService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class Variantes_productosServiceImpl implements Variantes_productosService {

    private final Variantes_productosRepository variantesProductosRepository;
    private final ProductosRepository productosRepository;

    @Override
    public List<Variantes_productosDTO> obtenerTodos() {

        return variantesProductosRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Variantes_productosDTO obtenerPorId(UUID id) {

        Variantes_productos variante = variantesProductosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Variante no encontrada"));

        return convertToDTO(variante);
    }

    @Override
    public Variantes_productosDTO crear(Variantes_productosDTO dto) {

        Productos producto = productosRepository.findById(dto.getProductoId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        Variantes_productos variante = Variantes_productos.builder()
                .producto(producto)
                .tipoVariante(dto.getTipoVariante())
                .nombre(dto.getNombre())
                .sku(dto.getSku())
                .precio(dto.getPrecio())
                .stock(dto.getStock())
                .stockMinimo(dto.getStockMinimo())
                .nivelAlerta(dto.getNivelAlerta())
                .activa(dto.getActiva())
                .build();

        Variantes_productos varianteGuardada = variantesProductosRepository.save(variante);

        return convertToDTO(varianteGuardada);
    }

    @Override
    public Variantes_productosDTO actualizar(UUID id, Variantes_productosDTO dto) {

        Variantes_productos variante = variantesProductosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Variante no encontrada"));

        Productos producto = productosRepository.findById(dto.getProductoId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        variante.setProducto(producto);
        variante.setTipoVariante(dto.getTipoVariante());
        variante.setNombre(dto.getNombre());
        variante.setSku(dto.getSku());
        variante.setPrecio(dto.getPrecio());
        variante.setStock(dto.getStock());
        variante.setStockMinimo(dto.getStockMinimo());
        variante.setNivelAlerta(dto.getNivelAlerta());
        variante.setActiva(dto.getActiva());

        Variantes_productos varianteActualizada = variantesProductosRepository.save(variante);

        return convertToDTO(varianteActualizada);
    }

    @Override
    public void eliminar(UUID id) {

        Variantes_productos variante = variantesProductosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Variante no encontrada"));

        variantesProductosRepository.delete(variante);
    }

    // =========================
    // CONVERTERS
    // =========================

    private Variantes_productosDTO convertToDTO(Variantes_productos variante) {

        return Variantes_productosDTO.builder()
                .id(variante.getId())

                .productoId(variante.getProducto().getId())
                .productoNombre(variante.getProducto().getNombre())

                .tipoVariante(variante.getTipoVariante())
                .nombre(variante.getNombre())
                .sku(variante.getSku())
                .precio(variante.getPrecio())
                .stock(variante.getStock())
                .stockMinimo(variante.getStockMinimo())
                .nivelAlerta(variante.getNivelAlerta())
                .activa(variante.getActiva())

                .creadoEn(variante.getCreadoEn())
                .actualizadoEn(variante.getActualizadoEn())

                .build();
    }
}