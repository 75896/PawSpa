package com.example.spapet.service.impl;

import com.example.spapet.dto.PedidosDTO;

import com.example.spapet.model.Clientes;
import com.example.spapet.model.Pedidos;

import com.example.spapet.repository.ClientesRepository;
import com.example.spapet.repository.PedidosRepository;

import com.example.spapet.service.PedidosService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidosServiceImpl implements PedidosService {

    private final PedidosRepository pedidosRepository;
    private final ClientesRepository clientesRepository;

    @Override
    public List<PedidosDTO> obtenerTodos() {

        return pedidosRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PedidosDTO obtenerPorId(UUID id) {

        Pedidos pedido = pedidosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        return convertToDTO(pedido);
    }

    @Override
    public PedidosDTO crear(PedidosDTO dto) {

        Clientes cliente = clientesRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Pedidos pedido = Pedidos.builder()
                .cliente(cliente)
                .estado(dto.getEstado())
                .canalPedido(dto.getCanalPedido())
                .mensajeEnviado(dto.getMensajeEnviado())
                .subtotal(dto.getSubtotal())
                .descuento(dto.getDescuento())
                .total(dto.getTotal())
                .direccionEntrega(dto.getDireccionEntrega())
                .notas(dto.getNotas())
                .build();

        Pedidos pedidoGuardado = pedidosRepository.save(pedido);

        return convertToDTO(pedidoGuardado);
    }

    @Override
    public PedidosDTO actualizar(UUID id, PedidosDTO dto) {

        Pedidos pedido = pedidosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        Clientes cliente = clientesRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        pedido.setCliente(cliente);
        pedido.setEstado(dto.getEstado());
        pedido.setCanalPedido(dto.getCanalPedido());
        pedido.setMensajeEnviado(dto.getMensajeEnviado());
        pedido.setSubtotal(dto.getSubtotal());
        pedido.setDescuento(dto.getDescuento());
        pedido.setTotal(dto.getTotal());
        pedido.setDireccionEntrega(dto.getDireccionEntrega());
        pedido.setNotas(dto.getNotas());

        Pedidos pedidoActualizado = pedidosRepository.save(pedido);

        return convertToDTO(pedidoActualizado);
    }

    @Override
    public void eliminar(UUID id) {

        Pedidos pedido = pedidosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        pedidosRepository.delete(pedido);
    }

    // =========================
    // CONVERTERS
    // =========================

    private PedidosDTO convertToDTO(Pedidos pedido) {

        return PedidosDTO.builder()
                .id(pedido.getId())

                .clienteId(
                        pedido.getCliente() != null
                                ? pedido.getCliente().getId()
                                : null)

                .clienteNombre(
                        pedido.getCliente() != null
                                && pedido.getCliente().getUsuarios() != null
                                        ? pedido.getCliente().getUsuarios().getNombre()
                                        : null)

                .estado(pedido.getEstado())
                .canalPedido(pedido.getCanalPedido())
                .mensajeEnviado(pedido.getMensajeEnviado())
                .subtotal(pedido.getSubtotal())
                .descuento(pedido.getDescuento())
                .total(pedido.getTotal())
                .direccionEntrega(pedido.getDireccionEntrega())
                .notas(pedido.getNotas())
                .creadoEn(pedido.getCreadoEn())
                .actualizadoEn(pedido.getActualizadoEn())
                .build();
    }
}