package com.example.proydbp.events.email_event;

import com.example.proydbp.delivery.domain.Delivery;
import com.example.proydbp.events.model.Mail;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.HashMap;
import java.util.Map;

@Getter
public class DeliveryEnRutaEvent extends ApplicationEvent {

    private final Delivery delivery;
    private final Mail mail;

    public DeliveryEnRutaEvent(Delivery delivery, String recipientEmail) {
        super(delivery);
        this.delivery = delivery;

        // Configuración del correo electrónico
        Map<String, Object> properties = new HashMap<>();
        properties.put("ID de la Entrega", delivery.getId());
        properties.put("Repartidor", delivery.getRepartidor().getFirstName() + " " + delivery.getRepartidor().getLastName());
        properties.put("Direccion", delivery.getDireccion());
        properties.put("Fecha", delivery.getFecha());
        properties.put("Costo delivery", delivery.getCostoDelivery());
        properties.put("Total", delivery.getCostoDelivery() + delivery.getPrecio());
        properties.put("Estado", "En Ruta");

        Mail mail = Mail.builder()
                .from("fernando.munoz.p@utec.edu.pe")
                .to(recipientEmail)
                .htmlTemplate(new Mail.HtmlTemplate("DeliveryEnRutaTemplate", properties))
                .subject("Entrega en Ruta")
                .build();

        this.mail = mail;
    }
}
