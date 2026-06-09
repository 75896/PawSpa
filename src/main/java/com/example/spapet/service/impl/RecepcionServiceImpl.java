package com.example.spapet.service.impl;

import com.example.spapet.dto.*;
import com.example.spapet.model.*;
import com.example.spapet.repository.*;
import com.example.spapet.registro.auth.MailService;
import com.example.spapet.service.RecepcionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecepcionServiceImpl implements RecepcionService {

        private final CitasRepository citasRepository;
        private final MascotasRepository mascotasRepository;
        private final GroomersRepository groomersRepository;
        private final ServiciosRepository serviciosRepository;
        private final Bloqueos_agendaRepository bloqueosRepository;
        private final UsuariosRepository usuariosRepository;
        private final ClientesRepository clientesRepository;
        private final MailService mailService;

        @Override
        public List<CitasDTO> listarTodasCitas() {
                return citasRepository.findAll()
                                .stream().map(this::toCitaDTO).collect(Collectors.toList());
        }

        @Override
        public List<CitasDTO> listarCitasHoy() {
                OffsetDateTime inicio = LocalDate.now().atStartOfDay()
                                .atOffset(OffsetDateTime.now().getOffset());
                OffsetDateTime fin = inicio.plusDays(1);
                return citasRepository.findByFechaInicioBetween(inicio, fin)
                                .stream().map(this::toCitaDTO).collect(Collectors.toList());
        }

        @Override
        public List<CitasDTO> listarCitasPorFecha(LocalDate fecha) {
                OffsetDateTime inicio = fecha.atStartOfDay()
                                .atOffset(OffsetDateTime.now().getOffset());
                OffsetDateTime fin = inicio.plusDays(1);
                return citasRepository.findByFechaInicioBetween(inicio, fin)
                                .stream().map(this::toCitaDTO).collect(Collectors.toList());
        }

        @Override
        @Transactional
        public CitasDTO agendarCita(CitasDTO dto) {
                Mascotas mascota = mascotasRepository.findById(dto.getMascotaId())
                                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));
                Groomers groomer = groomersRepository.findById(dto.getGroomerId())
                                .orElseThrow(() -> new RuntimeException("Groomer no encontrado"));
                Servicios servicio = serviciosRepository.findById(dto.getServicioId())
                                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

                // Calcular duración con ajuste por raza
                int duracion = servicio.getDuracionMin();
                if (mascota.getRazas() != null) {
                        duracion += mascota.getRazas().getAjusteDuracion();
                }

                // Ajuste por temperamento
                if ("nervioso".equals(mascota.getTemperamento()) ||
                                "agresivo".equals(mascota.getTemperamento())) {
                        duracion += 15;
                }

                OffsetDateTime fechaFin = dto.getFechaInicio().plusMinutes(duracion);

                // Verificar solapamiento
                List<Citas> solapadas = citasRepository.findSolapadas(
                                groomer.getId(), dto.getFechaInicio(), fechaFin);

                if (!solapadas.isEmpty() && !servicio.getPermiteDobleBooking()) {
                        throw new RuntimeException("El groomer ya tiene una cita en ese horario");
                }

                // Verificar bloqueos
                List<Bloqueos_agenda> bloqueos = bloqueosRepository.findBloqueosSolapados(
                                groomer.getId(), dto.getFechaInicio(), fechaFin);

                if (!bloqueos.isEmpty()) {
                        throw new RuntimeException("El groomer tiene un bloqueo en ese horario");
                }

                Citas cita = Citas.builder()
                                .mascotas(mascota)
                                .groomers(groomer)
                                .servicios(servicio)
                                .fechaInicio(dto.getFechaInicio())
                                .fechaFin(fechaFin)
                                .estado("pendiente")
                                .precioAcordado(dto.getPrecioAcordado() != null
                                                ? dto.getPrecioAcordado()
                                                : servicio.getPrecioBase())
                                .notasCliente(dto.getNotasCliente())
                                .build();

                // Verificar bloqueos

                // ← AGREGAR AQUÍ — Verificar capacidad máxima del día
                OffsetDateTime inicioHoy = dto.getFechaInicio().toLocalDate().atStartOfDay()
                                .atOffset(dto.getFechaInicio().getOffset());
                OffsetDateTime finHoy = inicioHoy.plusDays(1);

                long citasDelDia = citasRepository.findByFechaInicioBetween(inicioHoy, finHoy)
                                .stream()
                                .filter(c -> c.getGroomers().getId().equals(groomer.getId()))
                                .filter(c -> !c.getEstado().equals("cancelada"))
                                .count();

                if (citasDelDia >= groomer.getCapacidadMax()) {
                        throw new RuntimeException("El groomer ha alcanzado su capacidad máxima del día ("
                                        + groomer.getCapacidadMax() + " citas)");
                }

                return toCitaDTO(citasRepository.save(cita));
        }

        // En el método cambiarEstado(), cuando estado es "confirmada"
        @Override
        @Transactional
        public CitasDTO cambiarEstado(UUID id, String estado) {
                Citas cita = citasRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));
                cita.setEstado(estado);
                citasRepository.save(cita);

                // Notificar confirmación
                if (estado.equals("confirmada")) {
                        try {
                                String correo = cita.getMascotas().getClientes().getUsuarios().getCorreo();
                                String nombre = cita.getMascotas().getClientes().getUsuarios().getNombre();
                                String mascota = cita.getMascotas().getNombre();
                                String servicio = cita.getServicios().getNombre();
                                String fecha = cita.getFechaInicio()
                                                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
                                mailService.enviarConfirmacionCita(correo, nombre, mascota, servicio, fecha);
                        } catch (Exception e) {
                                log.error("Error enviando confirmación: {}", e.getMessage());
                        }
                }

                return toCitaDTO(cita);
        }

        @Override
        @Transactional
        public CitasDTO cancelarCita(UUID id, String motivo) {
                Citas cita = citasRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));
                cita.setEstado("cancelada");
                cita.setCancelacionMotivo(motivo);
                return toCitaDTO(citasRepository.save(cita));
        }

        @Override
        public List<ServiciosDTO> listarServicios() {
                return serviciosRepository.findByActivoTrue()
                                .stream().map(this::toServicioDTO).collect(Collectors.toList());
        }

        @Override
        public List<GroomersDTO> listarGroomers() {
                return groomersRepository.findByActivoTrue()
                                .stream().map(this::toGroomerDTO).collect(Collectors.toList());
        }

        @Override
        public List<Bloqueos_agendaDTO> listarBloqueos() {
                return bloqueosRepository.findAll()
                                .stream().map(this::toBloqueoDTO).collect(Collectors.toList());
        }

        @Override
        @Transactional
        public Bloqueos_agendaDTO crearBloqueo(Bloqueos_agendaDTO dto) {
                Bloqueos_agenda bloqueo = Bloqueos_agenda.builder()
                                .fechaInicio(dto.getFechaInicio())
                                .fechaFin(dto.getFechaFin())
                                .motivo(dto.getMotivo())
                                .build();

                if (dto.getGroomerId() != null) {
                        groomersRepository.findById(dto.getGroomerId())
                                        .ifPresent(bloqueo::setGroomers);
                }

                return toBloqueoDTO(bloqueosRepository.save(bloqueo));
        }

        @Override
        @Transactional
        public void eliminarBloqueo(UUID id) {
                bloqueosRepository.deleteById(id);
        }

        // ── DTOs ──────────────────────────────────────────────

        private CitasDTO toCitaDTO(Citas c) {
                return CitasDTO.builder()
                                .id(c.getId())
                                .mascotaId(c.getMascotas().getId())
                                .mascotaNombre(c.getMascotas().getNombre())
                                .mascotaEspecie(c.getMascotas().getEspecie())
                                .mascotaTemperamento(c.getMascotas().getTemperamento())
                                .groomerId(c.getGroomers().getId())
                                .groomerNombre(c.getGroomers().getUsuarios().getNombre())
                                .groomerApellido(c.getGroomers().getUsuarios().getApellido())
                                .servicioId(c.getServicios().getId())
                                .servicioNombre(c.getServicios().getNombre())
                                .servicioDuracionMin(c.getServicios().getDuracionMin())
                                .fechaInicio(c.getFechaInicio())
                                .fechaFin(c.getFechaFin())
                                .estado(c.getEstado())
                                .precioAcordado(c.getPrecioAcordado())
                                .notasCliente(c.getNotasCliente())
                                .cancelacionMotivo(c.getCancelacionMotivo())
                                .creadoEn(c.getCreadoEn())
                                .build();
        }

        private ServiciosDTO toServicioDTO(Servicios s) {
                return ServiciosDTO.builder()
                                .id(s.getId())
                                .nombre(s.getNombre())
                                .descripcion(s.getDescripcion())
                                .duracionMin(s.getDuracionMin())
                                .precioBase(s.getPrecioBase())
                                .permiteDobleBooking(s.getPermiteDobleBooking())
                                .activo(s.getActivo())
                                .build();
        }

        private MascotasDTO toMascotaDTO(Mascotas m) {
                return MascotasDTO.builder()
                                .id(m.getId())
                                .clienteId(m.getClientes().getId())
                                .nombre(m.getNombre())
                                .especie(m.getEspecie())
                                .sexo(m.getSexo())
                                .temperamento(m.getTemperamento())
                                .pesoKg(m.getPesoKg())
                                .alergias(m.getAlergias())
                                .restricciones(m.getRestricciones())
                                .activa(m.getActiva())
                                .build();
        }

        private GroomersDTO toGroomerDTO(Groomers g) {
                return GroomersDTO.builder()
                                .id(g.getId())
                                .usuarioId(g.getUsuarios().getId())
                                .usuarioNombre(g.getUsuarios().getNombre())
                                .usuarioApellido(g.getUsuarios().getApellido())
                                .usuarioCorreo(g.getUsuarios().getCorreo())
                                .especialidades(g.getEspecialidades())
                                .capacidadMax(g.getCapacidadMax())
                                .activo(g.getActivo())
                                .build();
        }

        private Bloqueos_agendaDTO toBloqueoDTO(Bloqueos_agenda b) {
                return Bloqueos_agendaDTO.builder()
                                .id(b.getId())
                                .groomerId(b.getGroomers() != null ? b.getGroomers().getId() : null)
                                .groomerNombre(b.getGroomers() != null ? b.getGroomers().getUsuarios().getNombre()
                                                : null)
                                .groomerApellido(b.getGroomers() != null ? b.getGroomers().getUsuarios().getApellido()
                                                : null)
                                .fechaInicio(b.getFechaInicio())
                                .fechaFin(b.getFechaFin())
                                .motivo(b.getMotivo())
                                .creadoEn(b.getCreadoEn())
                                .build();
        }

        @Override
        public List<UsuariosDTO> listarClientes() {
                return usuariosRepository.findByRol("cliente")
                                .stream()
                                .map(u -> UsuariosDTO.builder()
                                                .id(u.getId())
                                                .nombre(u.getNombre())
                                                .apellido(u.getApellido())
                                                .correo(u.getCorreo())
                                                .telefono(u.getTelefono())
                                                .rol(u.getRol())
                                                .activo(u.getActivo())
                                                .build())
                                .collect(Collectors.toList());
        }

        @Override
        public List<MascotasDTO> listarMascotasPorUsuarioId(UUID usuarioId) {
                Clientes cliente = clientesRepository.findByUsuariosId(usuarioId)
                                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
                return mascotasRepository.findByClientesId(cliente.getId())
                                .stream().map(this::toMascotaDTO).collect(Collectors.toList());
        }

        @Override
        @Transactional
        public CitasDTO asignarGroomer(UUID citaId, UUID groomerId) {
                Citas cita = citasRepository.findById(citaId)
                                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));
                Groomers groomer = groomersRepository.findById(groomerId)
                                .orElseThrow(() -> new RuntimeException("Groomer no encontrado"));

                // Verificar solapamiento
                List<Citas> solapadas = citasRepository.findSolapadas(
                                groomer.getId(), cita.getFechaInicio(), cita.getFechaFin());
                if (!solapadas.isEmpty()) {
                        throw new RuntimeException("El groomer ya tiene una cita en ese horario");
                }

                cita.setGroomers(groomer);
                return toCitaDTO(citasRepository.save(cita));
        }
}