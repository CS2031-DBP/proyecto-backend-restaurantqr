package com.example.proydbp.events.email_event;

import com.example.proydbp.delivery.domain.Delivery;
import com.example.proydbp.events.model.Mail;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.HashMap;
import java.util.Map;

@Getter
public class DeliveryUpdateEvent extends ApplicationEvent {

    final private Mail mail;
    final private Delivery delivery;

    public DeliveryUpdateEvent(Delivery delivery, String recipientEmail){
        super(delivery);
        this.delivery = delivery;

        Map<String, Object> properties = new HashMap<>();
        properties.put("Descripcion", delivery.getDescripcion());
        properties.put("Direccion", delivery.getDireccion());

        this.mail = Mail.builder()
                .from("fernando.munoz.p@utec.edu.pe")
                .to(recipientEmail)
                .htmlTemplate(new Mail.HtmlTemplate("DeliveryUpdateTemplate", properties))
                .subject("Actualizamos su orden de delivery")
                .build();
    }
}
