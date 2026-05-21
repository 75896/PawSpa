package com.example.spapet.service;

import com.example.spapet.dto.Bloqueos_agendaDTO;

import java.util.List;
import java.util.UUID;

public interface Bloqueos_agendaService {

    List<Bloqueos_agendaDTO> obtenerTodos();

    Bloqueos_agendaDTO obtenerPorId(UUID id);

    Bloqueos_agendaDTO crear(Bloqueos_agendaDTO bloqueosAgendaDTO);

    Bloqueos_agendaDTO actualizar(UUID id, Bloqueos_agendaDTO bloqueosAgendaDTO);

    void eliminar(UUID id);
}