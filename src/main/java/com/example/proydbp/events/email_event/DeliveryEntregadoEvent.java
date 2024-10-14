package com.example.proydbp.events.email_event;

import com.example.proydbp.delivery.domain.Delivery;
import com.example.proydbp.events.model.Mail;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.HashMap;
import java.util.Map;

@Getter
public class DeliveryEntregadoEvent extends ApplicationEvent {

    private final Delivery delivery;
    private final Mail mail;

    public DeliveryEntregadoEvent(Delivery delivery, String recipientEmail) {
        super(delivery);
        this.delivery = delivery;

        // Configuración del correo electrónico
        Map<String, Object> properties = new HashMap<>();
        properties.put("ID de la Entrega", delivery.getId());
        properties.put("Usuario", delivery.getClient().getFirstName() + " " + delivery.getClient().getLastName());
        properties.put("Estado", "Entregado");

        Mail mail = Mail.builder()
                .from("fernando.munoz.p@utec.edu.pe")
                .to(recipientEmail)
                .htmlTemplate(new Mail.HtmlTemplate("DeliveryEntregadoTemplate", properties))
                .subject("Entregado")
                .build();

        this.mail = mail;
    }
}
