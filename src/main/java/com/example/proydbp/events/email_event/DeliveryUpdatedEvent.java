package com.example.proydbp.events.email_event;

import com.example.proydbp.delivery.domain.Delivery;
import com.example.proydbp.events.model.Mail;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.HashMap;
import java.util.Map;

@Getter
public class DeliveryUpdatedEvent extends ApplicationEvent {

    private final Delivery delivery;
    private final Mail mail;

    public DeliveryUpdatedEvent(Delivery delivery, String recipientEmail) {
        super(delivery);
        this.delivery = delivery;

        // Configuración del correo electrónico
        Map<String, Object> properties = new HashMap<>();
        properties.put("ID de la Entrega", delivery.getId());
        properties.put("Usuario", delivery.getClient().getFirstName() + " " + delivery.getClient().getLastName());
        properties.put("Email", delivery.getClient().getEmail());
        properties.put("Nueva Direccion", delivery.getDireccion());
        properties.put("Fecha", delivery.getFecha());
        properties.put("Estado", "Actualizado");

        Mail mail = Mail.builder()
                .from("fernando.munoz.p@utec.edu.pe")
                .to(recipientEmail)
                .htmlTemplate(new Mail.HtmlTemplate("DeliveryUpdatedTemplate", properties))
                .subject("Entrega Actualizada")
                .build();

        this.mail = mail;
    }
}
