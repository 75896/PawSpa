package com.example.spapet.service;

import com.example.spapet.dto.Checklist_itemsDTO;

import java.util.List;
import java.util.UUID;

public interface Checklist_itemsService {

    List<Checklist_itemsDTO> obtenerTodos();

    Checklist_itemsDTO obtenerPorId(UUID id);

    Checklist_itemsDTO crear(Checklist_itemsDTO checklistItemsDTO);

    Checklist_itemsDTO actualizar(UUID id, Checklist_itemsDTO checklistItemsDTO);

    void eliminar(UUID id);
}