package com.example.proydbp.events.email_event;

import com.example.proydbp.events.model.Mail;
import com.example.proydbp.pedido_local.domain.PedidoLocal;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.HashMap;
import java.util.Map;

@Getter
public class EstadoPedidoLocalListoEvent extends ApplicationEvent {

    private final PedidoLocal pedidoLocal;
    private final Mail mail;

    public EstadoPedidoLocalListoEvent(PedidoLocal pedidoLocal, String recipientEmail) {
        super(pedidoLocal);
        this.pedidoLocal = pedidoLocal;

        // Configuración del correo electrónico
        Map<String, Object> properties = new HashMap<>();
        properties.put("ID del Pedido", pedidoLocal.getId());
        properties.put("Estado", "Listo");

        Mail mail = Mail.builder()
                .from("fernando.munoz.p@utec.edu.pe")
                .to(recipientEmail)
                .htmlTemplate(new Mail.HtmlTemplate("PedidoListoTemplate", properties))
                .subject("Estado del Pedido Local Cambiado a Listo")
                .build();

        this.mail = mail;
    }
}
