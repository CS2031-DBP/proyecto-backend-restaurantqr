package com.example.proydbp.events.email_event;

import com.example.proydbp.delivery.domain.Delivery;
import com.example.proydbp.events.model.Mail;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.HashMap;
import java.util.Map;

@Getter
public class DeliveryListoEvent extends ApplicationEvent {

    private final Delivery delivery;
    private final Mail mail;

    public DeliveryListoEvent(Delivery delivery, String recipientEmail) {
        super(delivery);
        this.delivery = delivery;

        // Configuración del correo electrónico
        Map<String, Object> properties = getStringObjectMap(delivery);

        Mail mail = Mail.builder()
                .from("fernando.munoz.p@utec.edu.pe")
                .to(recipientEmail)
                .htmlTemplate(new Mail.HtmlTemplate("DeliveryListoTemplate", properties))
                .subject("Entrega Lista")
                .build();

        this.mail = mail;
    }

    private static Map<String, Object> getStringObjectMap(Delivery delivery) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("ID de la Entrega", delivery.getId());
        properties.put("Repartidor", delivery.getRepartidor().getFirstName() + " " + delivery.getRepartidor().getLastName());
        properties.put("Usuario", delivery.getClient().getFirstName() + " " + delivery.getClient().getLastName());
        properties.put("Email", delivery.getClient().getEmail());
        properties.put("Direccion", delivery.getDireccion());
        properties.put("Estado", "Listo");
        return properties;
    }
}
