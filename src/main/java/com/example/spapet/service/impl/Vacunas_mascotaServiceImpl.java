package com.example.spapet.service.impl;

import com.example.spapet.dto.Vacunas_mascotaDTO;

import com.example.spapet.model.Mascotas;
import com.example.spapet.model.Vacunas_mascota;

import com.example.spapet.repository.MascotasRepository;
import com.example.spapet.repository.Vacunas_mascotaRepository;

import com.example.spapet.service.Vacunas_mascotaService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class Vacunas_mascotaServiceImpl implements Vacunas_mascotaService {

    private final Vacunas_mascotaRepository vacunasMascotaRepository;
    private final MascotasRepository mascotasRepository;

    @Override
    public List<Vacunas_mascotaDTO> obtenerTodos() {

        return vacunasMascotaRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Vacunas_mascotaDTO obtenerPorId(UUID id) {

        Vacunas_mascota vacuna = vacunasMascotaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vacuna no encontrada"));

        return convertToDTO(vacuna);
    }

    @Override
    public Vacunas_mascotaDTO crear(Vacunas_mascotaDTO dto) {

        Mascotas mascota = mascotasRepository.findById(dto.getMascotaId())
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));

        Vacunas_mascota vacuna = Vacunas_mascota.builder()
                .mascotas(mascota)
                .nombreVacuna(dto.getNombreVacuna())
                .fechaAplicacion(dto.getFechaAplicacion())
                .fechaProxima(dto.getFechaProxima())
                .veterinario(dto.getVeterinario())
                .observaciones(dto.getObservaciones())
                .build();

        Vacunas_mascota vacunaGuardada = vacunasMascotaRepository.save(vacuna);

        return convertToDTO(vacunaGuardada);
    }

    @Override
    public Vacunas_mascotaDTO actualizar(UUID id, Vacunas_mascotaDTO dto) {

        Vacunas_mascota vacuna = vacunasMascotaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vacuna no encontrada"));

        Mascotas mascota = mascotasRepository.findById(dto.getMascotaId())
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));

        vacuna.setMascotas(mascota);
        vacuna.setNombreVacuna(dto.getNombreVacuna());
        vacuna.setFechaAplicacion(dto.getFechaAplicacion());
        vacuna.setFechaProxima(dto.getFechaProxima());
        vacuna.setVeterinario(dto.getVeterinario());
        vacuna.setObservaciones(dto.getObservaciones());

        Vacunas_mascota vacunaActualizada = vacunasMascotaRepository.save(vacuna);

        return convertToDTO(vacunaActualizada);
    }

    @Override
    public void eliminar(UUID id) {

        Vacunas_mascota vacuna = vacunasMascotaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vacuna no encontrada"));

        vacunasMascotaRepository.delete(vacuna);
    }

    // =========================
    // CONVERTERS
    // =========================

    private Vacunas_mascotaDTO convertToDTO(Vacunas_mascota vacuna) {

        return Vacunas_mascotaDTO.builder()
                .id(vacuna.getId())

                .mascotaId(vacuna.getMascotas().getId())
                .mascotaNombre(vacuna.getMascotas().getNombre())

                .nombreVacuna(vacuna.getNombreVacuna())
                .fechaAplicacion(vacuna.getFechaAplicacion())
                .fechaProxima(vacuna.getFechaProxima())
                .veterinario(vacuna.getVeterinario())
                .observaciones(vacuna.getObservaciones())

                .creadoEn(vacuna.getCreadoEn())
                .build();
    }
}