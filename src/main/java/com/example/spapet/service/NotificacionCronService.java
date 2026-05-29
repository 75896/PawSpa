package com.example.spapet.service;

import com.example.spapet.model.Citas;
import com.example.spapet.model.Variantes_productos;
import com.example.spapet.repository.CitasRepository;
import com.example.spapet.repository.Variantes_productosRepository;
import com.example.spapet.registro.auth.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificacionCronService {

    private final CitasRepository citasRepository;
    private final Variantes_productosRepository variantesRepository;
    private final MailService mailService;

    @Value("${spring.mail.username}")
    private String adminEmail;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    // Corre cada hora
    @Scheduled(fixedRate = 3600000)
    public void enviarRecordatorios24h() {
        log.info("=== CRON: Verificando recordatorios 24h ===");

        OffsetDateTime ahora = OffsetDateTime.now();
        OffsetDateTime en24h = ahora.plusHours(24);
        OffsetDateTime en25h = ahora.plusHours(25);

        List<Citas> citas = citasRepository.findByFechaInicioBetween(en24h, en25h);

        for (Citas cita : citas) {
            if (cita.getEstado().equals("cancelada"))
                continue;
            try {
                String correo = cita.getMascotas().getClientes().getUsuarios().getCorreo();
                String nombre = cita.getMascotas().getClientes().getUsuarios().getNombre();
                String mascota = cita.getMascotas().getNombre();
                String servicio = cita.getServicios().getNombre();
                String fecha = cita.getFechaInicio().format(formatter);

                mailService.enviarRecordatorio(correo, nombre, mascota, servicio, fecha, "24h");
                log.info("Recordatorio 24h enviado a: {}", correo);
            } catch (Exception e) {
                log.error("Error enviando recordatorio 24h: {}", e.getMessage());
            }
        }
    }

    // Corre cada 30 minutos
    @Scheduled(fixedRate = 1800000)
    public void enviarRecordatorios2h() {
        log.info("=== CRON: Verificando recordatorios 2h ===");

        OffsetDateTime ahora = OffsetDateTime.now();
        OffsetDateTime en2h = ahora.plusHours(2);
        OffsetDateTime en2h30 = ahora.plusHours(2).plusMinutes(30);

        List<Citas> citas = citasRepository.findByFechaInicioBetween(en2h, en2h30);

        for (Citas cita : citas) {
            if (cita.getEstado().equals("cancelada"))
                continue;
            try {
                String correo = cita.getMascotas().getClientes().getUsuarios().getCorreo();
                String nombre = cita.getMascotas().getClientes().getUsuarios().getNombre();
                String mascota = cita.getMascotas().getNombre();
                String servicio = cita.getServicios().getNombre();
                String fecha = cita.getFechaInicio().format(formatter);

                mailService.enviarRecordatorio(correo, nombre, mascota, servicio, fecha, "2h");
                log.info("Recordatorio 2h enviado a: {}", correo);
            } catch (Exception e) {
                log.error("Error enviando recordatorio 2h: {}", e.getMessage());
            }
        }
    }

    // Corre cada hora
    @Scheduled(fixedRate = 3600000)
    public void verificarStockBajo() {
        log.info("=== CRON: Verificando stock bajo ===");

        List<Variantes_productos> bajos = variantesRepository.findConStockBajo();

        if (!bajos.isEmpty()) {
            for (Variantes_productos v : bajos) {
                try {
                    String producto = v.getProducto().getNombre() + " — " + v.getNombre();
                    mailService.enviarAlertaStockBajo(adminEmail, producto, v.getStock());
                    log.info("Alerta stock bajo enviada para: {}", producto);
                } catch (Exception e) {
                    log.error("Error enviando alerta stock: {}", e.getMessage());
                }
            }
        }
    }
}