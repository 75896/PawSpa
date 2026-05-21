package com.example.spapet.service.impl;

import com.example.spapet.dto.NotificacionesDTO;

import com.example.spapet.model.Citas;
import com.example.spapet.model.Notificaciones;
import com.example.spapet.model.Usuarios;

import com.example.spapet.repository.CitasRepository;
import com.example.spapet.repository.NotificacionesRepository;
import com.example.spapet.repository.UsuariosRepository;

import com.example.spapet.service.NotificacionesService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificacionesServiceImpl implements NotificacionesService {

    private final NotificacionesRepository notificacionesRepository;
    private final UsuariosRepository usuariosRepository;
    private final CitasRepository citasRepository;

    @Override
    public List<NotificacionesDTO> obtenerTodos() {

        return notificacionesRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public NotificacionesDTO obtenerPorId(UUID id) {

        Notificaciones notificacion = notificacionesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada"));

        return convertToDTO(notificacion);
    }

    @Override
    public NotificacionesDTO crear(NotificacionesDTO dto) {

        Usuarios usuario = usuariosRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Citas cita = null;

        if (dto.getCitaId() != null) {

            cita = citasRepository.findById(dto.getCitaId())
                    .orElseThrow(() -> new RuntimeException("Cita no encontrada"));
        }

        Notificaciones notificacion = Notificaciones.builder()
                .usuarios(usuario)
                .citas(cita)
                .tipo(dto.getTipo())
                .canal(dto.getCanal())
                .estado(dto.getEstado())
                .asunto(dto.getAsunto())
                .cuerpo(dto.getCuerpo())
                .programadaPara(dto.getProgramadaPara())
                .enviadaEn(dto.getEnviadaEn())
                .errorDetalle(dto.getErrorDetalle())
                .build();

        Notificaciones notificacionGuardada = notificacionesRepository.save(notificacion);

        return convertToDTO(notificacionGuardada);
    }

    @Override
    public NotificacionesDTO actualizar(UUID id, NotificacionesDTO dto) {

        Notificaciones notificacion = notificacionesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada"));

        Usuarios usuario = usuariosRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Citas cita = null;

        if (dto.getCitaId() != null) {

            cita = citasRepository.findById(dto.getCitaId())
                    .orElseThrow(() -> new RuntimeException("Cita no encontrada"));
        }

        notificacion.setUsuarios(usuario);
        notificacion.setCitas(cita);
        notificacion.setTipo(dto.getTipo());
        notificacion.setCanal(dto.getCanal());
        notificacion.setEstado(dto.getEstado());
        notificacion.setAsunto(dto.getAsunto());
        notificacion.setCuerpo(dto.getCuerpo());
        notificacion.setProgramadaPara(dto.getProgramadaPara());
        notificacion.setEnviadaEn(dto.getEnviadaEn());
        notificacion.setErrorDetalle(dto.getErrorDetalle());

        Notificaciones notificacionActualizada = notificacionesRepository.save(notificacion);

        return convertToDTO(notificacionActualizada);
    }

    @Override
    public void eliminar(UUID id) {

        Notificaciones notificacion = notificacionesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada"));

        notificacionesRepository.delete(notificacion);
    }

    // =========================
    // CONVERTERS
    // =========================

    private NotificacionesDTO convertToDTO(Notificaciones notificacion) {

        return NotificacionesDTO.builder()
                .id(notificacion.getId())

                .usuarioId(
                        notificacion.getUsuarios() != null
                                ? notificacion.getUsuarios().getId()
                                : null)

                .usuarioNombre(
                        notificacion.getUsuarios() != null
                                ? notificacion.getUsuarios().getNombre()
                                : null)

                .citaId(
                        notificacion.getCitas() != null
                                ? notificacion.getCitas().getId()
                                : null)

                .tipo(notificacion.getTipo())
                .canal(notificacion.getCanal())
                .estado(notificacion.getEstado())
                .asunto(notificacion.getAsunto())
                .cuerpo(notificacion.getCuerpo())
                .programadaPara(notificacion.getProgramadaPara())
                .enviadaEn(notificacion.getEnviadaEn())
                .errorDetalle(notificacion.getErrorDetalle())
                .creadoEn(notificacion.getCreadoEn())
                .build();
    }
}