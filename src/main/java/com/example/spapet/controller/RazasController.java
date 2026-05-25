package com.example.spapet.controller;

import com.example.spapet.dto.RazasDTO;
import com.example.spapet.repository.RazasRepository;
import com.example.spapet.service.RazasService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/razas")
@RequiredArgsConstructor
public class RazasController {

    private final RazasService razasService;
    private final RazasRepository razasRepository;

    // @GetMapping
    // public ResponseEntity<List<RazasDTO>> obtenerTodos() {
    // return ResponseEntity.ok(
    // razasService.obtenerTodos());
    // }

    @GetMapping("/{id}")
    public ResponseEntity<RazasDTO> obtenerPorId(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                razasService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<RazasDTO> crear(
            @RequestBody RazasDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(razasService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RazasDTO> actualizar(
            @PathVariable UUID id,
            @RequestBody RazasDTO dto) {

        return ResponseEntity.ok(
                razasService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable UUID id) {

        razasService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<RazasDTO>> listar() {
        return ResponseEntity.ok(
                razasRepository.findAll().stream()
                        .map(r -> RazasDTO.builder()
                                .id(r.getId())
                                .nombre(r.getNombre())
                                .especie(r.getEspecie())
                                .tamanio(r.getTamanio())
                                .ajusteDuracion(r.getAjusteDuracion())
                                .build())
                        .collect(Collectors.toList()));
    }

    @GetMapping("/especie/{especie}")
    public ResponseEntity<List<RazasDTO>> porEspecie(@PathVariable String especie) {
        return ResponseEntity.ok(
                razasRepository.findByEspecie(especie).stream()
                        .map(r -> RazasDTO.builder()
                                .id(r.getId())
                                .nombre(r.getNombre())
                                .especie(r.getEspecie())
                                .tamanio(r.getTamanio())
                                .ajusteDuracion(r.getAjusteDuracion())
                                .build())
                        .collect(Collectors.toList()));
    }

}