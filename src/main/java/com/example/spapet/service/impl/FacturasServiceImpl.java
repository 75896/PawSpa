package com.example.spapet.service.impl;

import com.example.spapet.dto.FacturasDTO;
import com.example.spapet.dto.ReciboDTO;
import com.example.spapet.model.*;
import com.example.spapet.repository.*;
import com.example.spapet.service.FacturasService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
        private final Items_pedidoRepository itemsPedidoRepository;
        private final PagosRepository pagosRepository;

        // =============================================
        // CRUD existente
        // =============================================

        @Override
        public List<FacturasDTO> obtenerTodos() {
                return facturasRepository.findAll()
                                .stream().map(this::convertToDTO).collect(Collectors.toList());
        }

        @Override
        public FacturasDTO obtenerPorId(UUID id) {
                return convertToDTO(facturasRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Factura no encontrada")));
        }

        @Override
        public FacturasDTO crear(FacturasDTO dto) {
                Clientes cliente = clientesRepository.findById(dto.getClienteId())
                                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
                Citas cita = dto.getCitaId() != null
                                ? citasRepository.findById(dto.getCitaId())
                                                .orElseThrow(() -> new RuntimeException("Cita no encontrada"))
                                : null;
                Pedidos pedido = dto.getPedidoId() != null
                                ? pedidosRepository.findById(dto.getPedidoId())
                                                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"))
                                : null;

                Facturas factura = Facturas.builder()
                                .numero(generarNumero())
                                .clientes(cliente)
                                .citas(cita)
                                .pedidos(pedido)
                                .subtotal(dto.getSubtotal())
                                .descuento(dto.getDescuento())
                                .impuestos(dto.getImpuestos())
                                .total(dto.getTotal())
                                .estado(dto.getEstado() != null ? dto.getEstado() : "pendiente")
                                .notas(dto.getNotas())
                                .venceEn(dto.getVenceEn())
                                .build();

                return convertToDTO(facturasRepository.save(factura));
        }

        @Override
        public FacturasDTO actualizar(UUID id, FacturasDTO dto) {
                Facturas factura = facturasRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));
                factura.setEstado(dto.getEstado());
                factura.setNotas(dto.getNotas());
                factura.setDescuento(dto.getDescuento());
                factura.setTotal(dto.getTotal().subtract(dto.getDescuento()));
                return convertToDTO(facturasRepository.save(factura));
        }

        @Override
        public void eliminar(UUID id) {
                facturasRepository.delete(facturasRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Factura no encontrada")));
        }

        // =============================================
        // GENERACIÓN AUTOMÁTICA
        // =============================================

        @Override
        @Transactional
        public FacturasDTO generarDesdeCita(UUID citaId) {
                Citas cita = citasRepository.findById(citaId)
                                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

                // Evitar duplicados
                if (facturasRepository.existsByCitas(cita)) {
                        return convertToDTO(facturasRepository.findByCitas(cita).get());
                }

                // Obtener cliente desde la mascota
                Clientes cliente = cita.getMascotas().getClientes();
                BigDecimal monto = cita.getPrecioAcordado() != null
                                ? cita.getPrecioAcordado()
                                : cita.getServicios().getPrecioBase();

                Facturas factura = Facturas.builder()
                                .numero(generarNumero())
                                .clientes(cliente)
                                .citas(cita)
                                .subtotal(monto)
                                .descuento(BigDecimal.ZERO)
                                .impuestos(BigDecimal.ZERO)
                                .total(monto)
                                .estado("pendiente")
                                .build();

                return convertToDTO(facturasRepository.save(factura));
        }

        @Override
        @Transactional
        public FacturasDTO generarDesdePedido(UUID pedidoId) {
                Pedidos pedido = pedidosRepository.findById(pedidoId)
                                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

                // Evitar duplicados
                if (facturasRepository.existsByPedidos(pedido)) {
                        return convertToDTO(facturasRepository.findByPedidos(pedido).get());
                }

                Facturas factura = Facturas.builder()
                                .numero(generarNumero())
                                .clientes(pedido.getCliente())
                                .pedidos(pedido)
                                .subtotal(pedido.getSubtotal())
                                .descuento(pedido.getDescuento())
                                .impuestos(BigDecimal.ZERO)
                                .total(pedido.getTotal())
                                .estado("pendiente")
                                .build();

                return convertToDTO(facturasRepository.save(factura));
        }

        // =============================================
        // RECIBO
        // =============================================

        @Override
        public ReciboDTO generarRecibo(UUID facturaId) {
                Facturas factura = facturasRepository.findById(facturaId)
                                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));

                List<Pagos> pagos = pagosRepository.findByFacturaId(facturaId);
                Pagos pago = pagos.isEmpty() ? null : pagos.get(0);

                ReciboDTO.ReciboDTOBuilder recibo = ReciboDTO.builder()
                                .facturaId(factura.getId())
                                .numeroFactura(factura.getNumero())
                                .clienteNombre(factura.getClientes().getUsuarios().getNombre()
                                                + " " + factura.getClientes().getUsuarios().getApellido())
                                .clienteTelefono(factura.getClientes().getUsuarios().getTelefono())
                                .subtotal(factura.getSubtotal())
                                .descuento(factura.getDescuento())
                                .total(factura.getTotal())
                                .medioPago(pago != null ? pago.getMedio() : null)
                                .referenciaPago(pago != null ? pago.getReferencia() : null)
                                .pagadoEn(pago != null ? pago.getPagadoEn() : null);

                // Si es de cita
                if (factura.getCitas() != null) {
                        Citas cita = factura.getCitas();
                        recibo.tipo("servicio")
                                        .servicioNombre(cita.getServicios().getNombre())
                                        .mascotaNombre(cita.getMascotas().getNombre())
                                        .fechaCita(cita.getFechaInicio());
                }

                // Si es de pedido
                if (factura.getPedidos() != null) {
                        List<ItemPedido> items = itemsPedidoRepository
                                        .findByPedido(factura.getPedidos());
                        List<ReciboDTO.ReciboItemDTO> itemsDTO = items.stream()
                                        .map(i -> ReciboDTO.ReciboItemDTO.builder()
                                                        .productoNombre(i.getVariante().getProducto().getNombre())
                                                        .varianteNombre(i.getVariante().getNombre())
                                                        .cantidad(i.getCantidad())
                                                        .precioUnitario(i.getPrecioUnitario())
                                                        .subtotal(i.getPrecioUnitario()
                                                                        .multiply(BigDecimal.valueOf(i.getCantidad())))
                                                        .build())
                                        .collect(Collectors.toList());
                        recibo.tipo("pedido").items(itemsDTO);
                }

                return recibo.build();
        }

        // =============================================
        // HELPERS
        // =============================================

        private String generarNumero() {
                long count = facturasRepository.count() + 1;
                return String.format("FAC-%06d", count);
        }

        private FacturasDTO convertToDTO(Facturas factura) {
                return FacturasDTO.builder()
                                .id(factura.getId())
                                .numero(factura.getNumero())
                                .clienteId(factura.getClientes() != null ? factura.getClientes().getId() : null)
                                .clienteNombre(factura.getClientes() != null
                                                && factura.getClientes().getUsuarios() != null
                                                                ? factura.getClientes().getUsuarios().getNombre()
                                                                : null)
                                .citaId(factura.getCitas() != null ? factura.getCitas().getId() : null)
                                .pedidoId(factura.getPedidos() != null ? factura.getPedidos().getId() : null)
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