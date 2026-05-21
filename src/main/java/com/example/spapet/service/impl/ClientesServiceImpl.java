package com.example.spapet.service.impl;

import com.example.spapet.dto.ClientesDTO;
import com.example.spapet.model.Clientes;
import com.example.spapet.model.Usuarios;

import com.example.spapet.repository.ClientesRepository;
import com.example.spapet.repository.UsuariosRepository;

import com.example.spapet.service.ClientesService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientesServiceImpl implements ClientesService {

    private final ClientesRepository clientesRepository;
    private final UsuariosRepository usuariosRepository;

    @Override
    public List<ClientesDTO> obtenerTodos() {

        return clientesRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ClientesDTO obtenerPorId(UUID id) {

        Clientes cliente = clientesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        return convertToDTO(cliente);
    }

    @Override
    public ClientesDTO crear(ClientesDTO dto) {

        Usuarios usuario = usuariosRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Clientes cliente = Clientes.builder()
                .usuarios(usuario)
                .direccion(dto.getDireccion())
                .ciudad(dto.getCiudad())
                .canalPreferido(dto.getCanalPreferido())
                .notas(dto.getNotas())
                .build();

        Clientes clienteGuardado = clientesRepository.save(cliente);

        return convertToDTO(clienteGuardado);
    }

    @Override
    public ClientesDTO actualizar(UUID id, ClientesDTO dto) {

        Clientes cliente = clientesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Usuarios usuario = usuariosRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        cliente.setUsuarios(usuario);
        cliente.setDireccion(dto.getDireccion());
        cliente.setCiudad(dto.getCiudad());
        cliente.setCanalPreferido(dto.getCanalPreferido());
        cliente.setNotas(dto.getNotas());

        Clientes clienteActualizado = clientesRepository.save(cliente);

        return convertToDTO(clienteActualizado);
    }

    @Override
    public void eliminar(UUID id) {

        Clientes cliente = clientesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        clientesRepository.delete(cliente);
    }

    // =========================
    // CONVERTERS
    // =========================

    private ClientesDTO convertToDTO(Clientes cliente) {

        return ClientesDTO.builder()
                .id(cliente.getId())

                .usuarioId(
                        cliente.getUsuarios() != null
                                ? cliente.getUsuarios().getId()
                                : null)

                .usuarioNombre(
                        cliente.getUsuarios() != null
                                ? cliente.getUsuarios().getNombre()
                                : null)

                .usuarioApellido(
                        cliente.getUsuarios() != null
                                ? cliente.getUsuarios().getApellido()
                                : null)

                .usuarioCorreo(
                        cliente.getUsuarios() != null
                                ? cliente.getUsuarios().getCorreo()
                                : null)

                .direccion(cliente.getDireccion())
                .ciudad(cliente.getCiudad())
                .canalPreferido(cliente.getCanalPreferido())
                .notas(cliente.getNotas())
                .creadoEn(cliente.getCreadoEn())
                .actualizadoEn(cliente.getActualizadoEn())
                .build();
    }
}