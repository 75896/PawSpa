package com.example.spapet.service;

import com.example.spapet.dto.CierreCajaDTO;
import com.example.spapet.dto.CobrarDTO;
import com.example.spapet.dto.PagosDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface PagosService {

    List<PagosDTO> obtenerTodos();

    PagosDTO obtenerPorId(UUID id);

    PagosDTO crear(PagosDTO dto);

    PagosDTO actualizar(UUID id, PagosDTO dto);

    void eliminar(UUID id);

    PagosDTO cobrar(CobrarDTO dto, String correoRecepcion);

    CierreCajaDTO cierreCaja(LocalDate fecha);
}