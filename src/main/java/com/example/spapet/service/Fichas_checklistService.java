package com.example.spapet.service;

import com.example.spapet.dto.Fichas_checklistDTO;

import java.util.List;
import java.util.UUID;

public interface Fichas_checklistService {

    List<Fichas_checklistDTO> obtenerTodos();

    Fichas_checklistDTO obtenerPorId(UUID id);

    Fichas_checklistDTO crear(Fichas_checklistDTO fichasChecklistDTO);

    Fichas_checklistDTO actualizar(UUID id, Fichas_checklistDTO fichasChecklistDTO);

    void eliminar(UUID id);
}