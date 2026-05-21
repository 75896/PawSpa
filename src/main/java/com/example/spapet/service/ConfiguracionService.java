package com.example.spapet.service;

import com.example.spapet.dto.ConfiguracionDTO;

import java.util.List;

public interface ConfiguracionService {

    List<ConfiguracionDTO> obtenerTodos();

    ConfiguracionDTO obtenerPorClave(String clave);

    ConfiguracionDTO crear(ConfiguracionDTO configuracionDTO);

    ConfiguracionDTO actualizar(String clave, ConfiguracionDTO configuracionDTO);

    void eliminar(String clave);
}