package com.example.proydbp.events.email_event;

import com.example.proydbp.events.model.Mail;
import com.example.proydbp.pedido_local.domain.PedidoLocal;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.HashMap;
import java.util.Map;

@Getter
public class PedidoLocalUpdateEvent extends ApplicationEvent {

    private final Mail mail;
    private final PedidoLocal pedidoLocal;

    public PedidoLocalUpdateEvent(PedidoLocal pedidoLocal, String recipientEmail) {
        super(pedidoLocal);
        this.pedidoLocal = pedidoLocal;

        Map<String, Object> properties = new HashMap<>();
        properties.put("Descripcion", pedidoLocal.getDescripcion());
        properties.put("TipoPago", pedidoLocal.getTipoPago());

        this.mail = Mail.builder()
                .from("fernando.munoz.p@utec.edu.pe")
                .to(recipientEmail)
                .htmlTemplate(new Mail.HtmlTemplate("PedidoLocalUpdateTemplate", properties))
                .subject("Actualización de los datos de su Pedido en Local")
                .build();
    }
}
