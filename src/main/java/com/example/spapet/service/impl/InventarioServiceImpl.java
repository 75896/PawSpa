package com.example.spapet.service.impl;

import com.example.spapet.dto.Variantes_productosDTO;
import com.example.spapet.model.Variantes_productos;
import com.example.spapet.repository.Variantes_productosRepository;
import com.example.spapet.service.InventarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventarioServiceImpl implements InventarioService {

    private final Variantes_productosRepository variantesRepository;

    @Override
    public List<Variantes_productosDTO> listarVariantes() {
        return variantesRepository.findAll()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<Variantes_productosDTO> listarStockBajo() {
        return variantesRepository.findConStockBajo()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Variantes_productosDTO reponer(UUID id, Integer cantidad) {
        Variantes_productos variante = variantesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Variante no encontrada"));

        variante.setStock(variante.getStock() + cantidad);

        if (variante.getStock() > variante.getStockMinimo()) {
            variante.setNivelAlerta("ok");
        } else if (variante.getStock() > 0) {
            variante.setNivelAlerta("bajo");
        } else {
            variante.setNivelAlerta("critico");
        }

        return toDTO(variantesRepository.save(variante));
    }

    @Override
    @Transactional
    public Variantes_productosDTO actualizarStockMinimo(UUID id, Integer stockMinimo) {
        Variantes_productos variante = variantesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Variante no encontrada"));

        variante.setStockMinimo(stockMinimo);

        if (variante.getStock() == 0) {
            variante.setNivelAlerta("critico");
        } else if (variante.getStock() <= stockMinimo) {
            variante.setNivelAlerta("bajo");
        } else {
            variante.setNivelAlerta("ok");
        }

        return toDTO(variantesRepository.save(variante));
    }

    private Variantes_productosDTO toDTO(Variantes_productos v) {
        return Variantes_productosDTO.builder()
                .id(v.getId())
                .productoId(v.getProducto().getId())
                .productoNombre(v.getProducto().getNombre())
                .tipoVariante(v.getTipoVariante())
                .nombre(v.getNombre())
                .sku(v.getSku())
                .precio(v.getPrecio())
                .stock(v.getStock())
                .stockMinimo(v.getStockMinimo())
                .nivelAlerta(v.getNivelAlerta())
                .activa(v.getActiva())
                .creadoEn(v.getCreadoEn())
                .actualizadoEn(v.getActualizadoEn())
                .build();
    }
}