package com.example.spapet.service.impl;

import com.example.spapet.dto.Bloqueos_agendaDTO;
import com.example.spapet.model.Bloqueos_agenda;
import com.example.spapet.model.Groomers;
import com.example.spapet.model.Usuarios;
import com.example.spapet.repository.Bloqueos_agendaRepository;
import com.example.spapet.repository.GroomersRepository;
import com.example.spapet.repository.UsuariosRepository;
import com.example.spapet.service.Bloqueos_agendaService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class Bloqueos_agendaServiceImpl implements Bloqueos_agendaService {

    private final Bloqueos_agendaRepository bloqueosAgendaRepository;
    private final GroomersRepository groomersRepository;
    private final UsuariosRepository usuariosRepository;

    @Override
    public List<Bloqueos_agendaDTO> obtenerTodos() {

        return bloqueosAgendaRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Bloqueos_agendaDTO obtenerPorId(UUID id) {

        Bloqueos_agenda bloqueo = bloqueosAgendaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bloqueo de agenda no encontrado"));

        return convertToDTO(bloqueo);
    }

    @Override
    public Bloqueos_agendaDTO crear(Bloqueos_agendaDTO dto) {

        Groomers groomer = null;
        Usuarios creadoPor = null;

        if (dto.getGroomerId() != null) {

            groomer = groomersRepository.findById(dto.getGroomerId())
                    .orElseThrow(() -> new RuntimeException("Groomer no encontrado"));
        }

        if (dto.getCreadoPorId() != null) {

            creadoPor = usuariosRepository.findById(dto.getCreadoPorId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        }

        Bloqueos_agenda bloqueo = Bloqueos_agenda.builder()
                .groomers(groomer)
                .fechaInicio(dto.getFechaInicio())
                .fechaFin(dto.getFechaFin())
                .motivo(dto.getMotivo())
                .creadoPor(creadoPor)
                .build();

        Bloqueos_agenda bloqueoGuardado = bloqueosAgendaRepository.save(bloqueo);

        return convertToDTO(bloqueoGuardado);
    }

    @Override
    public Bloqueos_agendaDTO actualizar(UUID id, Bloqueos_agendaDTO dto) {

        Bloqueos_agenda bloqueo = bloqueosAgendaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bloqueo de agenda no encontrado"));

        Groomers groomer = null;
        Usuarios creadoPor = null;

        if (dto.getGroomerId() != null) {

            groomer = groomersRepository.findById(dto.getGroomerId())
                    .orElseThrow(() -> new RuntimeException("Groomer no encontrado"));
        }

        if (dto.getCreadoPorId() != null) {

            creadoPor = usuariosRepository.findById(dto.getCreadoPorId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        }

        bloqueo.setGroomers(groomer);
        bloqueo.setFechaInicio(dto.getFechaInicio());
        bloqueo.setFechaFin(dto.getFechaFin());
        bloqueo.setMotivo(dto.getMotivo());
        bloqueo.setCreadoPor(creadoPor);

        Bloqueos_agenda bloqueoActualizado = bloqueosAgendaRepository.save(bloqueo);

        return convertToDTO(bloqueoActualizado);
    }

    @Override
    public void eliminar(UUID id) {

        Bloqueos_agenda bloqueo = bloqueosAgendaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bloqueo de agenda no encontrado"));

        bloqueosAgendaRepository.delete(bloqueo);
    }

    // =========================
    // CONVERTERS
    // =========================

    private Bloqueos_agendaDTO convertToDTO(Bloqueos_agenda bloqueo) {

        return Bloqueos_agendaDTO.builder()
                .id(bloqueo.getId())

                .groomerId(
                        bloqueo.getGroomers() != null
                                ? bloqueo.getGroomers().getId()
                                : null)

                .groomerNombre(
                        bloqueo.getGroomers() != null
                                ? bloqueo.getGroomers().getUsuarios().getNombre()
                                : null)

                .fechaInicio(bloqueo.getFechaInicio())
                .fechaFin(bloqueo.getFechaFin())
                .motivo(bloqueo.getMotivo())

                .creadoPorId(
                        bloqueo.getCreadoPor() != null
                                ? bloqueo.getCreadoPor().getId()
                                : null)

                .creadoPorNombre(
                        bloqueo.getCreadoPor() != null
                                ? bloqueo.getCreadoPor().getNombre()
                                : null)

                .creadoEn(bloqueo.getCreadoEn())
                .build();
    }
}