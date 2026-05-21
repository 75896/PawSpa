package com.example.spapet.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificacionesDTO implements Serializable {

    private UUID id;

    @NotNull(message = "El usuario es obligatorio")
    private UUID usuarioId;

    private String usuarioNombre;

    private UUID citaId;

    @NotBlank(message = "El tipo de notificación no puede estar vacío")
    @Size(max = 30, message = "El tipo no puede tener más de 30 caracteres")
    private String tipo;

    @NotBlank(message = "El canal no puede estar vacío")
    @Pattern(regexp = "^(whatsapp|telegram|correo|sms)$", message = "El canal debe ser whatsapp, telegram, correo o sms")
    @Size(max = 20, message = "El canal no puede tener más de 20 caracteres")
    private String canal;

    @NotBlank(message = "El estado es obligatorio")
    @Pattern(regexp = "^(pendiente|enviada|fallida|cancelada)$", message = "El estado debe ser pendiente, enviada, fallida o cancelada")
    @Size(max = 20, message = "El estado no puede tener más de 20 caracteres")
    private String estado;

    @Size(max = 200, message = "El asunto no puede tener más de 200 caracteres")
    private String asunto;

    @Size(max = 3000, message = "El cuerpo no puede tener más de 3000 caracteres")
    private String cuerpo;

    @NotNull(message = "La fecha programada es obligatoria")
    private OffsetDateTime programadaPara;

    private OffsetDateTime enviadaEn;

    @Size(max = 2000, message = "El detalle del error no puede tener más de 2000 caracteres")
    private String errorDetalle;

    private OffsetDateTime creadoEn;
}