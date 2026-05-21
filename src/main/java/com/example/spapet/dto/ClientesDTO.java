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
public class ClientesDTO implements Serializable {

    private UUID id;

    @NotNull(message = "El usuario es obligatorio")
    private UUID usuarioId;

    private String usuarioNombre;
    private String usuarioApellido;
    private String usuarioCorreo;

    @Size(max = 500, message = "La dirección no puede tener más de 500 caracteres")
    private String direccion;

    @Size(max = 100, message = "La ciudad no puede tener más de 100 caracteres")
    private String ciudad;

    @NotBlank(message = "El canal preferido es obligatorio")
    @Pattern(regexp = "^(whatsapp|telegram|correo|sms)$", message = "El canal debe ser 'whatsapp', 'telegram', 'correo' o 'sms'")
    private String canalPreferido;

    @Size(max = 500, message = "Las notas no pueden tener más de 500 caracteres")
    private String notas;

    private OffsetDateTime creadoEn;

    private OffsetDateTime actualizadoEn;
}