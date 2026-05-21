package com.example.spapet.service;

import com.example.spapet.dto.NotificacionesDTO;

import java.util.List;
import java.util.UUID;

public interface NotificacionesService {

    List<NotificacionesDTO> obtenerTodos();

    NotificacionesDTO obtenerPorId(UUID id);

    NotificacionesDTO crear(NotificacionesDTO notificacionesDTO);

    NotificacionesDTO actualizar(UUID id, NotificacionesDTO notificacionesDTO);

    void eliminar(UUID id);
}