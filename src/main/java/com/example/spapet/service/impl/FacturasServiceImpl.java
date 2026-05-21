package com.example.spapet.service.impl;

import com.example.spapet.dto.FacturasDTO;

import com.example.spapet.model.Citas;
import com.example.spapet.model.Clientes;
import com.example.spapet.model.Facturas;
import com.example.spapet.model.Pedidos;

import com.example.spapet.repository.CitasRepository;
import com.example.spapet.repository.ClientesRepository;
import com.example.spapet.repository.FacturasRepository;
import com.example.spapet.repository.PedidosRepository;

import com.example.spapet.service.FacturasService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FacturasServiceImpl implements FacturasService {

    private final FacturasRepository facturasRepository;
    private final ClientesRepository clientesRepository;
    private final CitasRepository citasRepository;
    private final PedidosRepository pedidosRepository;

    @Override
    public List<FacturasDTO> obtenerTodos() {

        return facturasRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public FacturasDTO obtenerPorId(UUID id) {

        Facturas factura = facturasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));

        return convertToDTO(factura);
    }

    @Override
    public FacturasDTO crear(FacturasDTO dto) {

        Clientes cliente = clientesRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Citas cita = null;

        if (dto.getCitaId() != null) {

            cita = citasRepository.findById(dto.getCitaId())
                    .orElseThrow(() -> new RuntimeException("Cita no encontrada"));
        }

        Pedidos pedido = null;

        if (dto.getPedidoId() != null) {

            pedido = pedidosRepository.findById(dto.getPedidoId())
                    .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        }

        Facturas factura = Facturas.builder()
                .numero(dto.getNumero())
                .clientes(cliente)
                .citas(cita)
                .pedidos(pedido)
                .subtotal(dto.getSubtotal())
                .descuento(dto.getDescuento())
                .impuestos(dto.getImpuestos())
                .total(dto.getTotal())
                .estado(dto.getEstado())
                .notas(dto.getNotas())
                .venceEn(dto.getVenceEn())
                .build();

        Facturas facturaGuardada = facturasRepository.save(factura);

        return convertToDTO(facturaGuardada);
    }

    @Override
    public FacturasDTO actualizar(UUID id, FacturasDTO dto) {

        Facturas factura = facturasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));

        Clientes cliente = clientesRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Citas cita = null;

        if (dto.getCitaId() != null) {

            cita = citasRepository.findById(dto.getCitaId())
                    .orElseThrow(() -> new RuntimeException("Cita no encontrada"));
        }

        Pedidos pedido = null;

        if (dto.getPedidoId() != null) {

            pedido = pedidosRepository.findById(dto.getPedidoId())
                    .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        }

        factura.setNumero(dto.getNumero());
        factura.setClientes(cliente);
        factura.setCitas(cita);
        factura.setPedidos(pedido);
        factura.setSubtotal(dto.getSubtotal());
        factura.setDescuento(dto.getDescuento());
        factura.setImpuestos(dto.getImpuestos());
        factura.setTotal(dto.getTotal());
        factura.setEstado(dto.getEstado());
        factura.setNotas(dto.getNotas());
        factura.setVenceEn(dto.getVenceEn());

        Facturas facturaActualizada = facturasRepository.save(factura);

        return convertToDTO(facturaActualizada);
    }

    @Override
    public void eliminar(UUID id) {

        Facturas factura = facturasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));

        facturasRepository.delete(factura);
    }

    // =========================
    // CONVERTERS
    // =========================

    private FacturasDTO convertToDTO(Facturas factura) {

        return FacturasDTO.builder()
                .id(factura.getId())
                .numero(factura.getNumero())

                .clienteId(
                        factura.getClientes() != null
                                ? factura.getClientes().getId()
                                : null)

                .clienteNombre(
                        factura.getClientes() != null
                                && factura.getClientes().getUsuarios() != null
                                        ? factura.getClientes().getUsuarios().getNombre()
                                        : null)

                .citaId(
                        factura.getCitas() != null
                                ? factura.getCitas().getId()
                                : null)

                .pedidoId(
                        factura.getPedidos() != null
                                ? factura.getPedidos().getId()
                                : null)

                .subtotal(factura.getSubtotal())
                .descuento(factura.getDescuento())
                .impuestos(factura.getImpuestos())
                .total(factura.getTotal())
                .estado(factura.getEstado())
                .notas(factura.getNotas())
                .emitidaEn(factura.getEmitidaEn())
                .venceEn(factura.getVenceEn())
                .creadoEn(factura.getCreadoEn())
                .build();
    }
}