package com.example.spapet.service.impl;

import com.example.spapet.dto.*;
import com.example.spapet.model.*;
import com.example.spapet.repository.*;
import com.example.spapet.service.PedidosService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidosServiceImpl implements PedidosService {

        private final PedidosRepository pedidosRepository;
        private final ClientesRepository clientesRepository;
        private final UsuariosRepository usuariosRepository;
        private final Items_pedidoRepository itemsPedidoRepository;
        private final Variantes_productosRepository variantesRepository;

        // =============================================
        // CRUD ADMIN (existente, sin cambios de fondo)
        // =============================================

        @Override
        public List<PedidosDTO> obtenerTodos() {
                return pedidosRepository.findAll()
                                .stream()
                                .map(this::convertToDTO)
                                .collect(Collectors.toList());
        }

        @Override
        public PedidosDTO obtenerPorId(UUID id) {
                return convertToDTO(pedidosRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Pedido no encontrado")));
        }

        @Override
        public PedidosDTO crear(PedidosDTO dto) {
                Clientes cliente = clientesRepository.findById(dto.getClienteId())
                                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
                Pedidos pedido = Pedidos.builder()
                                .cliente(cliente)
                                .estado(dto.getEstado())
                                .canalPedido(dto.getCanalPedido())
                                .subtotal(BigDecimal.ZERO)
                                .descuento(BigDecimal.ZERO)
                                .total(BigDecimal.ZERO)
                                .direccionEntrega(dto.getDireccionEntrega())
                                .notas(dto.getNotas())
                                .build();
                return convertToDTO(pedidosRepository.save(pedido));
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
                pedido.setDireccionEntrega(dto.getDireccionEntrega());
                pedido.setNotas(dto.getNotas());
                return convertToDTO(pedidosRepository.save(pedido));
        }

        @Override
        public void eliminar(UUID id) {
                pedidosRepository.delete(
                                pedidosRepository.findById(id)
                                                .orElseThrow(() -> new RuntimeException("Pedido no encontrado")));
        }

        // =============================================
        // LÓGICA DE TIENDA — obtener cliente desde JWT
        // =============================================

        private Clientes getClientePorCorreo(String correo) {
                System.out.println("=== buscando usuario: " + correo);
                Usuarios usuario = usuariosRepository.findByCorreo(correo)
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                System.out.println("=== usuario id: " + usuario.getId());
                Clientes cliente = clientesRepository.findFirstByUsuarios(usuario)
                                .orElseThrow(() -> new RuntimeException("Perfil de cliente no encontrado"));
                System.out.println("=== cliente id: " + cliente.getId());
                return cliente;
        }

        // Crear pedido en borrador para el cliente autenticado
        @Override
        @Transactional
        public CarritoDTO crearCarrito(String correo) {
                System.out.println("=== crearCarrito INICIO para: " + correo);
                try {
                        Clientes cliente = getClientePorCorreo(correo);
                        System.out.println("=== cliente encontrado: " + cliente.getId());

                        return pedidosRepository.findFirstByClienteAndEstadoOrderByCreadoEnDesc(cliente, "borrador")
                                        .map(p -> {
                                                System.out.println("=== pedido borrador existente: " + p.getId());
                                                return convertToCarritoDTO(p);
                                        })
                                        .orElseGet(() -> {
                                                System.out.println("=== creando nuevo pedido borrador");
                                                Pedidos nuevo = Pedidos.builder()
                                                                .cliente(cliente)
                                                                .estado("borrador")
                                                                .canalPedido(cliente.getCanalPreferido())
                                                                .subtotal(BigDecimal.ZERO)
                                                                .descuento(BigDecimal.ZERO)
                                                                .total(BigDecimal.ZERO)
                                                                .build();
                                                return convertToCarritoDTO(pedidosRepository.save(nuevo));
                                        });
                } catch (Exception e) {
                        System.out.println("=== ERROR en crearCarrito: " + e.getClass().getName() + " - "
                                        + e.getMessage());
                        e.printStackTrace();
                        throw e;
                }
        }

        // Obtener carrito activo del cliente
        @Override
        public CarritoDTO obtenerCarrito(String correo) {
                Clientes cliente = getClientePorCorreo(correo);
                Pedidos pedido = pedidosRepository.findFirstByClienteAndEstadoOrderByCreadoEnDesc(cliente, "borrador")
                                .orElseThrow(() -> new RuntimeException("No tienes un carrito activo"));
                return convertToCarritoDTO(pedido);
        }

        // Listar todos los pedidos del cliente (historial)
        @Override
        public List<PedidosDTO> obtenerMisPedidos(String correo) {
                Clientes cliente = getClientePorCorreo(correo);
                return pedidosRepository.findByClienteOrderByCreadoEnDesc(cliente)
                                .stream()
                                .map(this::convertToDTO)
                                .collect(Collectors.toList());
        }

        // Agregar o actualizar item en el carrito
        @Override
        @Transactional
        public CarritoDTO agregarItem(String correo, UUID pedidoId, AgregarItemDTO dto) {
                Clientes cliente = getClientePorCorreo(correo);
                Pedidos pedido = pedidosRepository.findById(pedidoId)
                                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

                // Seguridad: el pedido debe pertenecer al cliente autenticado
                if (!pedido.getCliente().getId().equals(cliente.getId())) {
                        throw new RuntimeException("No tienes permiso para modificar este pedido");
                }
                if (!pedido.getEstado().equals("borrador")) {
                        throw new RuntimeException("Solo puedes modificar pedidos en borrador");
                }

                Variantes_productos variante = variantesRepository.findById(dto.getVarianteId())
                                .orElseThrow(() -> new RuntimeException("Variante no encontrada"));

                if (!variante.getActiva()) {
                        throw new RuntimeException("Este producto no está disponible");
                }
                if (variante.getStock() < dto.getCantidad()) {
                        throw new RuntimeException("Stock insuficiente. Disponible: " + variante.getStock());
                }

                // Si ya existe el item, actualiza la cantidad
                ItemPedido item = itemsPedidoRepository
                                .findByPedidoAndVariante(pedido, variante)
                                .map(existing -> {
                                        int nuevaCantidad = existing.getCantidad() + dto.getCantidad();
                                        if (variante.getStock() < nuevaCantidad) {
                                                throw new RuntimeException("Stock insuficiente. Disponible: "
                                                                + variante.getStock());
                                        }
                                        existing.setCantidad(nuevaCantidad);
                                        existing.setPrecioUnitario(variante.getPrecio());
                                        return itemsPedidoRepository.save(existing);
                                })
                                .orElseGet(() -> {
                                        ItemPedido nuevo = ItemPedido.builder()
                                                        .pedido(pedido)
                                                        .variante(variante)
                                                        .cantidad(dto.getCantidad())
                                                        .precioUnitario(variante.getPrecio())
                                                        .build();
                                        return itemsPedidoRepository.save(nuevo);
                                });

                recalcularTotales(pedido);
                return convertToCarritoDTO(pedido);
        }

        // Cambiar cantidad de un item
        @Override
        @Transactional
        public CarritoDTO actualizarItem(String correo, UUID pedidoId, UUID itemId, Integer cantidad) {
                Clientes cliente = getClientePorCorreo(correo);
                Pedidos pedido = pedidosRepository.findById(pedidoId)
                                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

                if (!pedido.getCliente().getId().equals(cliente.getId())) {
                        throw new RuntimeException("No tienes permiso para modificar este pedido");
                }

                ItemPedido item = itemsPedidoRepository.findById(itemId)
                                .orElseThrow(() -> new RuntimeException("Item no encontrado"));

                Variantes_productos variante = item.getVariante();
                if (variante.getStock() < cantidad) {
                        throw new RuntimeException("Stock insuficiente. Disponible: " + variante.getStock());
                }

                item.setCantidad(cantidad);
                item.setPrecioUnitario(variante.getPrecio());
                itemsPedidoRepository.save(item);

                recalcularTotales(pedido);
                return convertToCarritoDTO(pedido);
        }

        // Eliminar item del carrito
        @Override
        @Transactional
        public CarritoDTO eliminarItem(String correo, UUID pedidoId, UUID itemId) {
                Clientes cliente = getClientePorCorreo(correo);
                Pedidos pedido = pedidosRepository.findById(pedidoId)
                                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

                if (!pedido.getCliente().getId().equals(cliente.getId())) {
                        throw new RuntimeException("No tienes permiso para modificar este pedido");
                }

                ItemPedido item = itemsPedidoRepository.findById(itemId)
                                .orElseThrow(() -> new RuntimeException("Item no encontrado"));

                itemsPedidoRepository.delete(item);
                recalcularTotales(pedido);
                return convertToCarritoDTO(pedido);
        }

        // Confirmar pedido y descontar stock
        @Override
        @Transactional
        public PedidosDTO confirmarPedido(String correo, UUID pedidoId) {
                Clientes cliente = getClientePorCorreo(correo);
                Pedidos pedido = pedidosRepository.findById(pedidoId)
                                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

                if (!pedido.getCliente().getId().equals(cliente.getId())) {
                        throw new RuntimeException("No tienes permiso sobre este pedido");
                }
                if (!pedido.getEstado().equals("borrador")) {
                        throw new RuntimeException("El pedido ya fue confirmado");
                }

                List<ItemPedido> items = itemsPedidoRepository.findByPedido(pedido);
                if (items.isEmpty()) {
                        throw new RuntimeException("El carrito está vacío");
                }

                // Verificar stock y descontar
                for (ItemPedido item : items) {
                        Variantes_productos variante = item.getVariante();
                        if (variante.getStock() < item.getCantidad()) {
                                throw new RuntimeException(
                                                "Stock insuficiente para: " + variante.getNombre()
                                                                + ". Disponible: " + variante.getStock());
                        }
                        variante.setStock(variante.getStock() - item.getCantidad());
                        // Actualizar nivel de alerta
                        if (variante.getStock() == 0) {
                                variante.setNivelAlerta("critico");
                        } else if (variante.getStock() <= variante.getStockMinimo()) {
                                variante.setNivelAlerta("bajo");
                        } else {
                                variante.setNivelAlerta("ok");
                        }
                        variantesRepository.save(variante);
                }

                pedido.setEstado("confirmado");
                return convertToDTO(pedidosRepository.save(pedido));
        }

        // Generar mensaje para WhatsApp o Telegram
        @Override
        public MensajePedidoDTO generarMensaje(String correo, UUID pedidoId) {
                Clientes cliente = getClientePorCorreo(correo);
                Pedidos pedido = pedidosRepository.findById(pedidoId)
                                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

                if (!pedido.getCliente().getId().equals(cliente.getId())) {
                        throw new RuntimeException("No tienes permiso sobre este pedido");
                }

                List<ItemPedido> items = itemsPedidoRepository.findByPedido(pedido);
                String nombreCliente = cliente.getUsuarios().getNombre()
                                + " " + cliente.getUsuarios().getApellido();

                StringBuilder sb = new StringBuilder();
                sb.append("🐾 *Pedido SpaPet*\n");
                sb.append("Cliente: ").append(nombreCliente).append("\n");
                sb.append("─────────────────\n");

                for (ItemPedido item : items) {
                        sb.append("• ")
                                        .append(item.getVariante().getProducto().getNombre())
                                        .append(" - ").append(item.getVariante().getNombre())
                                        .append(" x").append(item.getCantidad())
                                        .append(" → Bs. ").append(
                                                        item.getPrecioUnitario()
                                                                        .multiply(BigDecimal
                                                                                        .valueOf(item.getCantidad())))
                                        .append("\n");
                }

                sb.append("─────────────────\n");
                sb.append("Subtotal: Bs. ").append(pedido.getSubtotal()).append("\n");
                if (pedido.getDescuento().compareTo(BigDecimal.ZERO) > 0) {
                        sb.append("Descuento: Bs. ").append(pedido.getDescuento()).append("\n");
                }
                sb.append("*TOTAL: Bs. ").append(pedido.getTotal()).append("*\n");

                if (pedido.getDireccionEntrega() != null) {
                        sb.append("📍 Entrega: ").append(pedido.getDireccionEntrega()).append("\n");
                }

                String mensaje = sb.toString();
                String canal = cliente.getCanalPreferido();

                // Guardar mensaje en el pedido
                pedido.setMensajeEnviado(mensaje);
                pedidosRepository.save(pedido);

                String linkWhatsapp = null;
                String linkTelegram = null;

                if ("whatsapp".equals(canal)) {
                        String encoded = URLEncoder.encode(mensaje, StandardCharsets.UTF_8);
                        linkWhatsapp = "https://wa.me/?text=" + encoded;
                } else if ("telegram".equals(canal)) {
                        String encoded = URLEncoder.encode(mensaje, StandardCharsets.UTF_8);
                        linkTelegram = "https://t.me/share/url?text=" + encoded;
                }

                return MensajePedidoDTO.builder()
                                .canal(canal)
                                .mensaje(mensaje)
                                .linkWhatsapp(linkWhatsapp)
                                .linkTelegram(linkTelegram)
                                .build();
        }

        // =============================================
        // HELPERS
        // =============================================

        private void recalcularTotales(Pedidos pedido) {
                List<ItemPedido> items = itemsPedidoRepository.findByPedido(pedido);
                BigDecimal subtotal = items.stream()
                                .map(i -> i.getPrecioUnitario().multiply(BigDecimal.valueOf(i.getCantidad())))
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                pedido.setSubtotal(subtotal);
                pedido.setTotal(subtotal.subtract(pedido.getDescuento()));
                pedidosRepository.save(pedido);
        }

        private CarritoDTO convertToCarritoDTO(Pedidos pedido) {
                List<ItemCarritoDTO> items = itemsPedidoRepository.findByPedido(pedido)
                                .stream()
                                .map(item -> ItemCarritoDTO.builder()
                                                .itemId(item.getId())
                                                .varianteId(item.getVariante().getId())
                                                .varianteNombre(item.getVariante().getNombre())
                                                .productoNombre(item.getVariante().getProducto().getNombre())
                                                .productoImagenUrl(item.getVariante().getProducto().getImagenUrl())
                                                .cantidad(item.getCantidad())
                                                .precioUnitario(item.getPrecioUnitario())
                                                .subtotal(item.getPrecioUnitario()
                                                                .multiply(BigDecimal.valueOf(item.getCantidad())))
                                                .build())
                                .collect(Collectors.toList());

                return CarritoDTO.builder()
                                .pedidoId(pedido.getId())
                                .estado(pedido.getEstado())
                                .items(items)
                                .subtotal(pedido.getSubtotal())
                                .descuento(pedido.getDescuento())
                                .total(pedido.getTotal())
                                .direccionEntrega(pedido.getDireccionEntrega())
                                .notas(pedido.getNotas())
                                .build();
        }

        private PedidosDTO convertToDTO(Pedidos pedido) {
                return PedidosDTO.builder()
                                .id(pedido.getId())
                                .clienteId(pedido.getCliente() != null ? pedido.getCliente().getId() : null)
                                .clienteNombre(pedido.getCliente() != null && pedido.getCliente().getUsuarios() != null
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