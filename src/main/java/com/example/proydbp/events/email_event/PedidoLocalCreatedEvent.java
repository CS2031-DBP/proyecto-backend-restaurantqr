package com.example.proydbp.events.email_event;

import com.example.proydbp.events.model.Mail;
import com.example.proydbp.pedido_local.domain.PedidoLocal;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.HashMap;
import java.util.Map;

@Getter
public class PedidoLocalCreatedEvent extends ApplicationEvent {

    final private PedidoLocal pedidoLocal;
    final private Mail mail;

    public PedidoLocalCreatedEvent(PedidoLocal pedidoLocal, String recipientEmail) {
        super(pedidoLocal);
        this.pedidoLocal = pedidoLocal;

        // Configuración del correo electrónico
        Map<String, Object> properties = new HashMap<>();
        properties.put("ID del Pedido", pedidoLocal.getId());
        properties.put("Estado", pedidoLocal.getEstado());
        properties.put("Precio Total", pedidoLocal.getPrecio());
        properties.put("Tipo de Pago", pedidoLocal.getTipoPago());

        // Si el pedido tiene un mesero asignado
        if (pedidoLocal.getMesero() != null) {
            properties.put("Mesero", pedidoLocal.getMesero().getFirstName() + " " + pedidoLocal.getMesero().getLastName());
        }

        // Creación del correo
        Mail mail = Mail.builder()
                .from("fernando.munoz.p@utec.edu.pe")
                .to(recipientEmail)
                .htmlTemplate(new Mail.HtmlTemplate("PedidoCreatedTemplate", properties))
                .subject("Nuevo Pedido Local Creado") // Asunto del correo
                .build();

        this.mail = mail;
    }

}

