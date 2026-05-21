package com.example.spapet.service.impl;

import com.example.spapet.dto.Insumos_groomingDTO;

import com.example.spapet.model.Fichas_grooming;
import com.example.spapet.model.Insumos_grooming;
import com.example.spapet.model.Variantes_productos;

import com.example.spapet.repository.Fichas_groomingRepository;
import com.example.spapet.repository.Insumos_groomingRepository;
import com.example.spapet.repository.Variantes_productosRepository;

import com.example.spapet.service.Insumos_groomingService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class Insumos_groomingServiceImpl implements Insumos_groomingService {

    private final Insumos_groomingRepository insumosGroomingRepository;
    private final Fichas_groomingRepository fichasGroomingRepository;
    private final Variantes_productosRepository variantesProductosRepository;

    @Override
    public List<Insumos_groomingDTO> obtenerTodos() {

        return insumosGroomingRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Insumos_groomingDTO obtenerPorId(UUID id) {

        Insumos_grooming insumo = insumosGroomingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Insumo grooming no encontrado"));

        return convertToDTO(insumo);
    }

    @Override
    public Insumos_groomingDTO crear(Insumos_groomingDTO dto) {

        Fichas_grooming ficha = fichasGroomingRepository.findById(dto.getFichaId())
                .orElseThrow(() -> new RuntimeException("Ficha grooming no encontrada"));

        Variantes_productos variante = variantesProductosRepository.findById(dto.getVarianteId())
                .orElseThrow(() -> new RuntimeException("Variante no encontrada"));

        Insumos_grooming insumo = Insumos_grooming.builder()
                .ficha(ficha)
                .variante(variante)
                .cantidad(dto.getCantidad())
                .unidad(dto.getUnidad())
                .build();

        Insumos_grooming insumoGuardado = insumosGroomingRepository.save(insumo);

        return convertToDTO(insumoGuardado);
    }

    @Override
    public Insumos_groomingDTO actualizar(UUID id, Insumos_groomingDTO dto) {

        Insumos_grooming insumo = insumosGroomingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Insumo grooming no encontrado"));

        Fichas_grooming ficha = fichasGroomingRepository.findById(dto.getFichaId())
                .orElseThrow(() -> new RuntimeException("Ficha grooming no encontrada"));

        Variantes_productos variante = variantesProductosRepository.findById(dto.getVarianteId())
                .orElseThrow(() -> new RuntimeException("Variante no encontrada"));

        insumo.setFicha(ficha);
        insumo.setVariante(variante);
        insumo.setCantidad(dto.getCantidad());
        insumo.setUnidad(dto.getUnidad());

        Insumos_grooming insumoActualizado = insumosGroomingRepository.save(insumo);

        return convertToDTO(insumoActualizado);
    }

    @Override
    public void eliminar(UUID id) {

        Insumos_grooming insumo = insumosGroomingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Insumo grooming no encontrado"));

        insumosGroomingRepository.delete(insumo);
    }

    // =========================
    // CONVERTERS
    // =========================

    private Insumos_groomingDTO convertToDTO(Insumos_grooming insumo) {

        return Insumos_groomingDTO.builder()
                .id(insumo.getId())

                .fichaId(
                        insumo.getFicha() != null
                                ? insumo.getFicha().getId()
                                : null)

                .varianteId(
                        insumo.getVariante() != null
                                ? insumo.getVariante().getId()
                                : null)

                .varianteNombre(
                        insumo.getVariante() != null
                                ? insumo.getVariante().getNombre()
                                : null)

                .cantidad(insumo.getCantidad())
                .unidad(insumo.getUnidad())
                .registradoEn(insumo.getRegistradoEn())
                .build();
    }
}