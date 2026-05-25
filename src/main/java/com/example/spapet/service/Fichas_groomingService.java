package com.example.spapet.service;

import com.example.spapet.dto.Fichas_groomingDTO;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

public interface Fichas_groomingService {

    Optional<Fichas_groomingDTO> buscarPorCitaId(UUID citaId);

    List<Fichas_groomingDTO> listarPorCorreo(String correo);

    Fichas_groomingDTO abrirFicha(UUID citaId, String correo);

    Fichas_groomingDTO actualizar(UUID fichaId, Fichas_groomingDTO dto);

    Fichas_groomingDTO cerrar(UUID fichaId);

}