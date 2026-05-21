package com.example.spapet.service.impl;

import com.example.spapet.dto.ConfiguracionDTO;
import com.example.spapet.model.Configuracion;
import com.example.spapet.model.Usuarios;
import com.example.spapet.repository.ConfiguracionRepository;
import com.example.spapet.repository.UsuariosRepository;
import com.example.spapet.service.ConfiguracionService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConfiguracionServiceImpl implements ConfiguracionService {

    private final ConfiguracionRepository configuracionRepository;
    private final UsuariosRepository usuariosRepository;

    @Override
    public List<ConfiguracionDTO> obtenerTodos() {

        return configuracionRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ConfiguracionDTO obtenerPorClave(String clave) {

        Configuracion configuracion = configuracionRepository.findById(clave)
                .orElseThrow(() -> new RuntimeException("Configuración no encontrada"));

        return convertToDTO(configuracion);
    }

    @Override
    public ConfiguracionDTO crear(ConfiguracionDTO dto) {

        Usuarios usuario = null;

        if (dto.getActualizadoPorId() != null) {

            usuario = usuariosRepository.findById(dto.getActualizadoPorId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        }

        Configuracion configuracion = Configuracion.builder()
                .clave(dto.getClave())
                .valor(dto.getValor())
                .descripcion(dto.getDescripcion())
                .actualizadoPor(usuario)
                .build();

        Configuracion configuracionGuardada = configuracionRepository.save(configuracion);

        return convertToDTO(configuracionGuardada);
    }

    @Override
    public ConfiguracionDTO actualizar(String clave, ConfiguracionDTO dto) {

        Configuracion configuracion = configuracionRepository.findById(clave)
                .orElseThrow(() -> new RuntimeException("Configuración no encontrada"));

        Usuarios usuario = null;

        if (dto.getActualizadoPorId() != null) {

            usuario = usuariosRepository.findById(dto.getActualizadoPorId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        }

        configuracion.setValor(dto.getValor());
        configuracion.setDescripcion(dto.getDescripcion());
        configuracion.setActualizadoPor(usuario);

        Configuracion configuracionActualizada = configuracionRepository.save(configuracion);

        return convertToDTO(configuracionActualizada);
    }

    @Override
    public void eliminar(String clave) {

        Configuracion configuracion = configuracionRepository.findById(clave)
                .orElseThrow(() -> new RuntimeException("Configuración no encontrada"));

        configuracionRepository.delete(configuracion);
    }

    // =========================
    // CONVERTERS
    // =========================

    private ConfiguracionDTO convertToDTO(Configuracion configuracion) {

        return ConfiguracionDTO.builder()
                .clave(configuracion.getClave())
                .valor(configuracion.getValor())
                .descripcion(configuracion.getDescripcion())
                .actualizadoEn(configuracion.getActualizadoEn())

                .actualizadoPorId(
                        configuracion.getActualizadoPor() != null
                                ? configuracion.getActualizadoPor().getId()
                                : null)

                .actualizadoPorNombre(
                        configuracion.getActualizadoPor() != null
                                ? configuracion.getActualizadoPor().getNombre()
                                : null)

                .build();
    }
}