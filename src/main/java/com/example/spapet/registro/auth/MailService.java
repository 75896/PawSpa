package com.example.spapet.registro.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    public void enviarActivacion(String correo, String nombre, String token) {
        String link = "http://localhost:5173/activar?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(correo);
        message.setSubject("PawSpa — Activa tu cuenta");
        message.setText(
                "Hola " + nombre + ",\n\n" +
                        "Gracias por registrarte en PawSpa.\n" +
                        "Haz clic en el siguiente enlace para activar tu cuenta:\n\n" +
                        link + "\n\n" +
                        "Este enlace expira en 15 minutos.\n\n" +
                        "El equipo de PawSpa 🐾");

        mailSender.send(message);
    }

    public void enviarCambioPassword(String correo, String nombre, String token) {
        String link = "http://localhost:5173/cambiar-password?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(correo);
        message.setSubject("PawSpa — Cambio de contraseña");
        message.setText(
                "Hola " + nombre + ",\n\n" +
                        "Recibimos una solicitud para cambiar tu contraseña.\n" +
                        "Haz clic en el siguiente enlace:\n\n" +
                        link + "\n\n" +
                        "Este enlace expira en 15 minutos.\n" +
                        "Si no solicitaste esto ignora este correo.\n\n" +
                        "El equipo de PawSpa 🐾");

        mailSender.send(message);
    }

    public void enviarCredencialesPersonal(String correo, String nombre,
            String password, String rol) {
        String rolNombre = rol.equals("groomer") ? "Groomer" : "Recepcionista";
        String link = "http://localhost:5173/login";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(correo);
        message.setSubject("PawSpa — Bienvenido al equipo 🐾");
        message.setText(
                "Hola " + nombre + ",\n\n" +
                        "El administrador te ha creado una cuenta como " + rolNombre + " en PawSpa.\n\n" +
                        "Tus credenciales de acceso son:\n" +
                        "Correo:      " + correo + "\n" +
                        "Contraseña:  " + password + "\n\n" +
                        "Ingresa aquí: " + link + "\n\n" +
                        "Te recomendamos cambiar tu contraseña después de iniciar sesión.\n" +
                        "Puedes hacerlo desde la opción '¿Olvidaste tu contraseña?' en el login.\n\n" +
                        "El equipo de PawSpa 🐾");

        mailSender.send(message);
    }

    public void enviarConfirmacionCita(String correo, String nombre,
            String mascota, String servicio,
            String fecha) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(from);
        msg.setTo(correo);
        msg.setSubject("PawSpa — Tu cita ha sido confirmada 🐾");
        msg.setText(
                "Hola " + nombre + ",\n\n" +
                        "Tu cita ha sido confirmada:\n" +
                        "🐾 Mascota: " + mascota + "\n" +
                        "✂️  Servicio: " + servicio + "\n" +
                        "📅  Fecha: " + fecha + "\n\n" +
                        "Te esperamos en PawSpa 🐾");
        mailSender.send(msg);
    }

    public void enviarRecordatorio(String correo, String nombre,
            String mascota, String servicio,
            String fecha, String tipo) {
        String tiempoText = tipo.equals("24h") ? "mañana" : "en 2 horas";
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(from);
        msg.setTo(correo);
        msg.setSubject("PawSpa — Recordatorio de cita 🐾");
        msg.setText(
                "Hola " + nombre + ",\n\n" +
                        "Te recordamos que tienes una cita " + tiempoText + ":\n" +
                        "🐾 Mascota: " + mascota + "\n" +
                        "✂️  Servicio: " + servicio + "\n" +
                        "📅  Fecha: " + fecha + "\n\n" +
                        "¡Nos vemos pronto! 🐾");
        mailSender.send(msg);
    }

    public void enviarListoParaRecoger(String correo, String nombre, String mascota) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(from);
        msg.setTo(correo);
        msg.setSubject("PawSpa — " + mascota + " está listo para recoger 🐾");
        msg.setText(
                "Hola " + nombre + ",\n\n" +
                        mascota + " ya terminó su sesión de grooming y está listo para ser recogido.\n\n" +
                        "¡Te esperamos en PawSpa! 🐾");
        mailSender.send(msg);
    }

    public void enviarAlertaStockBajo(String correo, String producto, int stockActual) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(from);
        msg.setTo(correo);
        msg.setSubject("PawSpa — Alerta de stock bajo ⚠️");
        msg.setText(
                "Alerta de inventario:\n\n" +
                        "El producto '" + producto + "' tiene stock bajo.\n" +
                        "Stock actual: " + stockActual + " unidades.\n\n" +
                        "Por favor reponer el inventario.\n\n" +
                        "Sistema PawSpa 🐾");
        mailSender.send(msg);
    }

    public void enviarPagoRegistrado(String correo, String nombre,
            String numero, String total) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(from);
        msg.setTo(correo);
        msg.setSubject("PawSpa — Pago registrado ✅");
        msg.setText(
                "Hola " + nombre + ",\n\n" +
                        "Tu pago ha sido registrado correctamente.\n" +
                        "Factura N°: " + numero + "\n" +
                        "Total: Bs. " + total + "\n\n" +
                        "Gracias por confiar en PawSpa 🐾");
        mailSender.send(msg);
    }
}