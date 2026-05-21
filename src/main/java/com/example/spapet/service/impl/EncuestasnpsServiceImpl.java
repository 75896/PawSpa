package com.example.spapet.service.impl;

import com.example.spapet.dto.EncuestasnpsDTO;

import com.example.spapet.model.Citas;
import com.example.spapet.model.Clientes;
import com.example.spapet.model.Encuestasnps;

import com.example.spapet.repository.CitasRepository;
import com.example.spapet.repository.ClientesRepository;
import com.example.spapet.repository.EncuestasnpsRepository;

import com.example.spapet.service.EncuestasnpsService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EncuestasnpsServiceImpl implements EncuestasnpsService {

    private final EncuestasnpsRepository encuestasnpsRepository;
    private final CitasRepository citasRepository;
    private final ClientesRepository clientesRepository;

    @Override
    public List<EncuestasnpsDTO> obtenerTodos() {

        return encuestasnpsRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EncuestasnpsDTO obtenerPorId(UUID id) {

        Encuestasnps encuesta = encuestasnpsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Encuesta NPS no encontrada"));

        return convertToDTO(encuesta);
    }

    @Override
    public EncuestasnpsDTO crear(EncuestasnpsDTO dto) {

        Citas cita = citasRepository.findById(dto.getCitaId())
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        Clientes cliente = clientesRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Encuestasnps encuesta = Encuestasnps.builder()
                .citas(cita)
                .clientes(cliente)
                .puntuacion(dto.getPuntuacion())
                .comentario(dto.getComentario())
                .build();

        Encuestasnps encuestaGuardada = encuestasnpsRepository.save(encuesta);

        return convertToDTO(encuestaGuardada);
    }

    @Override
    public EncuestasnpsDTO actualizar(UUID id, EncuestasnpsDTO dto) {

        Encuestasnps encuesta = encuestasnpsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Encuesta NPS no encontrada"));

        Citas cita = citasRepository.findById(dto.getCitaId())
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        Clientes cliente = clientesRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        encuesta.setCitas(cita);
        encuesta.setClientes(cliente);
        encuesta.setPuntuacion(dto.getPuntuacion());
        encuesta.setComentario(dto.getComentario());

        Encuestasnps encuestaActualizada = encuestasnpsRepository.save(encuesta);

        return convertToDTO(encuestaActualizada);
    }

    @Override
    public void eliminar(UUID id) {

        Encuestasnps encuesta = encuestasnpsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Encuesta NPS no encontrada"));

        encuestasnpsRepository.delete(encuesta);
    }

    // =========================
    // CONVERTERS
    // =========================

    private EncuestasnpsDTO convertToDTO(Encuestasnps encuesta) {

        return EncuestasnpsDTO.builder()
                .id(encuesta.getId())

                .citaId(
                        encuesta.getCitas() != null
                                ? encuesta.getCitas().getId()
                                : null)

                .clienteId(
                        encuesta.getClientes() != null
                                ? encuesta.getClientes().getId()
                                : null)

                .clienteNombre(
                        encuesta.getClientes() != null
                                && encuesta.getClientes().getUsuarios() != null
                                        ? encuesta.getClientes().getUsuarios().getNombre()
                                        : null)

                .puntuacion(encuesta.getPuntuacion())
                .comentario(encuesta.getComentario())
                .respondidaEn(encuesta.getRespondidaEn())
                .build();
    }
}