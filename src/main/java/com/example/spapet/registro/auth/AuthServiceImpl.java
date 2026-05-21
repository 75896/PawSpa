package com.example.spapet.registro.auth;

import com.example.spapet.dto.auth.AuthResponse;
import com.example.spapet.dto.auth.CambioPasswordRequest;
import com.example.spapet.dto.auth.LoginRequest;
import com.example.spapet.dto.auth.RegisterRequest;
import com.example.spapet.dto.auth.SolicitarCambioRequest;
import com.example.spapet.model.Clientes;
import com.example.spapet.model.Usuarios;
import com.example.spapet.repository.ClientesRepository;
import com.example.spapet.repository.UsuariosRepository;
import com.example.spapet.service.AuditoriaService;
import com.example.spapet.registro.security.CaptchaService;
import com.example.spapet.registro.security.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsuariosRepository usuarioRepository;
    private final ClientesRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final MailService mailService;
    private final CaptchaService captchaService;
    private final AuditoriaService auditoriaService;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request, HttpServletRequest httpRequest) {
        if (usuarioRepository.existsByCorreo(request.getCorreo())) {
            auditoriaService.registrar(
                    request.getCorreo(), "REGISTRO", "FALLIDO",
                    "Correo ya registrado", httpRequest);
            throw new RuntimeException("El correo ya está registrado");
        }

        Usuarios usuario = Usuarios.builder()
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .correo(request.getCorreo())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .telefono(request.getTelefono())
                .rol("cliente")
                .activo(false)
                .build();

        usuarioRepository.save(usuario);

        Clientes cliente = Clientes.builder()
                .usuarios(usuario)
                .direccion(request.getDireccion())
                .ciudad(request.getCiudad())
                .canalPreferido("whatsapp")
                .build();

        clienteRepository.save(cliente);

        String tokenActivacion = jwtUtils.generateActivationToken(usuario.getCorreo());
        mailService.enviarActivacion(
                usuario.getCorreo(),
                usuario.getNombre(),
                tokenActivacion);

        auditoriaService.registrar(
                usuario.getCorreo(), "REGISTRO", "EXITOSO",
                "Nuevo cliente registrado", httpRequest);

        return AuthResponse.builder()
                .usuarioId(usuario.getId())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .correo(usuario.getCorreo())
                .rol(usuario.getRol())
                .requiere2fa(false)
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request, HttpServletRequest httpRequest) {
        Usuarios usuario = usuarioRepository.findByCorreo(request.getCorreo())
                .orElse(null);

        if (usuario == null || !passwordEncoder.matches(request.getPassword(), usuario.getPasswordHash())) {
            auditoriaService.registrar(
                    request.getCorreo(), "LOGIN", "FALLIDO",
                    "Credenciales inválidas", httpRequest);
            throw new RuntimeException("Credenciales inválidas");
        }

        if (!usuario.getActivo()) {
            auditoriaService.registrar(
                    request.getCorreo(), "LOGIN", "BLOQUEADO",
                    "Cuenta no activada", httpRequest);
            throw new RuntimeException("Cuenta no activada, revisa tu correo");
        }

        String token = jwtUtils.generateToken(usuario.getCorreo(), usuario.getRol());

        auditoriaService.registrar(
                request.getCorreo(), "LOGIN", "EXITOSO",
                "Inicio de sesión exitoso - Rol: " + usuario.getRol(), httpRequest);

        return AuthResponse.builder()
                .token(token)
                .usuarioId(usuario.getId())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .correo(usuario.getCorreo())
                .rol(usuario.getRol())
                .requiere2fa("admin".equals(usuario.getRol()))
                .build();
    }

    @Override
    @Transactional
    public void activarCuenta(String token) {
        if (!jwtUtils.isTokenValid(token)) {
            throw new RuntimeException("Token inválido o expirado");
        }
        String correo = jwtUtils.extractCorreo(token);
        Usuarios usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setActivo(true);
        usuarioRepository.save(usuario);
    }

    @Override
    public void reenviarActivacion(String correo) {
        Usuarios usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        if (usuario.getActivo()) {
            throw new RuntimeException("La cuenta ya está activa");
        }
        String token = jwtUtils.generateActivationToken(correo);
        mailService.enviarActivacion(correo, usuario.getNombre(), token);
    }

    @Override
    public void solicitarCambioPassword(SolicitarCambioRequest request) {
        Usuarios usuario = usuarioRepository.findByCorreo(request.getCorreo())
                .orElseThrow(() -> new RuntimeException("Correo no registrado"));
        String token = jwtUtils.generateActivationToken(usuario.getCorreo());
        mailService.enviarCambioPassword(
                usuario.getCorreo(),
                usuario.getNombre(),
                token);
    }

    @Override
    @Transactional
    public void cambiarPassword(CambioPasswordRequest request, HttpServletRequest httpRequest) {
        if (!jwtUtils.isTokenValid(request.getToken())) {
            throw new RuntimeException("Token inválido o expirado");
        }
        String correo = jwtUtils.extractCorreo(request.getToken());
        Usuarios usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setPasswordHash(passwordEncoder.encode(request.getNuevaPassword()));
        usuarioRepository.save(usuario);

        auditoriaService.registrar(
                correo, "CAMBIO_PASSWORD", "EXITOSO",
                "Contraseña cambiada correctamente", httpRequest);
    }
}