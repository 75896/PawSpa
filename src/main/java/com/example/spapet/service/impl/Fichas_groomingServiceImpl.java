// Rewritten to resolve Maven bad source file issues
package com.example.spapet.service.impl;

import com.example.spapet.dto.Fichas_groomingDTO;

import com.example.spapet.model.Citas;
import com.example.spapet.model.Fichas_grooming;
import com.example.spapet.model.Groomers;
import com.example.spapet.model.Mascotas;
import com.example.spapet.model.Usuarios;
import com.example.spapet.repository.CitasRepository;
import com.example.spapet.repository.Fichas_groomingRepository;
import com.example.spapet.repository.GroomersRepository;
import com.example.spapet.repository.UsuariosRepository;
import com.example.spapet.service.Fichas_groomingService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class Fichas_groomingServiceImpl implements Fichas_groomingService {

        private final Fichas_groomingRepository fichasRepository;
        private final CitasRepository citasRepository;
        private final GroomersRepository groomersRepository;
        private final UsuariosRepository usuariosRepository;

        @Override
        public List<Fichas_groomingDTO> listarPorCorreo(String correo) {
                Usuarios usuario = usuariosRepository.findByCorreo(correo)
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                Groomers groomer = groomersRepository.findByUsuariosId(usuario.getId())
                                .orElseThrow(() -> new RuntimeException("Groomer no encontrado"));
                return fichasRepository.findByGroomersId(groomer.getId())
                                .stream().map(this::toDTO).collect(Collectors.toList());
        }

        @Override
        @Transactional
        public Fichas_groomingDTO abrirFicha(UUID citaId, String correo) {
                Citas cita = citasRepository.findById(citaId)
                                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

                // Verificar si ya existe ficha
                fichasRepository.findByCitasId(citaId).ifPresent(f -> {
                        throw new RuntimeException("Ya existe una ficha para esta cita");
                });

                Usuarios usuario = usuariosRepository.findByCorreo(correo)
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                Groomers groomer = groomersRepository.findByUsuariosId(usuario.getId())
                                .orElseThrow(() -> new RuntimeException("Groomer no encontrado"));

                Fichas_grooming ficha = Fichas_grooming.builder()
                                .citas(cita)
                                .groomers(groomer)
                                .estado("abierta")
                                .tieneNudos(false)
                                .tienePulgas(false)
                                .tieneHeridas(false)
                                .build();

                // Cambiar estado de la cita a en_proceso
                cita.setEstado("en_proceso");
                citasRepository.save(cita);

                return toDTO(fichasRepository.save(ficha));
        }

        @Override
        @Transactional
        public Fichas_groomingDTO actualizar(UUID fichaId, Fichas_groomingDTO dto) {
                Fichas_grooming ficha = fichasRepository.findById(fichaId)
                                .orElseThrow(() -> new RuntimeException("Ficha no encontrada"));

                ficha.setTieneNudos(dto.getTieneNudos());
                ficha.setTienePulgas(dto.getTienePulgas());
                ficha.setTieneHeridas(dto.getTieneHeridas());
                ficha.setNivelNudos(dto.getNivelNudos());
                ficha.setObservacionesIngreso(dto.getObservacionesIngreso());
                ficha.setObservacionesSalida(dto.getObservacionesSalida());
                ficha.setRecomendaciones(dto.getRecomendaciones());
                ficha.setPesoKgActual(dto.getPesoKgActual());
                ficha.setEstado(dto.getEstado());

                return toDTO(fichasRepository.save(ficha));
        }

        @Override
        @Transactional
        public Fichas_groomingDTO cerrar(UUID fichaId) {
                Fichas_grooming ficha = fichasRepository.findById(fichaId)
                                .orElseThrow(() -> new RuntimeException("Ficha no encontrada"));

                ficha.setEstado("cerrada");
                ficha.setCerradaEn(OffsetDateTime.now());

                // Cambiar estado de la cita a completada
                Citas cita = ficha.getCitas();
                cita.setEstado("completada");
                citasRepository.save(cita);

                return toDTO(fichasRepository.save(ficha));
        }

        @Override
        public Optional<Fichas_groomingDTO> buscarPorCitaId(UUID citaId) {
                return fichasRepository.findByCitasId(citaId)
                                .map(this::toDTO);
        }

        private Fichas_groomingDTO toDTO(Fichas_grooming f) {
                Mascotas mascota = f.getCitas().getMascotas();
                return Fichas_groomingDTO.builder()
                                .id(f.getId())
                                .citaId(f.getCitas().getId())
                                .mascotaNombre(mascota.getNombre())
                                .mascotaEspecie(mascota.getEspecie())
                                .mascotaTemperamento(mascota.getTemperamento())
                                .mascotaAlergias(mascota.getAlergias())
                                .mascotaRestricciones(mascota.getRestricciones())
                                .groomerId(f.getGroomers().getId())
                                .groomerNombre(f.getGroomers().getUsuarios().getNombre())
                                .groomerApellido(f.getGroomers().getUsuarios().getApellido())
                                .estado(f.getEstado())
                                .tieneNudos(f.getTieneNudos())
                                .tienePulgas(f.getTienePulgas())
                                .tieneHeridas(f.getTieneHeridas())
                                .nivelNudos(f.getNivelNudos())
                                .observacionesIngreso(f.getObservacionesIngreso())
                                .observacionesSalida(f.getObservacionesSalida())
                                .recomendaciones(f.getRecomendaciones())
                                .pesoKgActual(f.getPesoKgActual())
                                .abiertaEn(f.getAbiertaEn())
                                .cerradaEn(f.getCerradaEn())
                                .creadoEn(f.getCreadoEn())
                                .build();
        }
}