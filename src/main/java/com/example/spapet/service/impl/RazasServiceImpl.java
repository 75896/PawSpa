package com.example.spapet.service.impl;

import com.example.spapet.dto.RazasDTO;

import com.example.spapet.model.Razas;

import com.example.spapet.repository.RazasRepository;

import com.example.spapet.service.RazasService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RazasServiceImpl implements RazasService {

    private final RazasRepository razasRepository;

    @Override
    public List<RazasDTO> obtenerTodos() {

        return razasRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RazasDTO obtenerPorId(UUID id) {

        Razas raza = razasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Raza no encontrada"));

        return convertToDTO(raza);
    }

    @Override
    public RazasDTO crear(RazasDTO dto) {

        Razas raza = Razas.builder()
                .nombre(dto.getNombre())
                .especie(dto.getEspecie())
                .tamanio(dto.getTamanio())
                .ajusteDuracion(dto.getAjusteDuracion())
                .build();

        Razas razaGuardada = razasRepository.save(raza);

        return convertToDTO(razaGuardada);
    }

    @Override
    public RazasDTO actualizar(UUID id, RazasDTO dto) {

        Razas raza = razasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Raza no encontrada"));

        raza.setNombre(dto.getNombre());
        raza.setEspecie(dto.getEspecie());
        raza.setTamanio(dto.getTamanio());
        raza.setAjusteDuracion(dto.getAjusteDuracion());

        Razas razaActualizada = razasRepository.save(raza);

        return convertToDTO(razaActualizada);
    }

    @Override
    public void eliminar(UUID id) {

        Razas raza = razasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Raza no encontrada"));

        razasRepository.delete(raza);
    }

    // =========================
    // CONVERTERS
    // =========================

    private RazasDTO convertToDTO(Razas raza) {

        return RazasDTO.builder()
                .id(raza.getId())
                .nombre(raza.getNombre())
                .especie(raza.getEspecie())
                .tamanio(raza.getTamanio())
                .ajusteDuracion(raza.getAjusteDuracion())
                .build();
    }
}