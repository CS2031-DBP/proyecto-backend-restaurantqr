package com.example.proydbp.events.email_event;

import com.example.proydbp.delivery.domain.Delivery;
import com.example.proydbp.events.model.Mail;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.HashMap;
import java.util.Map;

@Getter
public class DeliveryCrearEvent extends ApplicationEvent {

    final private Mail mail;
    final private Delivery delivery;

    public DeliveryCrearEvent(Delivery delivery, String recipientEmail){
        super(delivery);
        this.delivery = delivery;

        Map<String, Object> properties = new HashMap<>();
        properties.put("id", delivery.getId());
        properties.put("Direccion", delivery.getDireccion());
        properties.put("Hora", delivery.getFecha().toLocalTime());
        properties.put("Fecha", delivery.getFecha().toLocalDate());
        properties.put("Estado", delivery.getStatus());

        this.mail = Mail.builder()
                .from("fernando.munoz.p@utec.edu.pe")
                .to(recipientEmail)
                .htmlTemplate(new Mail.HtmlTemplate("DeliveryCreadoTemplate", properties))
                .subject("Su orden de delivery fue creada exitosamente")
                .build();
    }
}
