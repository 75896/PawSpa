package com.example.spapet.controller;

import com.example.spapet.dto.Checklist_itemsDTO;
import com.example.spapet.service.Checklist_itemsService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/checklist-items")
@RequiredArgsConstructor
public class Checklist_itemsController {

    private final Checklist_itemsService checklistItemsService;

    @GetMapping
    public ResponseEntity<List<Checklist_itemsDTO>> obtenerTodos() {

        return ResponseEntity.ok(
                checklistItemsService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Checklist_itemsDTO> obtenerPorId(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                checklistItemsService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<Checklist_itemsDTO> crear(
            @RequestBody Checklist_itemsDTO checklistItemsDTO) {

        Checklist_itemsDTO nuevoItem = checklistItemsService.crear(checklistItemsDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(nuevoItem);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Checklist_itemsDTO> actualizar(
            @PathVariable UUID id,
            @RequestBody Checklist_itemsDTO checklistItemsDTO) {

        return ResponseEntity.ok(
                checklistItemsService.actualizar(id, checklistItemsDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable UUID id) {

        checklistItemsService.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}