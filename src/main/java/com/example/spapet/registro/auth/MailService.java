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
}