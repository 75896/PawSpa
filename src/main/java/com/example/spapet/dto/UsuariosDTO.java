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
public class UsuariosDTO implements Serializable {

    private UUID id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 100, message = "El apellido debe tener entre 2 y 100 caracteres")
    private String apellido;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo no es válido")
    @Size(max = 255, message = "El correo no puede tener más de 255 caracteres")
    private String correo;

    @Size(max = 20, message = "El teléfono no puede tener más de 20 caracteres")
    private String telefono;

    private String passwordHash;

    @NotBlank(message = "El rol es obligatorio")
    @Pattern(regexp = "^(admin|recepcion|groomer|cliente)$", message = "El rol debe ser 'admin', 'recepcion', 'groomer' o 'cliente'")
    private String rol;

    private Boolean activo;

    private String fotoUrl;

    private String secret2fa;

    private OffsetDateTime ultimoAcceso;

    private OffsetDateTime creadoEn;

    private OffsetDateTime actualizadoEn;
}