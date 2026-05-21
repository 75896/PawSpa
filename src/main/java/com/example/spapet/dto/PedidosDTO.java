package com.example.spapet.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidosDTO implements Serializable {

    private UUID id;

    @NotNull(message = "El cliente es obligatorio")
    private UUID clienteId;

    private String clienteNombre;

    @NotBlank(message = "El estado es obligatorio")
    @Pattern(regexp = "^(borrador|confirmado|enviado|entregado|cancelado)$", message = "El estado debe ser borrador, confirmado, enviado, entregado o cancelado")
    @Size(max = 20, message = "El estado no puede tener más de 20 caracteres")
    private String estado;

    @NotBlank(message = "El canal del pedido es obligatorio")
    @Pattern(regexp = "^(whatsapp|telegram|web|tienda)$", message = "El canal debe ser whatsapp, telegram, web o tienda")
    @Size(max = 20, message = "El canal no puede tener más de 20 caracteres")
    private String canalPedido;

    @Size(max = 5000, message = "El mensaje enviado no puede tener más de 5000 caracteres")
    private String mensajeEnviado;

    @NotNull(message = "El subtotal es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El subtotal no puede ser negativo")
    private BigDecimal subtotal;

    @NotNull(message = "El descuento es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El descuento no puede ser negativo")
    private BigDecimal descuento;

    @NotNull(message = "El total es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El total no puede ser negativo")
    private BigDecimal total;

    @Size(max = 1000, message = "La dirección no puede tener más de 1000 caracteres")
    private String direccionEntrega;

    @Size(max = 2000, message = "Las notas no pueden tener más de 2000 caracteres")
    private String notas;

    private OffsetDateTime creadoEn;

    private OffsetDateTime actualizadoEn;
}