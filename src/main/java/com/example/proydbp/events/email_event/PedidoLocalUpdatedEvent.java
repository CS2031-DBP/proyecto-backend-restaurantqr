package com.example.proydbp.events.email_event;

import com.example.proydbp.events.model.Mail;
import com.example.proydbp.pedido_local.domain.PedidoLocal;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.HashMap;
import java.util.Map;

@Getter
public class PedidoLocalUpdatedEvent extends ApplicationEvent {

    final private PedidoLocal pedidoLocal;
    final private Mail mail;

    public PedidoLocalUpdatedEvent(PedidoLocal pedidoLocal, String recipientEmail) {
        super(pedidoLocal);
        this.pedidoLocal = pedidoLocal;

        // Configuración del correo electrónico
        Map<String, Object> properties = new HashMap<>();
        properties.put("ID del Pedido", pedidoLocal.getId());
        properties.put("Estado", pedidoLocal.getStatus());
        properties.put("Precio", pedidoLocal.getPrecio());


        Mail mail = Mail.builder()
                .from("fernando.munoz.p@utec.edu.pe")
                .to(recipientEmail)
                .htmlTemplate(new Mail.HtmlTemplate("PedidoUpdatedTemplate", properties))
                .subject("Pedido Local Actualizado")
                .build();

        this.mail = mail;
    }
}
