// Rewritten to resolve Maven bad source file issues
package com.example.spapet.service.impl;

import com.example.spapet.dto.Fichas_groomingDTO;

import com.example.spapet.model.Citas;
import com.example.spapet.model.Fichas_grooming;
import com.example.spapet.model.Groomers;

import com.example.spapet.repository.CitasRepository;
import com.example.spapet.repository.Fichas_groomingRepository;
import com.example.spapet.repository.GroomersRepository;

import com.example.spapet.service.Fichas_groomingService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class Fichas_groomingServiceImpl implements Fichas_groomingService {

    private final Fichas_groomingRepository fichasGroomingRepository;
    private final CitasRepository citasRepository;
    private final GroomersRepository groomersRepository;

    @Override
    public List<Fichas_groomingDTO> obtenerTodos() {

        return fichasGroomingRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Fichas_groomingDTO obtenerPorId(UUID id) {

        Fichas_grooming ficha = fichasGroomingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ficha grooming no encontrada"));

        return convertToDTO(ficha);
    }

    @Override
    public Fichas_groomingDTO crear(Fichas_groomingDTO dto) {

        Citas cita = citasRepository.findById(dto.getCitaId())
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        Groomers groomer = groomersRepository.findById(dto.getGroomerId())
                .orElseThrow(() -> new RuntimeException("Groomer no encontrado"));

        Fichas_grooming ficha = Fichas_grooming.builder()
                .citas(cita)
                .groomers(groomer)
                .estado(dto.getEstado())
                .tieneNudos(dto.getTieneNudos())
                .tienePulgas(dto.getTienePulgas())
                .tieneHeridas(dto.getTieneHeridas())
                .nivelNudos(dto.getNivelNudos())
                .observacionesIngreso(dto.getObservacionesIngreso())
                .observacionesSalida(dto.getObservacionesSalida())
                .recomendaciones(dto.getRecomendaciones())
                .pesoKgActual(dto.getPesoKgActual())
                .cerradaEn(dto.getCerradaEn())
                .build();

        Fichas_grooming fichaGuardada = fichasGroomingRepository.save(ficha);

        return convertToDTO(fichaGuardada);
    }

    @Override
    public Fichas_groomingDTO actualizar(UUID id, Fichas_groomingDTO dto) {

        Fichas_grooming ficha = fichasGroomingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ficha grooming no encontrada"));

        Citas cita = citasRepository.findById(dto.getCitaId())
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        Groomers groomer = groomersRepository.findById(dto.getGroomerId())
                .orElseThrow(() -> new RuntimeException("Groomer no encontrado"));

        ficha.setCitas(cita);
        ficha.setGroomers(groomer);
        ficha.setEstado(dto.getEstado());
        ficha.setTieneNudos(dto.getTieneNudos());
        ficha.setTienePulgas(dto.getTienePulgas());
        ficha.setTieneHeridas(dto.getTieneHeridas());
        ficha.setNivelNudos(dto.getNivelNudos());
        ficha.setObservacionesIngreso(dto.getObservacionesIngreso());
        ficha.setObservacionesSalida(dto.getObservacionesSalida());
        ficha.setRecomendaciones(dto.getRecomendaciones());
        ficha.setPesoKgActual(dto.getPesoKgActual());
        ficha.setCerradaEn(dto.getCerradaEn());

        Fichas_grooming fichaActualizada = fichasGroomingRepository.save(ficha);

        return convertToDTO(fichaActualizada);
    }

    @Override
    public void eliminar(UUID id) {

        Fichas_grooming ficha = fichasGroomingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ficha grooming no encontrada"));

        fichasGroomingRepository.delete(ficha);
    }

    // =========================
    // CONVERTERS
    // =========================

    private Fichas_groomingDTO convertToDTO(Fichas_grooming ficha) {

        return Fichas_groomingDTO.builder()
                .id(ficha.getId())

                .citaId(
                        ficha.getCitas() != null
                                ? ficha.getCitas().getId()
                                : null)

                .groomerId(
                        ficha.getGroomers() != null
                                ? ficha.getGroomers().getId()
                                : null)

                .groomerNombre(
                        ficha.getGroomers() != null
                                ? ficha.getGroomers().getUsuarios().getNombre()
                                : null)

                .estado(ficha.getEstado())
                .tieneNudos(ficha.getTieneNudos())
                .tienePulgas(ficha.getTienePulgas())
                .tieneHeridas(ficha.getTieneHeridas())
                .nivelNudos(ficha.getNivelNudos())
                .observacionesIngreso(ficha.getObservacionesIngreso())
                .observacionesSalida(ficha.getObservacionesSalida())
                .recomendaciones(ficha.getRecomendaciones())
                .pesoKgActual(ficha.getPesoKgActual())
                .abiertaEn(ficha.getAbiertaEn())
                .cerradaEn(ficha.getCerradaEn())
                .creadoEn(ficha.getCreadoEn())
                .actualizadoEn(ficha.getActualizadoEn())
                .build();
    }
}