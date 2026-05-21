package com.example.spapet.service.impl;

import com.example.spapet.dto.CitasDTO;
import com.example.spapet.model.Citas;
import com.example.spapet.model.Clientes;
import com.example.spapet.model.Usuarios;
import com.example.spapet.repository.CitasRepository;
import com.example.spapet.repository.ClientesRepository;
import com.example.spapet.repository.UsuariosRepository;
import com.example.spapet.service.CitasService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CitasServiceImpl implements CitasService {

        private final CitasRepository citaRepository;
        private final ClientesRepository clienteRepository;
        private final UsuariosRepository usuarioRepository;

        @Override
        public List<CitasDTO> obtenerTodos() {
                return citaRepository.findAll()
                                .stream().map(this::toDTO).collect(Collectors.toList());
        }

        @Override
        public CitasDTO obtenerPorId(UUID id) {
                Citas c = citaRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));
                return toDTO(c);
        }

        @Override
        public CitasDTO crear(CitasDTO dto) {
                throw new UnsupportedOperationException("Usar endpoint de agenda");
        }

        @Override
        public CitasDTO actualizar(UUID id, CitasDTO dto) {
                throw new UnsupportedOperationException("Usar endpoint de agenda");
        }

        @Override
        public void eliminar(UUID id) {
                citaRepository.deleteById(id);
        }

        @Override
        public List<CitasDTO> listarPorCorreo(String correo) {
                Usuarios usuario = usuarioRepository.findByCorreo(correo)
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                Clientes cliente = clienteRepository.findByUsuariosId(usuario.getId())
                                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
                return citaRepository.findByMascotaClienteId(cliente.getId())
                                .stream().map(this::toDTO).collect(Collectors.toList());
        }

        private CitasDTO toDTO(Citas c) {
                return CitasDTO.builder()
                                .id(c.getId())
                                .mascotaId(c.getMascotas().getId())
                                .mascotaNombre(c.getMascotas().getNombre())
                                .mascotaEspecie(c.getMascotas().getEspecie())
                                .mascotaTemperamento(c.getMascotas().getTemperamento())
                                .groomerId(c.getGroomers().getId())
                                .groomerNombre(c.getGroomers().getUsuarios().getNombre())
                                .groomerApellido(c.getGroomers().getUsuarios().getApellido())
                                .servicioId(c.getServicios().getId())
                                .servicioNombre(c.getServicios().getNombre())
                                .fechaInicio(c.getFechaInicio())
                                .fechaFin(c.getFechaFin())
                                .estado(c.getEstado())
                                .precioAcordado(c.getPrecioAcordado())
                                .creadoEn(c.getCreadoEn())
                                .build();
        }
}