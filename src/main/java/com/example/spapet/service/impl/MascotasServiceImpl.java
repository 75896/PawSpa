package com.example.spapet.service.impl;

import com.example.spapet.dto.MascotasDTO;
import com.example.spapet.model.Clientes;
import com.example.spapet.model.Mascotas;
import com.example.spapet.model.Usuarios;
import com.example.spapet.repository.ClientesRepository;
import com.example.spapet.repository.MascotasRepository;
import com.example.spapet.repository.UsuariosRepository;
import com.example.spapet.service.MascotasService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MascotasServiceImpl implements MascotasService {

        private final MascotasRepository mascotaRepository;
        private final ClientesRepository clienteRepository;
        private final UsuariosRepository usuarioRepository;

        @Override
        public List<MascotasDTO> obtenerTodos() {
                return mascotaRepository.findAll()
                                .stream().map(this::toDTO).collect(Collectors.toList());
        }

        @Override
        public MascotasDTO obtenerPorId(UUID id) {
                Mascotas m = mascotaRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));
                return toDTO(m);
        }

        @Override
        public MascotasDTO crear(MascotasDTO dto) {
                Clientes cliente = clienteRepository.findById(dto.getClienteId())
                                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
                Mascotas mascota = buildMascota(dto, cliente);
                return toDTO(mascotaRepository.save(mascota));
        }

        @Override
        public MascotasDTO actualizar(UUID id, MascotasDTO dto) {
                Mascotas mascota = mascotaRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));
                mascota.setNombre(dto.getNombre());
                mascota.setEspecie(dto.getEspecie());
                mascota.setSexo(dto.getSexo());
                mascota.setTemperamento(dto.getTemperamento());
                mascota.setAlergias(dto.getAlergias());
                mascota.setRestricciones(dto.getRestricciones());
                mascota.setColorPelaje(dto.getColorPelaje());
                mascota.setPesoKg(dto.getPesoKg());
                mascota.setFechaNac(dto.getFechaNac());
                return toDTO(mascotaRepository.save(mascota));
        }

        @Override
        public void eliminar(UUID id) {
                mascotaRepository.deleteById(id);
        }

        @Override
        public List<MascotasDTO> listarPorCorreo(String correo) {
                Usuarios usuario = usuarioRepository.findByCorreo(correo)
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                Clientes cliente = clienteRepository.findByUsuariosId(usuario.getId())
                                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
                return mascotaRepository.findByClientesId(cliente.getId())
                                .stream().map(this::toDTO).collect(Collectors.toList());
        }

        @Override
        public MascotasDTO crearParaCliente(String correo, MascotasDTO dto) {
                Usuarios usuario = usuarioRepository.findByCorreo(correo)
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                Clientes cliente = clienteRepository.findByUsuariosId(usuario.getId())
                                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
                Mascotas mascota = buildMascota(dto, cliente);
                return toDTO(mascotaRepository.save(mascota));
        }

        private Mascotas buildMascota(MascotasDTO dto, Clientes cliente) {
                return Mascotas.builder()
                                .clientes(cliente)
                                .nombre(dto.getNombre())
                                .especie(dto.getEspecie())
                                .sexo(dto.getSexo() != null ? dto.getSexo() : "desconocido")
                                .temperamento(dto.getTemperamento() != null ? dto.getTemperamento() : "desconocido")
                                .alergias(dto.getAlergias())
                                .restricciones(dto.getRestricciones())
                                .colorPelaje(dto.getColorPelaje())
                                .pesoKg(dto.getPesoKg())
                                .fechaNac(dto.getFechaNac())
                                .activa(true)
                                .build();
        }

        private MascotasDTO toDTO(Mascotas m) {
                return MascotasDTO.builder()
                                .id(m.getId())
                                .clienteId(m.getClientes().getId())
                                .clienteNombre(m.getClientes().getUsuarios().getNombre())
                                .clienteApellido(m.getClientes().getUsuarios().getApellido())
                                .nombre(m.getNombre())
                                .especie(m.getEspecie())
                                .sexo(m.getSexo())
                                .temperamento(m.getTemperamento())
                                .alergias(m.getAlergias())
                                .restricciones(m.getRestricciones())
                                .colorPelaje(m.getColorPelaje())
                                .pesoKg(m.getPesoKg())
                                .fechaNac(m.getFechaNac())
                                .activa(m.getActiva())
                                .fotoUrl(m.getFotoUrl())
                                .creadoEn(m.getCreadoEn())
                                .build();
        }

        @Override
        public List<MascotasDTO> listarPorClienteId(UUID clienteId) {
                return mascotaRepository.findByClientesId(clienteId)
                                .stream().map(this::toDTO).collect(Collectors.toList());
        }
}