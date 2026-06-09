package com.example.spapet.controller;

import com.example.spapet.dto.AgregarItemDTO;
import com.example.spapet.dto.CarritoDTO;
import com.example.spapet.dto.CitasDTO;
import com.example.spapet.dto.MascotasDTO;
import com.example.spapet.dto.MensajePedidoDTO;
import com.example.spapet.dto.PedidosDTO;
import com.example.spapet.dto.ServiciosDTO;
import com.example.spapet.repository.ServiciosRepository;
import com.example.spapet.service.CitasService;
import com.example.spapet.service.MascotasService;
import com.example.spapet.service.PedidosService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cliente")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ClientesController {

    private final MascotasService mascotasService;
    private final CitasService citasService;
    private final PedidosService pedidosService;
    private final ServiciosRepository serviciosRepository;

    @GetMapping("/mascotas")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<List<MascotasDTO>> misMascotas(
            @AuthenticationPrincipal String correo) {
        return ResponseEntity.ok(mascotasService.listarPorCorreo(correo));
    }

    @PostMapping("/mascotas")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<MascotasDTO> agregarMascota(
            @AuthenticationPrincipal String correo,
            @RequestBody MascotasDTO dto) {
        return ResponseEntity.ok(mascotasService.crearParaCliente(correo, dto));
    }

    @GetMapping("/citas")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<List<CitasDTO>> misCitas(
            @AuthenticationPrincipal String correo) {
        return ResponseEntity.ok(citasService.listarPorCorreo(correo));
    }

    @PutMapping("/mascotas/{id}")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<MascotasDTO> editarMascota(
            @PathVariable UUID id,
            @RequestBody MascotasDTO dto) {
        return ResponseEntity.ok(mascotasService.actualizar(id, dto));
    }

    @GetMapping("/{clienteId}/mascotas")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION')")
    public ResponseEntity<List<MascotasDTO>> mascotasPorCliente(
            @PathVariable UUID clienteId) {
        return ResponseEntity.ok(mascotasService.listarPorClienteId(clienteId));
    }

    @PostMapping("/citas")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<CitasDTO> solicitarCita(
            @AuthenticationPrincipal String correo,
            @RequestBody CitasDTO dto) {
        return ResponseEntity.ok(citasService.solicitarCita(correo, dto));
    }

    @GetMapping("/servicios")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<List<ServiciosDTO>> listarServicios() {
        return ResponseEntity.ok(
                serviciosRepository.findByActivoTrue()
                        .stream()
                        .map(s -> ServiciosDTO.builder()
                                .id(s.getId())
                                .nombre(s.getNombre())
                                .descripcion(s.getDescripcion())
                                .duracionMin(s.getDuracionMin())
                                .precioBase(s.getPrecioBase())
                                .permiteDobleBooking(s.getPermiteDobleBooking())
                                .activo(s.getActivo())
                                .build())
                        .collect(Collectors.toList()));
    }

    // =============================================
    // TIENDA — CARRITO Y PEDIDOS
    // =============================================

    // Obtener o crear carrito activo (borrador)
    @PostMapping("/carrito")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADMIN')")
    public ResponseEntity<CarritoDTO> obtenerOCrearCarrito(
            @AuthenticationPrincipal String correo) {
        System.out.println("=== /carrito llamado por: " + correo);
        return ResponseEntity.ok(pedidosService.crearCarrito(correo));
    }

    // Ver carrito actual con items y totales
    @GetMapping("/carrito")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<CarritoDTO> verCarrito(
            @AuthenticationPrincipal String correo) {
        return ResponseEntity.ok(pedidosService.obtenerCarrito(correo));
    }

    // Agregar item al carrito
    @PostMapping("/carrito/{pedidoId}/items")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<CarritoDTO> agregarItem(
            @AuthenticationPrincipal String correo,
            @PathVariable UUID pedidoId,
            @Valid @RequestBody AgregarItemDTO dto) {
        return ResponseEntity.ok(pedidosService.agregarItem(correo, pedidoId, dto));
    }

    // Cambiar cantidad de un item
    @PutMapping("/carrito/{pedidoId}/items/{itemId}")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<CarritoDTO> actualizarItem(
            @AuthenticationPrincipal String correo,
            @PathVariable UUID pedidoId,
            @PathVariable UUID itemId,
            @RequestParam Integer cantidad) {
        return ResponseEntity.ok(pedidosService.actualizarItem(correo, pedidoId, itemId, cantidad));
    }

    // Eliminar item del carrito
    @DeleteMapping("/carrito/{pedidoId}/items/{itemId}")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<CarritoDTO> eliminarItem(
            @AuthenticationPrincipal String correo,
            @PathVariable UUID pedidoId,
            @PathVariable UUID itemId) {
        return ResponseEntity.ok(pedidosService.eliminarItem(correo, pedidoId, itemId));
    }

    // Confirmar pedido y descontar stock
    @PostMapping("/carrito/{pedidoId}/confirmar")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<PedidosDTO> confirmarPedido(
            @AuthenticationPrincipal String correo,
            @PathVariable UUID pedidoId) {
        return ResponseEntity.ok(pedidosService.confirmarPedido(correo, pedidoId));
    }

    // Generar mensaje para WhatsApp o Telegram
    @GetMapping("/carrito/{pedidoId}/mensaje")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<MensajePedidoDTO> generarMensaje(
            @AuthenticationPrincipal String correo,
            @PathVariable UUID pedidoId) {
        return ResponseEntity.ok(pedidosService.generarMensaje(correo, pedidoId));
    }

    // Historial de pedidos del cliente
    @GetMapping("/pedidos")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<List<PedidosDTO>> misPedidos(
            @AuthenticationPrincipal String correo) {
        return ResponseEntity.ok(pedidosService.obtenerMisPedidos(correo));
    }
}