package com.example.spapet.service.impl;

import com.example.spapet.dto.Checklist_itemsDTO;
import com.example.spapet.model.Checklist_items;
import com.example.spapet.repository.Checklist_itemsRepository;
import com.example.spapet.service.Checklist_itemsService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class Checklist_itemsServiceImpl implements Checklist_itemsService {

    private final Checklist_itemsRepository checklistItemsRepository;

    @Override
    public List<Checklist_itemsDTO> obtenerTodos() {

        return checklistItemsRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Checklist_itemsDTO obtenerPorId(UUID id) {

        Checklist_items item = checklistItemsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item de checklist no encontrado"));

        return convertToDTO(item);
    }

    @Override
    public Checklist_itemsDTO crear(Checklist_itemsDTO dto) {

        Checklist_items item = Checklist_items.builder()
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .obligatorio(dto.getObligatorio())
                .orden(dto.getOrden())
                .build();

        Checklist_items itemGuardado = checklistItemsRepository.save(item);

        return convertToDTO(itemGuardado);
    }

    @Override
    public Checklist_itemsDTO actualizar(UUID id, Checklist_itemsDTO dto) {

        Checklist_items item = checklistItemsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item de checklist no encontrado"));

        item.setNombre(dto.getNombre());
        item.setDescripcion(dto.getDescripcion());
        item.setObligatorio(dto.getObligatorio());
        item.setOrden(dto.getOrden());

        Checklist_items itemActualizado = checklistItemsRepository.save(item);

        return convertToDTO(itemActualizado);
    }

    @Override
    public void eliminar(UUID id) {

        Checklist_items item = checklistItemsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item de checklist no encontrado"));

        checklistItemsRepository.delete(item);
    }

    // =========================
    // CONVERTERS
    // =========================

    private Checklist_itemsDTO convertToDTO(Checklist_items item) {

        return Checklist_itemsDTO.builder()
                .id(item.getId())
                .nombre(item.getNombre())
                .descripcion(item.getDescripcion())
                .obligatorio(item.getObligatorio())
                .orden(item.getOrden())
                .build();
    }
}