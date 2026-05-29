package com.example.spapet.service.impl;

import com.example.spapet.dto.CierreCajaDTO;
import com.example.spapet.dto.CobrarDTO;
import com.example.spapet.dto.PagosDTO;
import com.example.spapet.model.*;
import com.example.spapet.repository.*;
import com.example.spapet.service.PagosService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.*;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PagosServiceImpl implements PagosService {

        private final PagosRepository pagosRepository;
        private final FacturasRepository facturasRepository;
        private final UsuariosRepository usuariosRepository;

        // =============================================
        // CRUD existente
        // =============================================

        @Override
        public List<PagosDTO> obtenerTodos() {
                return pagosRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
        }

        @Override
        public PagosDTO obtenerPorId(UUID id) {
                return convertToDTO(pagosRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Pago no encontrado")));
        }

        @Override
        public PagosDTO crear(PagosDTO dto) {
                Facturas factura = facturasRepository.findById(dto.getFacturaId())
                                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));
                Usuarios usuario = dto.getRegistradoPorId() != null
                                ? usuariosRepository.findById(dto.getRegistradoPorId()).orElse(null)
                                : null;
                Pagos pago = Pagos.builder()
                                .factura(factura).monto(dto.getMonto()).medio(dto.getMedio())
                                .referencia(dto.getReferencia()).estado(dto.getEstado())
                                .registradoPor(usuario).build();
                return convertToDTO(pagosRepository.save(pago));
        }

        @Override
        public PagosDTO actualizar(UUID id, PagosDTO dto) {
                Pagos pago = pagosRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));
                pago.setMedio(dto.getMedio());
                pago.setReferencia(dto.getReferencia());
                pago.setEstado(dto.getEstado());
                return convertToDTO(pagosRepository.save(pago));
        }

        @Override
        public void eliminar(UUID id) {
                pagosRepository.delete(pagosRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Pago no encontrado")));
        }

        // =============================================
        // PUNTO DE VENTA
        // =============================================

        @Override
        @Transactional
        public PagosDTO cobrar(CobrarDTO dto, String correoRecepcion) {
                Facturas factura = facturasRepository.findById(dto.getFacturaId())
                                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));

                if ("pagada".equals(factura.getEstado())) {
                        throw new RuntimeException("Esta factura ya fue pagada");
                }

                Usuarios recepcion = usuariosRepository.findByCorreo(correoRecepcion)
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                Pagos pago = Pagos.builder()
                                .factura(factura)
                                .monto(dto.getMonto())
                                .medio(dto.getMedio())
                                .referencia(dto.getReferencia())
                                .estado("pagado")
                                .registradoPor(recepcion)
                                .build();

                pagosRepository.save(pago);

                // Marcar factura como pagada
                factura.setEstado("pagada");
                facturasRepository.save(factura);

                return convertToDTO(pago);
        }

        // =============================================
        // CIERRE DE CAJA
        // =============================================

        @Override
        public CierreCajaDTO cierreCaja(LocalDate fecha) {
                ZoneOffset offset = ZoneOffset.ofHours(-4); // Bolivia UTC-4
                OffsetDateTime desde = fecha.atStartOfDay().atOffset(offset);
                OffsetDateTime hasta = fecha.plusDays(1).atStartOfDay().atOffset(offset);

                List<Pagos> pagos = pagosRepository.findByPagadoEnBetween(desde, hasta);

                BigDecimal totalEfectivo = sumarPorMedio(pagos, "efectivo");
                BigDecimal totalQr = sumarPorMedio(pagos, "qr");
                BigDecimal totalTransferencia = sumarPorMedio(pagos, "transferencia");
                BigDecimal totalTarjeta = sumarPorMedio(pagos, "tarjeta");
                BigDecimal totalGeneral = pagos.stream()
                                .map(Pagos::getMonto).reduce(BigDecimal.ZERO, BigDecimal::add);

                List<CierreCajaDTO.CierreCajaDetalleDTO> detalle = pagos.stream()
                                .map(p -> CierreCajaDTO.CierreCajaDetalleDTO.builder()
                                                .numeroFactura(p.getFactura().getNumero())
                                                .clienteNombre(p.getFactura().getClientes().getUsuarios().getNombre()
                                                                + " "
                                                                + p.getFactura().getClientes().getUsuarios()
                                                                                .getApellido())
                                                .tipo(p.getFactura().getCitas() != null ? "servicio" : "pedido")
                                                .medio(p.getMedio())
                                                .monto(p.getMonto())
                                                .pagadoEn(p.getPagadoEn().toString())
                                                .build())
                                .collect(Collectors.toList());

                return CierreCajaDTO.builder()
                                .fecha(fecha)
                                .totalEfectivo(totalEfectivo)
                                .totalQr(totalQr)
                                .totalTransferencia(totalTransferencia)
                                .totalTarjeta(totalTarjeta)
                                .totalGeneral(totalGeneral)
                                .cantidadPagos(pagos.size())
                                .detalle(detalle)
                                .build();
        }

        private BigDecimal sumarPorMedio(List<Pagos> pagos, String medio) {
                return pagos.stream()
                                .filter(p -> medio.equals(p.getMedio()))
                                .map(Pagos::getMonto)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        private PagosDTO convertToDTO(Pagos pago) {
                return PagosDTO.builder()
                                .id(pago.getId())
                                .facturaId(pago.getFactura() != null ? pago.getFactura().getId() : null)
                                .facturaNumero(pago.getFactura() != null ? pago.getFactura().getNumero() : null)
                                .monto(pago.getMonto())
                                .medio(pago.getMedio())
                                .referencia(pago.getReferencia())
                                .estado(pago.getEstado())
                                .registradoPorId(pago.getRegistradoPor() != null ? pago.getRegistradoPor().getId()
                                                : null)
                                .registradoPorNombre(
                                                pago.getRegistradoPor() != null ? pago.getRegistradoPor().getNombre()
                                                                : null)
                                .pagadoEn(pago.getPagadoEn())
                                .build();
        }
}