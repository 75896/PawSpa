package com.example.spapet.service.impl;

import com.example.spapet.dto.Fichas_checklistDTO;

import com.example.spapet.model.Checklist_items;
import com.example.spapet.model.Fichas_checklist;
import com.example.spapet.model.Fichas_grooming;

import com.example.spapet.repository.Checklist_itemsRepository;
import com.example.spapet.repository.Fichas_checklistRepository;
import com.example.spapet.repository.Fichas_groomingRepository;

import com.example.spapet.service.Fichas_checklistService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class Fichas_checklistServiceImpl implements Fichas_checklistService {

    private final Fichas_checklistRepository fichasChecklistRepository;
    private final Fichas_groomingRepository fichasGroomingRepository;
    private final Checklist_itemsRepository checklistItemsRepository;

    @Override
    public List<Fichas_checklistDTO> obtenerTodos() {

        return fichasChecklistRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Fichas_checklistDTO obtenerPorId(UUID id) {

        Fichas_checklist fichaChecklist = fichasChecklistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ficha checklist no encontrada"));

        return convertToDTO(fichaChecklist);
    }

    @Override
    public Fichas_checklistDTO crear(Fichas_checklistDTO dto) {

        Fichas_grooming ficha = fichasGroomingRepository.findById(dto.getFichaId())
                .orElseThrow(() -> new RuntimeException("Ficha grooming no encontrada"));

        Checklist_items item = checklistItemsRepository.findById(dto.getItemId())
                .orElseThrow(() -> new RuntimeException("Item checklist no encontrado"));

        Fichas_checklist fichaChecklist = Fichas_checklist.builder()
                .ficha(ficha)
                .item(item)
                .realizado(dto.getRealizado())
                .observacion(dto.getObservacion())
                .build();

        Fichas_checklist fichaChecklistGuardada = fichasChecklistRepository.save(fichaChecklist);

        return convertToDTO(fichaChecklistGuardada);
    }

    @Override
    public Fichas_checklistDTO actualizar(UUID id, Fichas_checklistDTO dto) {

        Fichas_checklist fichaChecklist = fichasChecklistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ficha checklist no encontrada"));

        Fichas_grooming ficha = fichasGroomingRepository.findById(dto.getFichaId())
                .orElseThrow(() -> new RuntimeException("Ficha grooming no encontrada"));

        Checklist_items item = checklistItemsRepository.findById(dto.getItemId())
                .orElseThrow(() -> new RuntimeException("Item checklist no encontrado"));

        fichaChecklist.setFicha(ficha);
        fichaChecklist.setItem(item);
        fichaChecklist.setRealizado(dto.getRealizado());
        fichaChecklist.setObservacion(dto.getObservacion());

        Fichas_checklist fichaChecklistActualizada = fichasChecklistRepository.save(fichaChecklist);

        return convertToDTO(fichaChecklistActualizada);
    }

    @Override
    public void eliminar(UUID id) {

        Fichas_checklist fichaChecklist = fichasChecklistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ficha checklist no encontrada"));

        fichasChecklistRepository.delete(fichaChecklist);
    }

    // =========================
    // CONVERTERS
    // =========================

    private Fichas_checklistDTO convertToDTO(Fichas_checklist fichaChecklist) {

        return Fichas_checklistDTO.builder()
                .id(fichaChecklist.getId())

                .fichaId(
                        fichaChecklist.getFicha() != null
                                ? fichaChecklist.getFicha().getId()
                                : null)

                .itemId(
                        fichaChecklist.getItem() != null
                                ? fichaChecklist.getItem().getId()
                                : null)

                .itemNombre(
                        fichaChecklist.getItem() != null
                                ? fichaChecklist.getItem().getNombre()
                                : null)

                .realizado(fichaChecklist.getRealizado())
                .observacion(fichaChecklist.getObservacion())
                .registradoEn(fichaChecklist.getRegistradoEn())
                .build();
    }
}