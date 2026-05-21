package com.example.spapet.service;

import com.example.spapet.dto.AuditoriaDTO;
import com.example.spapet.model.Auditoria_acceso;
import com.example.spapet.model.Usuarios;
import com.example.spapet.repository.Auditoria_accesoRepository;
import com.example.spapet.repository.UsuariosRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuditoriaService {

    private final Auditoria_accesoRepository auditoriaRepository;
    private final UsuariosRepository usuariosRepository;

    public void registrar(String correo,
            String accion,
            String resultado,
            String detalle,
            HttpServletRequest request) {
        Auditoria_acceso auditoria = Auditoria_acceso.builder()
                .correo(correo)
                .accion(accion)
                .resultado(resultado)
                .detalle(detalle)
                .ip(getIp(request))
                .navegador(request.getHeader("User-Agent"))
                .build();

        if (correo != null) {
            usuariosRepository.findByCorreo(correo)
                    .ifPresent(auditoria::setUsuario);
        }

        auditoriaRepository.save(auditoria);
    }

    public List<AuditoriaDTO> listarTodos() {
        return auditoriaRepository.findAllByOrderByCreadoEnDesc()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<AuditoriaDTO> listarPorCorreo(String correo) {
        return auditoriaRepository.findByCorreoOrderByCreadoEnDesc(correo)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<AuditoriaDTO> listarPorAccion(String accion) {
        return auditoriaRepository.findByAccionOrderByCreadoEnDesc(accion)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<AuditoriaDTO> listarPorResultado(String resultado) {
        return auditoriaRepository.findByResultadoOrderByCreadoEnDesc(resultado)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    private AuditoriaDTO toDTO(Auditoria_acceso a) {
        String nombre = null;
        String apellido = null;
        if (a.getUsuario() != null) {
            Usuarios u = a.getUsuario();
            nombre = u.getNombre();
            apellido = u.getApellido();
        }
        return AuditoriaDTO.builder()
                .id(a.getId())
                .correo(a.getCorreo())
                .usuarioNombre(nombre)
                .usuarioApellido(apellido)
                .accion(a.getAccion())
                .resultado(a.getResultado())
                .ip(a.getIp())
                .navegador(a.getNavegador())
                .detalle(a.getDetalle())
                .creadoEn(a.getCreadoEn())
                .build();
    }

    private String getIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isBlank()) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}