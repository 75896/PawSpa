package com.example.spapet.service.impl;

import com.example.spapet.dto.CitasDTO;
import com.example.spapet.model.*;
import com.example.spapet.repository.*;
import com.example.spapet.service.CitasService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CitasServiceImpl implements CitasService {

        private final CitasRepository citaRepository;
        private final ClientesRepository clienteRepository;
        private final UsuariosRepository usuarioRepository;
        private final MascotasRepository mascotasRepository;
        private final ServiciosRepository serviciosRepository;
        private final GroomersRepository groomersRepository;

        @Override
        public List<CitasDTO> obtenerTodos() {
                return citaRepository.findAll()
                                .stream().map(this::toDTO).collect(Collectors.toList());
        }

        @Override
        public CitasDTO obtenerPorId(UUID id) {
                Citas c = citaRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));
                return toDTO(c);
        }

        @Override
        public CitasDTO crear(CitasDTO dto) {
                throw new UnsupportedOperationException("Usar endpoint de agenda");
        }

        @Override
        public CitasDTO actualizar(UUID id, CitasDTO dto) {
                throw new UnsupportedOperationException("Usar endpoint de agenda");
        }

        @Override
        public void eliminar(UUID id) {
                citaRepository.deleteById(id);
        }

        @Override
        public List<CitasDTO> listarPorCorreo(String correo) {
                Usuarios usuario = usuarioRepository.findByCorreo(correo)
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                Clientes cliente = clienteRepository.findByUsuariosId(usuario.getId())
                                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
                return citaRepository.findByMascotaClienteId(cliente.getId())
                                .stream().map(this::toDTO).collect(Collectors.toList());
        }

        @Override
        @Transactional
        public CitasDTO solicitarCita(String correo, CitasDTO dto) {
                Usuarios usuario = usuarioRepository.findByCorreo(correo)
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                Clientes cliente = clienteRepository.findByUsuariosId(usuario.getId())
                                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

                Mascotas mascota = mascotasRepository.findById(dto.getMascotaId())
                                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));

                if (!mascota.getClientes().getId().equals(cliente.getId())) {
                        throw new RuntimeException("La mascota no pertenece a este cliente");
                }

                Servicios servicio = serviciosRepository.findById(dto.getServicioId())
                                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

                // Calcular duración con ajuste por raza
                int duracion = servicio.getDuracionMin();
                if (mascota.getRazas() != null) {
                        duracion += mascota.getRazas().getAjusteDuracion();
                }
                if ("nervioso".equals(mascota.getTemperamento()) ||
                                "agresivo".equals(mascota.getTemperamento())) {
                        duracion += 15;
                }

                Citas cita = Citas.builder()
                                .mascotas(mascota)
                                .servicios(servicio)
                                .fechaInicio(dto.getFechaInicio())
                                .fechaFin(dto.getFechaInicio().plusMinutes(duracion))
                                .estado("pendiente")
                                .notasCliente(dto.getNotasCliente())
                                .build();

                // Asignar groomer si el cliente eligió uno
                if (dto.getGroomerId() != null) {
                        groomersRepository.findById(dto.getGroomerId())
                                        .ifPresent(cita::setGroomers);
                } else {
                        // Asignar primer groomer activo disponible
                        groomersRepository.findByActivoTrue().stream()
                                        .findFirst()
                                        .ifPresent(cita::setGroomers);
                }

                if (cita.getGroomers() == null) {
                        throw new RuntimeException("No hay groomers disponibles");
                }

                return toDTO(citaRepository.save(cita));
        }

        private CitasDTO toDTO(Citas c) {
                CitasDTO.CitasDTOBuilder builder = CitasDTO.builder()
                                .id(c.getId())
                                .mascotaId(c.getMascotas().getId())
                                .mascotaNombre(c.getMascotas().getNombre())
                                .mascotaEspecie(c.getMascotas().getEspecie())
                                .mascotaTemperamento(c.getMascotas().getTemperamento())
                                .mascotaAlergias(c.getMascotas().getAlergias())
                                .mascotaRestricciones(c.getMascotas().getRestricciones())
                                .servicioId(c.getServicios().getId())
                                .servicioNombre(c.getServicios().getNombre())
                                .servicioDuracionMin(c.getServicios().getDuracionMin())
                                .fechaInicio(c.getFechaInicio())
                                .fechaFin(c.getFechaFin())
                                .estado(c.getEstado())
                                .notasCliente(c.getNotasCliente())
                                .precioAcordado(c.getPrecioAcordado())
                                .creadoEn(c.getCreadoEn());

                // Groomer puede ser null si recepción aún no lo asignó
                if (c.getGroomers() != null) {
                        builder.groomerId(c.getGroomers().getId())
                                        .groomerNombre(c.getGroomers().getUsuarios().getNombre())
                                        .groomerApellido(c.getGroomers().getUsuarios().getApellido());
                }

                return builder.build();
        }
}