package com.example.spapet.service;

import com.example.spapet.dto.MascotasDTO;

import java.util.List;
import java.util.UUID;

public interface MascotasService {

    List<MascotasDTO> obtenerTodos();

    MascotasDTO obtenerPorId(UUID id);

    MascotasDTO crear(MascotasDTO mascotasDTO);

    MascotasDTO actualizar(UUID id, MascotasDTO mascotasDTO);

    void eliminar(UUID id);

    List<MascotasDTO> listarPorCorreo(String correo);

    MascotasDTO crearParaCliente(String correo, MascotasDTO dto);

    List<MascotasDTO> listarPorClienteId(UUID clienteId);
}