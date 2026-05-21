package com.example.spapet.service.impl;

import com.example.spapet.dto.UsuariosDTO;
import com.example.spapet.model.Groomers;
import com.example.spapet.model.Usuarios;
import com.example.spapet.registro.auth.MailService;
import com.example.spapet.repository.GroomersRepository;
import com.example.spapet.repository.UsuariosRepository;
import com.example.spapet.service.UsuariosService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class UsuariosServiceImpl implements UsuariosService {

    private final UsuariosRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final GroomersRepository groomersRepository;

    @Override
    public List<UsuariosDTO> listarTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UsuariosDTO obtenerPorId(UUID id) {
        Usuarios usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return toDTO(usuario);
    }

    @Override
    public UsuariosDTO crear(UsuariosDTO dto) {
        if (usuarioRepository.existsByCorreo(dto.getCorreo())) {
            throw new RuntimeException("El correo ya está registrado");
        }

        String passwordFinal;
        if (dto.getRol().equals("groomer") || dto.getRol().equals("recepcion")) {
            passwordFinal = generarPassword();
        } else {
            if (dto.getPasswordHash() == null || dto.getPasswordHash().isBlank()) {
                throw new RuntimeException("La contraseña es obligatoria");
            }
            passwordFinal = dto.getPasswordHash();
        }

        Usuarios usuario = Usuarios.builder()
                .nombre(dto.getNombre())
                .apellido(dto.getApellido())
                .correo(dto.getCorreo())
                .passwordHash(passwordEncoder.encode(passwordFinal))
                .telefono(dto.getTelefono())
                .rol(dto.getRol())
                .activo(true)
                .build();

        usuarioRepository.save(usuario);

        // Si es groomer crear registro en tabla groomers
        if (dto.getRol().equals("groomer")) {
            Groomers groomer = Groomers.builder()
                    .usuarios(usuario)
                    .capacidadMax((short) 1)
                    .activo(true)
                    .build();
            groomersRepository.save(groomer);
        }

        if (dto.getRol().equals("groomer") || dto.getRol().equals("recepcion")) {
            System.out.println("=== Enviando correo a: " + usuario.getCorreo());
            mailService.enviarCredencialesPersonal(
                    usuario.getCorreo(),
                    usuario.getNombre(),
                    passwordFinal,
                    dto.getRol());
            System.out.println("=== Correo enviado!");
        }

        return toDTO(usuario);
    }

    @Override
    public UsuariosDTO actualizar(UUID id, UsuariosDTO dto) {
        Usuarios usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setTelefono(dto.getTelefono());
        usuario.setRol(dto.getRol());

        if (dto.getPasswordHash() != null && !dto.getPasswordHash().isBlank()) {
            usuario.setPasswordHash(passwordEncoder.encode(dto.getPasswordHash()));
        }

        return toDTO(usuarioRepository.save(usuario));
    }

    @Override
    public void desactivar(UUID id) {
        Usuarios usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
    }

    @Override
    public void activar(UUID id) {
        Usuarios usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setActivo(true);
        usuarioRepository.save(usuario);
    }

    private UsuariosDTO toDTO(Usuarios u) {
        return UsuariosDTO.builder()
                .id(u.getId())
                .nombre(u.getNombre())
                .apellido(u.getApellido())
                .correo(u.getCorreo())
                .telefono(u.getTelefono())
                .rol(u.getRol())
                .activo(u.getActivo())
                .fotoUrl(u.getFotoUrl())
                .ultimoAcceso(u.getUltimoAcceso())
                .creadoEn(u.getCreadoEn())
                .actualizadoEn(u.getActualizadoEn())
                .build();
    }

    private String generarPassword() {
        String mayusculas = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String minusculas = "abcdefghijklmnopqrstuvwxyz";
        String numeros = "0123456789";
        String simbolos = "*#!@$%";
        String todos = mayusculas + minusculas + numeros + simbolos;

        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        // Garantiza al menos uno de cada tipo
        password.append(mayusculas.charAt(random.nextInt(mayusculas.length())));
        password.append(minusculas.charAt(random.nextInt(minusculas.length())));
        password.append(numeros.charAt(random.nextInt(numeros.length())));
        password.append(simbolos.charAt(random.nextInt(simbolos.length())));

        // Completa hasta 12 caracteres
        for (int i = 4; i < 12; i++) {
            password.append(todos.charAt(random.nextInt(todos.length())));
        }

        // Mezcla los caracteres
        char[] chars = password.toString().toCharArray();
        for (int i = chars.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = chars[i];
            chars[i] = chars[j];
            chars[j] = temp;
        }

        return new String(chars);
    }

    @Override
    public List<UsuariosDTO> listarPorRol(String rol) {
        return usuarioRepository.findByRol(rol)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }
}