package com.example.spapet.service.impl;

import com.example.spapet.dto.Disponibilidad_groomerDTO;

import com.example.spapet.model.Disponibilidad_groomer;
import com.example.spapet.model.Groomers;

import com.example.spapet.repository.Disponibilidad_groomerRepository;
import com.example.spapet.repository.GroomersRepository;

import com.example.spapet.service.Disponibilidad_groomerService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class Disponibilidad_groomerServiceImpl implements Disponibilidad_groomerService {

    private final Disponibilidad_groomerRepository disponibilidadGroomerRepository;
    private final GroomersRepository groomersRepository;

    @Override
    public List<Disponibilidad_groomerDTO> obtenerTodos() {

        return disponibilidadGroomerRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Disponibilidad_groomerDTO obtenerPorId(UUID id) {

        Disponibilidad_groomer disponibilidad = disponibilidadGroomerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Disponibilidad no encontrada"));

        return convertToDTO(disponibilidad);
    }

    @Override
    public Disponibilidad_groomerDTO crear(Disponibilidad_groomerDTO dto) {

        Groomers groomer = groomersRepository.findById(dto.getGroomerId())
                .orElseThrow(() -> new RuntimeException("Groomer no encontrado"));

        Disponibilidad_groomer disponibilidad = Disponibilidad_groomer.builder()
                .groomers(groomer)
                .diaSemana(dto.getDiaSemana())
                .horaInicio(dto.getHoraInicio())
                .horaFin(dto.getHoraFin())
                .activo(dto.getActivo())
                .build();

        Disponibilidad_groomer disponibilidadGuardada = disponibilidadGroomerRepository.save(disponibilidad);

        return convertToDTO(disponibilidadGuardada);
    }

    @Override
    public Disponibilidad_groomerDTO actualizar(UUID id, Disponibilidad_groomerDTO dto) {

        Disponibilidad_groomer disponibilidad = disponibilidadGroomerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Disponibilidad no encontrada"));

        Groomers groomer = groomersRepository.findById(dto.getGroomerId())
                .orElseThrow(() -> new RuntimeException("Groomer no encontrado"));

        disponibilidad.setGroomers(groomer);
        disponibilidad.setDiaSemana(dto.getDiaSemana());
        disponibilidad.setHoraInicio(dto.getHoraInicio());
        disponibilidad.setHoraFin(dto.getHoraFin());
        disponibilidad.setActivo(dto.getActivo());

        Disponibilidad_groomer disponibilidadActualizada = disponibilidadGroomerRepository.save(disponibilidad);

        return convertToDTO(disponibilidadActualizada);
    }

    @Override
    public void eliminar(UUID id) {

        Disponibilidad_groomer disponibilidad = disponibilidadGroomerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Disponibilidad no encontrada"));

        disponibilidadGroomerRepository.delete(disponibilidad);
    }

    // =========================
    // CONVERTERS
    // =========================

    private Disponibilidad_groomerDTO convertToDTO(Disponibilidad_groomer disponibilidad) {

        return Disponibilidad_groomerDTO.builder()
                .id(disponibilidad.getId())

                .groomerId(
                        disponibilidad.getGroomers() != null
                                ? disponibilidad.getGroomers().getId()
                                : null)

                .groomerNombre(
                        disponibilidad.getGroomers() != null
                                ? disponibilidad.getGroomers().getUsuarios().getNombre()
                                : null)

                .diaSemana(disponibilidad.getDiaSemana())
                .horaInicio(disponibilidad.getHoraInicio())
                .horaFin(disponibilidad.getHoraFin())
                .activo(disponibilidad.getActivo())
                .build();
    }
}