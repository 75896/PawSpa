package com.example.spapet.service.impl;

import com.example.spapet.dto.PagosDTO;

import com.example.spapet.model.Facturas;
import com.example.spapet.model.Pagos;
import com.example.spapet.model.Usuarios;

import com.example.spapet.repository.FacturasRepository;
import com.example.spapet.repository.PagosRepository;
import com.example.spapet.repository.UsuariosRepository;

import com.example.spapet.service.PagosService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PagosServiceImpl implements PagosService {

    private final PagosRepository pagosRepository;
    private final FacturasRepository facturasRepository;
    private final UsuariosRepository usuariosRepository;

    @Override
    public List<PagosDTO> obtenerTodos() {

        return pagosRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PagosDTO obtenerPorId(UUID id) {

        Pagos pago = pagosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));

        return convertToDTO(pago);
    }

    @Override
    public PagosDTO crear(PagosDTO dto) {

        Facturas factura = facturasRepository.findById(dto.getFacturaId())
                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));

        Usuarios usuario = null;

        if (dto.getRegistradoPorId() != null) {

            usuario = usuariosRepository.findById(dto.getRegistradoPorId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        }

        Pagos pago = Pagos.builder()
                .factura(factura)
                .monto(dto.getMonto())
                .medio(dto.getMedio())
                .referencia(dto.getReferencia())
                .estado(dto.getEstado())
                .registradoPor(usuario)
                .build();

        Pagos pagoGuardado = pagosRepository.save(pago);

        return convertToDTO(pagoGuardado);
    }

    @Override
    public PagosDTO actualizar(UUID id, PagosDTO dto) {

        Pagos pago = pagosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));

        Facturas factura = facturasRepository.findById(dto.getFacturaId())
                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));

        Usuarios usuario = null;

        if (dto.getRegistradoPorId() != null) {

            usuario = usuariosRepository.findById(dto.getRegistradoPorId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        }

        pago.setFactura(factura);
        pago.setMonto(dto.getMonto());
        pago.setMedio(dto.getMedio());
        pago.setReferencia(dto.getReferencia());
        pago.setEstado(dto.getEstado());
        pago.setRegistradoPor(usuario);

        Pagos pagoActualizado = pagosRepository.save(pago);

        return convertToDTO(pagoActualizado);
    }

    @Override
    public void eliminar(UUID id) {

        Pagos pago = pagosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));

        pagosRepository.delete(pago);
    }

    // =========================
    // CONVERTERS
    // =========================

    private PagosDTO convertToDTO(Pagos pago) {

        return PagosDTO.builder()
                .id(pago.getId())

                .facturaId(
                        pago.getFactura() != null
                                ? pago.getFactura().getId()
                                : null)

                .facturaNumero(
                        pago.getFactura() != null
                                ? pago.getFactura().getNumero()
                                : null)

                .monto(pago.getMonto())
                .medio(pago.getMedio())
                .referencia(pago.getReferencia())
                .estado(pago.getEstado())

                .registradoPorId(
                        pago.getRegistradoPor() != null
                                ? pago.getRegistradoPor().getId()
                                : null)

                .registradoPorNombre(
                        pago.getRegistradoPor() != null
                                ? pago.getRegistradoPor().getNombre()
                                : null)

                .pagadoEn(pago.getPagadoEn())
                .build();
    }
}