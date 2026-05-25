package com.example.spapet.dto;

import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MensajePedidoDTO {

    private String canal; // "whatsapp" o "telegram"
    private String mensaje; // texto formateado listo para enviar
    private String linkWhatsapp; // wa.me/... con el mensaje codificado (si canal=whatsapp)
    private String linkTelegram; // t.me/... (si canal=telegram)
}