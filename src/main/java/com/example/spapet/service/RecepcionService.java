package com.example.spapet.service;

import com.example.spapet.dto.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface RecepcionService {
    List<CitasDTO> listarTodasCitas();

    List<CitasDTO> listarCitasHoy();

    List<CitasDTO> listarCitasPorFecha(LocalDate fecha);

    CitasDTO agendarCita(CitasDTO dto);

    CitasDTO cambiarEstado(UUID id, String estado);

    CitasDTO cancelarCita(UUID id, String motivo);

    CitasDTO asignarGroomer(UUID citaId, UUID groomerId);

    List<ServiciosDTO> listarServicios();

    List<GroomersDTO> listarGroomers();

    List<Bloqueos_agendaDTO> listarBloqueos();

    Bloqueos_agendaDTO crearBloqueo(Bloqueos_agendaDTO dto);

    void eliminarBloqueo(UUID id);

    List<UsuariosDTO> listarClientes();

    List<MascotasDTO> listarMascotasPorUsuarioId(UUID usuarioId);
}