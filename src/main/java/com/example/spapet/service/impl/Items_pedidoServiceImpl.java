package com.example.spapet.service.impl;

import com.example.spapet.dto.Items_pedidoDTO;

import com.example.spapet.model.ItemPedido;
import com.example.spapet.model.Pedidos;
import com.example.spapet.model.Variantes_productos;

import com.example.spapet.repository.Items_pedidoRepository;
import com.example.spapet.repository.PedidosRepository;
import com.example.spapet.repository.Variantes_productosRepository;

import com.example.spapet.service.Items_pedidoService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class Items_pedidoServiceImpl implements Items_pedidoService {

    private final Items_pedidoRepository itemsPedidoRepository;
    private final PedidosRepository pedidosRepository;
    private final Variantes_productosRepository variantesProductosRepository;

    @Override
    public List<Items_pedidoDTO> obtenerTodos() {

        return itemsPedidoRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Items_pedidoDTO obtenerPorId(UUID id) {

        ItemPedido itemPedido = itemsPedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item del pedido no encontrado"));

        return convertToDTO(itemPedido);
    }

    @Override
    public Items_pedidoDTO crear(Items_pedidoDTO dto) {

        Pedidos pedido = pedidosRepository.findById(dto.getPedidoId())
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        Variantes_productos variante = variantesProductosRepository.findById(dto.getVarianteId())
                .orElseThrow(() -> new RuntimeException("Variante no encontrada"));

        ItemPedido itemPedido = ItemPedido.builder()
                .pedido(pedido)
                .variante(variante)
                .cantidad(dto.getCantidad())
                .precioUnitario(dto.getPrecioUnitario())
                .build();

        ItemPedido itemGuardado = itemsPedidoRepository.save(itemPedido);

        return convertToDTO(itemGuardado);
    }

    @Override
    public Items_pedidoDTO actualizar(UUID id, Items_pedidoDTO dto) {

        ItemPedido itemPedido = itemsPedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item del pedido no encontrado"));

        Pedidos pedido = pedidosRepository.findById(dto.getPedidoId())
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        Variantes_productos variante = variantesProductosRepository.findById(dto.getVarianteId())
                .orElseThrow(() -> new RuntimeException("Variante no encontrada"));

        itemPedido.setPedido(pedido);
        itemPedido.setVariante(variante);
        itemPedido.setCantidad(dto.getCantidad());
        itemPedido.setPrecioUnitario(dto.getPrecioUnitario());

        ItemPedido itemActualizado = itemsPedidoRepository.save(itemPedido);

        return convertToDTO(itemActualizado);
    }

    @Override
    public void eliminar(UUID id) {

        ItemPedido itemPedido = itemsPedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item del pedido no encontrado"));

        itemsPedidoRepository.delete(itemPedido);
    }

    // =========================
    // CONVERTERS
    // =========================

    private Items_pedidoDTO convertToDTO(ItemPedido itemPedido) {

        return Items_pedidoDTO.builder()
                .id(itemPedido.getId())

                .pedidoId(
                        itemPedido.getPedido() != null
                                ? itemPedido.getPedido().getId()
                                : null)

                .varianteId(
                        itemPedido.getVariante() != null
                                ? itemPedido.getVariante().getId()
                                : null)

                .varianteNombre(
                        itemPedido.getVariante() != null
                                ? itemPedido.getVariante().getNombre()
                                : null)

                .cantidad(itemPedido.getCantidad())
                .precioUnitario(itemPedido.getPrecioUnitario())
                .subtotal(itemPedido.getSubtotal())
                .build();
    }
}