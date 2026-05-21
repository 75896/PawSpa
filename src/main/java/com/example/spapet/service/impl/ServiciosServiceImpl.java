package com.example.spapet.service.impl;

import com.example.spapet.dto.ServiciosDTO;

import com.example.spapet.model.Servicios;

import com.example.spapet.repository.ServiciosRepository;

import com.example.spapet.service.ServiciosService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiciosServiceImpl implements ServiciosService {

    private final ServiciosRepository serviciosRepository;

    @Override
    public List<ServiciosDTO> obtenerTodos() {

        return serviciosRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ServiciosDTO obtenerPorId(UUID id) {

        Servicios servicio = serviciosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

        return convertToDTO(servicio);
    }

    @Override
    public ServiciosDTO crear(ServiciosDTO dto) {

        Servicios servicio = Servicios.builder()
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .duracionMin(dto.getDuracionMin())
                .precioBase(dto.getPrecioBase())
                .permiteDobleBooking(dto.getPermiteDobleBooking())
                .activo(dto.getActivo())
                .build();

        Servicios servicioGuardado = serviciosRepository.save(servicio);

        return convertToDTO(servicioGuardado);
    }

    @Override
    public ServiciosDTO actualizar(UUID id, ServiciosDTO dto) {

        Servicios servicio = serviciosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

        servicio.setNombre(dto.getNombre());
        servicio.setDescripcion(dto.getDescripcion());
        servicio.setDuracionMin(dto.getDuracionMin());
        servicio.setPrecioBase(dto.getPrecioBase());
        servicio.setPermiteDobleBooking(dto.getPermiteDobleBooking());
        servicio.setActivo(dto.getActivo());

        Servicios servicioActualizado = serviciosRepository.save(servicio);

        return convertToDTO(servicioActualizado);
    }

    @Override
    public void eliminar(UUID id) {

        Servicios servicio = serviciosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

        serviciosRepository.delete(servicio);
    }

    // =========================
    // CONVERTERS
    // =========================

    private ServiciosDTO convertToDTO(Servicios servicio) {

        return ServiciosDTO.builder()
                .id(servicio.getId())
                .nombre(servicio.getNombre())
                .descripcion(servicio.getDescripcion())
                .duracionMin(servicio.getDuracionMin())
                .precioBase(servicio.getPrecioBase())
                .permiteDobleBooking(servicio.getPermiteDobleBooking())
                .activo(servicio.getActivo())
                .creadoEn(servicio.getCreadoEn())
                .actualizadoEn(servicio.getActualizadoEn())
                .build();
    }
}