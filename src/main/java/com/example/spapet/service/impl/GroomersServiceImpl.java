package com.example.spapet.service.impl;

import com.example.spapet.dto.GroomersDTO;

import com.example.spapet.model.Groomers;
import com.example.spapet.model.Usuarios;

import com.example.spapet.repository.GroomersRepository;
import com.example.spapet.repository.UsuariosRepository;

import com.example.spapet.dto.CitasDTO;
import com.example.spapet.model.Citas;
import com.example.spapet.repository.CitasRepository;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import com.example.spapet.service.GroomersService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroomersServiceImpl implements GroomersService {

        private final GroomersRepository groomersRepository;
        private final UsuariosRepository usuariosRepository;
        private final CitasRepository citasRepository;

        @Override
        public List<GroomersDTO> obtenerTodos() {

                return groomersRepository.findAll()
                                .stream()
                                .map(this::convertToDTO)
                                .collect(Collectors.toList());
        }

        @Override
        public GroomersDTO obtenerPorId(UUID id) {

                Groomers groomer = groomersRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Groomer no encontrado"));

                return convertToDTO(groomer);
        }

        @Override
        public GroomersDTO crear(GroomersDTO dto) {

                Usuarios usuario = usuariosRepository.findById(dto.getUsuarioId())
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                Groomers groomer = Groomers.builder()
                                .usuarios(usuario)
                                .especialidades(dto.getEspecialidades())
                                .capacidadMax(dto.getCapacidadMax())
                                .activo(dto.getActivo())
                                .build();

                Groomers groomerGuardado = groomersRepository.save(groomer);

                return convertToDTO(groomerGuardado);
        }

        @Override
        public GroomersDTO actualizar(UUID id, GroomersDTO dto) {

                Groomers groomer = groomersRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Groomer no encontrado"));

                Usuarios usuario = usuariosRepository.findById(dto.getUsuarioId())
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                groomer.setUsuarios(usuario);
                groomer.setEspecialidades(dto.getEspecialidades());
                groomer.setCapacidadMax(dto.getCapacidadMax());
                groomer.setActivo(dto.getActivo());

                Groomers groomerActualizado = groomersRepository.save(groomer);

                return convertToDTO(groomerActualizado);
        }

        @Override
        public void eliminar(UUID id) {

                Groomers groomer = groomersRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Groomer no encontrado"));

                groomersRepository.delete(groomer);
        }

        // =========================
        // CONVERTERS
        // =========================

        private GroomersDTO convertToDTO(Groomers groomer) {

                return GroomersDTO.builder()
                                .id(groomer.getId())

                                .usuarioId(
                                                groomer.getUsuarios() != null
                                                                ? groomer.getUsuarios().getId()
                                                                : null)

                                .usuarioNombre(
                                                groomer.getUsuarios() != null
                                                                ? groomer.getUsuarios().getNombre()
                                                                : null)

                                .usuarioApellido(
                                                groomer.getUsuarios() != null
                                                                ? groomer.getUsuarios().getApellido()
                                                                : null)

                                .usuarioCorreo(
                                                groomer.getUsuarios() != null
                                                                ? groomer.getUsuarios().getCorreo()
                                                                : null)

                                .especialidades(groomer.getEspecialidades())
                                .capacidadMax(groomer.getCapacidadMax())
                                .activo(groomer.getActivo())
                                .creadoEn(groomer.getCreadoEn())
                                .actualizadoEn(groomer.getActualizadoEn())
                                .build();
        }

        @Override
        public List<CitasDTO> listarCitasPorCorreo(String correo) {
                Usuarios usuario = usuariosRepository.findByCorreo(correo)
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                Groomers groomer = groomersRepository.findByUsuariosId(usuario.getId())
                                .orElseThrow(() -> new RuntimeException("Groomer no encontrado"));
                return citasRepository.findByGroomersId(groomer.getId())
                                .stream().map(this::toCitaDTO).collect(Collectors.toList());
        }

        @Override
        public List<CitasDTO> listarCitasHoy(String correo) {
                Usuarios usuario = usuariosRepository.findByCorreo(correo)
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                Groomers groomer = groomersRepository.findByUsuariosId(usuario.getId())
                                .orElseThrow(() -> new RuntimeException("Groomer no encontrado"));
                OffsetDateTime inicio = LocalDate.now().atStartOfDay()
                                .atOffset(OffsetDateTime.now().getOffset());
                OffsetDateTime fin = inicio.plusDays(1);
                return citasRepository.findByGroomersIdAndFecha(groomer.getId(), inicio, fin)
                                .stream().map(this::toCitaDTO).collect(Collectors.toList());
        }

        private CitasDTO toCitaDTO(Citas c) {
                return CitasDTO.builder()
                                .id(c.getId())
                                .mascotaId(c.getMascotas().getId())
                                .mascotaNombre(c.getMascotas().getNombre())
                                .mascotaEspecie(c.getMascotas().getEspecie())
                                .mascotaTemperamento(c.getMascotas().getTemperamento())
                                .mascotaAlergias(c.getMascotas().getAlergias())
                                .mascotaRestricciones(c.getMascotas().getRestricciones())
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
                                .creadoEn(c.getCreadoEn())
                                .build();
        }
}