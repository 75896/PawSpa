package com.example.spapet.service.impl;

import com.example.spapet.dto.GroomersDTO;

import com.example.spapet.model.Groomers;
import com.example.spapet.model.Usuarios;

import com.example.spapet.repository.GroomersRepository;
import com.example.spapet.repository.UsuariosRepository;

import com.example.spapet.service.GroomersService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroomersServiceImpl implements GroomersService {

    private final GroomersRepository groomersRepository;
    private final UsuariosRepository usuariosRepository;

    @Override
    public List<GroomersDTO> obtenerTodos() {

        return groomersRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public GroomersDTO obtenerPorId(UUID id) {

        Groomers groomer = groomersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Groomer no encontrado"));

        return convertToDTO(groomer);
    }

    @Override
    public GroomersDTO crear(GroomersDTO dto) {

        Usuarios usuario = usuariosRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Groomers groomer = Groomers.builder()
                .usuarios(usuario)
                .especialidades(dto.getEspecialidades())
                .capacidadMax(dto.getCapacidadMax())
                .activo(dto.getActivo())
                .build();

        Groomers groomerGuardado = groomersRepository.save(groomer);

        return convertToDTO(groomerGuardado);
    }

    @Override
    public GroomersDTO actualizar(UUID id, GroomersDTO dto) {

        Groomers groomer = groomersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Groomer no encontrado"));

        Usuarios usuario = usuariosRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        groomer.setUsuarios(usuario);
        groomer.setEspecialidades(dto.getEspecialidades());
        groomer.setCapacidadMax(dto.getCapacidadMax());
        groomer.setActivo(dto.getActivo());

        Groomers groomerActualizado = groomersRepository.save(groomer);

        return convertToDTO(groomerActualizado);
    }

    @Override
    public void eliminar(UUID id) {

        Groomers groomer = groomersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Groomer no encontrado"));

        groomersRepository.delete(groomer);
    }

    // =========================
    // CONVERTERS
    // =========================

    private GroomersDTO convertToDTO(Groomers groomer) {

        return GroomersDTO.builder()
                .id(groomer.getId())

                .usuarioId(
                        groomer.getUsuarios() != null
                                ? groomer.getUsuarios().getId()
                                : null)

                .usuarioNombre(
                        groomer.getUsuarios() != null
                                ? groomer.getUsuarios().getNombre()
                                : null)

                .usuarioApellido(
                        groomer.getUsuarios() != null
                                ? groomer.getUsuarios().getApellido()
                                : null)

                .usuarioCorreo(
                        groomer.getUsuarios() != null
                                ? groomer.getUsuarios().getCorreo()
                                : null)

                .especialidades(groomer.getEspecialidades())
                .capacidadMax(groomer.getCapacidadMax())
                .activo(groomer.getActivo())
                .creadoEn(groomer.getCreadoEn())
                .actualizadoEn(groomer.getActualizadoEn())
                .build();
    }
}