package com.example.spapet.service.impl;

import com.example.spapet.dto.ProductoConVariantesDTO;
import com.example.spapet.dto.ProductosDTO;
import com.example.spapet.dto.Variantes_productosDTO;
import com.example.spapet.model.Categorias_producto;
import com.example.spapet.model.Productos;
import com.example.spapet.model.Variantes_productos;
import com.example.spapet.repository.Categorias_productoRepository;
import com.example.spapet.repository.ProductosRepository;
import com.example.spapet.repository.Variantes_productosRepository;
import com.example.spapet.service.ProductosService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductosServiceImpl implements ProductosService {

        private final ProductosRepository productosRepository;
        private final Categorias_productoRepository categoriasProductoRepository;
        private final Variantes_productosRepository variantesRepository;

        @Override
        public List<ProductosDTO> obtenerTodos() {

                return productosRepository.findAll()
                                .stream()
                                .map(this::convertToDTO)
                                .collect(Collectors.toList());
        }

        @Override
        public ProductosDTO obtenerPorId(UUID id) {

                Productos producto = productosRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

                return convertToDTO(producto);
        }

        @Override
        public ProductosDTO crear(ProductosDTO productosDTO) {

                Categorias_producto categoria = null;

                if (productosDTO.getCategoriaId() != null) {

                        categoria = categoriasProductoRepository.findById(productosDTO.getCategoriaId())
                                        .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
                }

                Productos producto = Productos.builder()
                                .categoria(categoria)
                                .nombre(productosDTO.getNombre())
                                .descripcion(productosDTO.getDescripcion())
                                .imagenUrl(productosDTO.getImagenUrl())
                                .precioBase(productosDTO.getPrecioBase())
                                .activo(productosDTO.getActivo())
                                .build();

                Productos productoGuardado = productosRepository.save(producto);

                return convertToDTO(productoGuardado);
        }

        @Override
        public ProductosDTO actualizar(UUID id, ProductosDTO productosDTO) {

                Productos producto = productosRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

                Categorias_producto categoria = null;

                if (productosDTO.getCategoriaId() != null) {

                        categoria = categoriasProductoRepository.findById(productosDTO.getCategoriaId())
                                        .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
                }

                producto.setCategoria(categoria);
                producto.setNombre(productosDTO.getNombre());
                producto.setDescripcion(productosDTO.getDescripcion());
                producto.setImagenUrl(productosDTO.getImagenUrl());
                producto.setPrecioBase(productosDTO.getPrecioBase());
                producto.setActivo(productosDTO.getActivo());

                Productos productoActualizado = productosRepository.save(producto);

                return convertToDTO(productoActualizado);
        }

        @Override
        public void eliminar(UUID id) {

                Productos producto = productosRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

                productosRepository.delete(producto);
        }

        // =========================
        // CONVERTERS
        // =========================

        private ProductosDTO convertToDTO(Productos producto) {

                return ProductosDTO.builder()
                                .id(producto.getId())

                                .categoriaId(
                                                producto.getCategoria() != null
                                                                ? producto.getCategoria().getId()
                                                                : null)

                                .categoriaNombre(
                                                producto.getCategoria() != null
                                                                ? producto.getCategoria().getNombre()
                                                                : null)

                                .nombre(producto.getNombre())
                                .descripcion(producto.getDescripcion())
                                .imagenUrl(producto.getImagenUrl())
                                .precioBase(producto.getPrecioBase())
                                .activo(producto.getActivo())
                                .creadoEn(producto.getCreadoEn())
                                .actualizadoEn(producto.getActualizadoEn())
                                .build();
        }

        @Override
        public List<ProductosDTO> obtenerActivos() {
                return productosRepository.findByActivoTrue()
                                .stream()
                                .map(this::convertToDTO)
                                .collect(Collectors.toList());
        }

        @Override
        public List<ProductosDTO> obtenerActivosPorCategoria(UUID categoriaId) {
                return productosRepository.findByActivoTrueAndCategoriaId(categoriaId)
                                .stream()
                                .map(this::convertToDTO)
                                .collect(Collectors.toList());
        }

        @Override
        public ProductoConVariantesDTO obtenerConVariantes(UUID id) {
                Productos producto = productosRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

                List<Variantes_productosDTO> variantes = variantesRepository
                                .findByProductoIdAndActivaTrue(id)
                                .stream()
                                .map(this::convertVarianteToDTO)
                                .collect(Collectors.toList());

                return ProductoConVariantesDTO.builder()
                                .id(producto.getId())
                                .nombre(producto.getNombre())
                                .descripcion(producto.getDescripcion())
                                .imagenUrl(producto.getImagenUrl())
                                .precioBase(producto.getPrecioBase())
                                .categoriaId(producto.getCategoria() != null ? producto.getCategoria().getId() : null)
                                .categoriaNombre(producto.getCategoria() != null ? producto.getCategoria().getNombre()
                                                : null)
                                .variantes(variantes)
                                .build();
        }

        private Variantes_productosDTO convertVarianteToDTO(Variantes_productos v) {
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
                                .build();
        }
}