package com.example.proydbp.events.email_event;

import com.example.proydbp.events.model.Mail;
import com.example.proydbp.pedido_local.domain.PedidoLocal;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.HashMap;
import java.util.Map;

@Getter
public class PedidoLocalDeletedEvent extends ApplicationEvent {

    private final Long pedidoLocalId;
    final private Mail mail;

    public PedidoLocalDeletedEvent(Long pedidoLocalId, PedidoLocal pedidoLocal, String recipientEmail) {
        super(pedidoLocalId);
        this.pedidoLocalId = pedidoLocalId;

        // Configuración del correo electrónico
        Map<String, Object> properties = new HashMap<>();
        properties.put("ID del Pedido", pedidoLocal.getId());
        properties.put("Estado", pedidoLocal.getEstado());
        properties.put("Precio", pedidoLocal.getPrecio());

        Mail mail = Mail.builder()
                .from("fernando.munoz.p@utec.edu.pe")
                .to(recipientEmail)
                .htmlTemplate(new Mail.HtmlTemplate("PedidoDeletedTemplate", properties))
                .subject("Pedido Local Eliminado")
                .build();

        this.mail = mail;
    }
}
