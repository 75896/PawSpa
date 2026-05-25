package com.example.spapet.service.impl;

import com.example.spapet.dto.Insumos_groomingDTO;
import com.example.spapet.dto.ProductosDTO;
import com.example.spapet.model.Fichas_grooming;
import com.example.spapet.model.Insumos_grooming;
import com.example.spapet.model.Variantes_productos;

import com.example.spapet.repository.Fichas_groomingRepository;
import com.example.spapet.repository.Insumos_groomingRepository;
import com.example.spapet.repository.Variantes_productosRepository;

import com.example.spapet.service.Insumos_groomingService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class Insumos_groomingServiceImpl implements Insumos_groomingService {

        private final Insumos_groomingRepository insumosRepository;
        private final Variantes_productosRepository variantesRepository;
        private final Fichas_groomingRepository fichasRepository;

        @Override
        public List<Insumos_groomingDTO> listarPorFicha(UUID fichaId) {
                return insumosRepository.findByFichaId(fichaId)
                                .stream().map(this::toDTO).collect(Collectors.toList());
        }

        @Override
        @Transactional
        public Insumos_groomingDTO registrar(Insumos_groomingDTO dto) {
                Fichas_grooming ficha = fichasRepository.findById(dto.getFichaId())
                                .orElseThrow(() -> new RuntimeException("Ficha no encontrada"));

                Variantes_productos variante = variantesRepository.findById(dto.getVarianteId())
                                .orElseThrow(() -> new RuntimeException("Variante no encontrada"));

                if (variante.getStock() < dto.getCantidad().intValue()) {
                        throw new RuntimeException("Stock insuficiente para " + variante.getNombre());
                }

                // Descontar stock
                variante.setStock(variante.getStock() - dto.getCantidad().intValue());

                // Actualizar nivel de alerta
                if (variante.getStock() == 0) {
                        variante.setNivelAlerta("critico");
                } else if (variante.getStock() <= variante.getStockMinimo()) {
                        variante.setNivelAlerta("bajo");
                } else {
                        variante.setNivelAlerta("ok");
                }
                variantesRepository.save(variante);

                Insumos_grooming insumo = Insumos_grooming.builder()
                                .ficha(ficha)
                                .variante(variante)
                                .cantidad(dto.getCantidad())
                                .unidad(dto.getUnidad() != null ? dto.getUnidad() : "unidad")
                                .build();

                return toDTO(insumosRepository.save(insumo));
        }

        @Override
        @Transactional
        public void eliminar(UUID id) {
                Insumos_grooming insumo = insumosRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Insumo no encontrado"));

                // Devolver stock
                Variantes_productos variante = insumo.getVariante();
                variante.setStock(variante.getStock() + insumo.getCantidad().intValue());
                if (variante.getStock() > variante.getStockMinimo()) {
                        variante.setNivelAlerta("ok");
                }
                variantesRepository.save(variante);

                insumosRepository.deleteById(id);
        }

        @Override
        public List<ProductosDTO> listarProductosDisponibles() {
                return variantesRepository.findByActivaTrue()
                                .stream()
                                .filter(v -> v.getStock() > 0)
                                .map(v -> ProductosDTO.builder()
                                                .id(v.getId())
                                                .nombre(v.getProducto().getNombre() + " — " + v.getNombre())
                                                .precioBase(v.getPrecio())
                                                .activo(v.getActiva())
                                                .build())
                                .collect(Collectors.toList());
        }

        @Override
        public List<ProductosDTO> listarStockBajo() {
                return variantesRepository.findConStockBajo()
                                .stream()
                                .map(v -> ProductosDTO.builder()
                                                .id(v.getId())
                                                .nombre(v.getProducto().getNombre() + " — " + v.getNombre())
                                                .precioBase(v.getPrecio())
                                                .activo(v.getActiva())
                                                .build())
                                .collect(Collectors.toList());
        }

        private Insumos_groomingDTO toDTO(Insumos_grooming i) {
                return Insumos_groomingDTO.builder()
                                .id(i.getId())
                                .fichaId(i.getFicha().getId())
                                .varianteId(i.getVariante().getId())
                                .varianteNombre(i.getVariante().getNombre())
                                .productoNombre(i.getVariante().getProducto().getNombre())
                                .cantidad(i.getCantidad())
                                .unidad(i.getUnidad())
                                .stockDisponible(i.getVariante().getStock())
                                .registradoEn(i.getRegistradoEn())
                                .build();
        }
}