package com.example.spapet.service.impl;

import com.example.spapet.dto.Fotos_mascotaDTO;

import com.example.spapet.model.Fichas_grooming;
import com.example.spapet.model.Fotos_mascota;
import com.example.spapet.model.Mascotas;
import com.example.spapet.model.Usuarios;

import com.example.spapet.repository.Fichas_groomingRepository;
import com.example.spapet.repository.Fotos_mascotaRepository;
import com.example.spapet.repository.MascotasRepository;
import com.example.spapet.repository.UsuariosRepository;

import com.example.spapet.service.Fotos_mascotaService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class Fotos_mascotaServiceImpl implements Fotos_mascotaService {

    private final Fotos_mascotaRepository fotosMascotaRepository;
    private final MascotasRepository mascotasRepository;
    private final Fichas_groomingRepository fichasGroomingRepository;
    private final UsuariosRepository usuariosRepository;

    @Override
    public List<Fotos_mascotaDTO> obtenerTodos() {

        return fotosMascotaRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Fotos_mascotaDTO obtenerPorId(UUID id) {

        Fotos_mascota foto = fotosMascotaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Foto no encontrada"));

        return convertToDTO(foto);
    }

    @Override
    public Fotos_mascotaDTO crear(Fotos_mascotaDTO dto) {

        Mascotas mascota = mascotasRepository.findById(dto.getMascotaId())
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));

        Fichas_grooming ficha = null;

        if (dto.getFichaId() != null) {

            ficha = fichasGroomingRepository.findById(dto.getFichaId())
                    .orElseThrow(() -> new RuntimeException("Ficha grooming no encontrada"));
        }

        Usuarios usuario = null;

        if (dto.getSubidaPorId() != null) {

            usuario = usuariosRepository.findById(dto.getSubidaPorId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        }

        Fotos_mascota foto = Fotos_mascota.builder()
                .mascotas(mascota)
                .ficha(ficha)
                .url(dto.getUrl())
                .etiqueta(dto.getEtiqueta())
                .descripcion(dto.getDescripcion())
                .subidaPor(usuario)
                .build();

        Fotos_mascota fotoGuardada = fotosMascotaRepository.save(foto);

        return convertToDTO(fotoGuardada);
    }

    @Override
    public Fotos_mascotaDTO actualizar(UUID id, Fotos_mascotaDTO dto) {

        Fotos_mascota foto = fotosMascotaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Foto no encontrada"));

        Mascotas mascota = mascotasRepository.findById(dto.getMascotaId())
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));

        Fichas_grooming ficha = null;

        if (dto.getFichaId() != null) {

            ficha = fichasGroomingRepository.findById(dto.getFichaId())
                    .orElseThrow(() -> new RuntimeException("Ficha grooming no encontrada"));
        }

        Usuarios usuario = null;

        if (dto.getSubidaPorId() != null) {

            usuario = usuariosRepository.findById(dto.getSubidaPorId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        }

        foto.setMascotas(mascota);
        foto.setFicha(ficha);
        foto.setUrl(dto.getUrl());
        foto.setEtiqueta(dto.getEtiqueta());
        foto.setDescripcion(dto.getDescripcion());
        foto.setSubidaPor(usuario);

        Fotos_mascota fotoActualizada = fotosMascotaRepository.save(foto);

        return convertToDTO(fotoActualizada);
    }

    @Override
    public void eliminar(UUID id) {

        Fotos_mascota foto = fotosMascotaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Foto no encontrada"));

        fotosMascotaRepository.delete(foto);
    }

    // =========================
    // CONVERTERS
    // =========================

    private Fotos_mascotaDTO convertToDTO(Fotos_mascota foto) {

        return Fotos_mascotaDTO.builder()
                .id(foto.getId())

                .mascotaId(
                        foto.getMascotas() != null
                                ? foto.getMascotas().getId()
                                : null)

                .mascotaNombre(
                        foto.getMascotas() != null
                                ? foto.getMascotas().getNombre()
                                : null)

                .fichaId(
                        foto.getFicha() != null
                                ? foto.getFicha().getId()
                                : null)

                .url(foto.getUrl())
                .etiqueta(foto.getEtiqueta())
                .descripcion(foto.getDescripcion())

                .subidaPorId(
                        foto.getSubidaPor() != null
                                ? foto.getSubidaPor().getId()
                                : null)

                .subidaPorNombre(
                        foto.getSubidaPor() != null
                                ? foto.getSubidaPor().getNombre()
                                : null)

                .creadoEn(foto.getCreadoEn())
                .build();
    }
}