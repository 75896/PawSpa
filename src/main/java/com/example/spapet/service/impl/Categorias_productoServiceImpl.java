package com.example.spapet.service.impl;

import com.example.spapet.dto.Categorias_productoDTO;
import com.example.spapet.model.Categorias_producto;
import com.example.spapet.repository.Categorias_productoRepository;
import com.example.spapet.service.Categorias_productoService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class Categorias_productoServiceImpl implements Categorias_productoService {

    private final Categorias_productoRepository categoriasProductoRepository;

    @Override
    public List<Categorias_productoDTO> obtenerTodos() {

        return categoriasProductoRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Categorias_productoDTO obtenerPorId(UUID id) {

        Categorias_producto categoria = categoriasProductoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        return convertToDTO(categoria);
    }

    @Override
    public Categorias_productoDTO crear(Categorias_productoDTO dto) {

        Categorias_producto categoria = Categorias_producto.builder()
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .imagenUrl(dto.getImagenUrl())
                .activa(dto.getActiva())
                .build();

        Categorias_producto categoriaGuardada = categoriasProductoRepository.save(categoria);

        return convertToDTO(categoriaGuardada);
    }

    @Override
    public Categorias_productoDTO actualizar(UUID id, Categorias_productoDTO dto) {

        Categorias_producto categoria = categoriasProductoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        categoria.setNombre(dto.getNombre());
        categoria.setDescripcion(dto.getDescripcion());
        categoria.setImagenUrl(dto.getImagenUrl());
        categoria.setActiva(dto.getActiva());

        Categorias_producto categoriaActualizada = categoriasProductoRepository.save(categoria);

        return convertToDTO(categoriaActualizada);
    }

    @Override
    public void eliminar(UUID id) {

        Categorias_producto categoria = categoriasProductoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        categoriasProductoRepository.delete(categoria);
    }

    // =========================
    // CONVERTERS
    // =========================

    private Categorias_productoDTO convertToDTO(Categorias_producto categoria) {

        return Categorias_productoDTO.builder()
                .id(categoria.getId())
                .nombre(categoria.getNombre())
                .descripcion(categoria.getDescripcion())
                .imagenUrl(categoria.getImagenUrl())
                .activa(categoria.getActiva())
                .build();
    }
}