package com.example.proydbp.events.email_event;

import com.example.proydbp.events.model.Mail;
import com.example.proydbp.reviewDelivery.domain.ReviewDelivery;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ReviewDeliveryCreatedEvent extends ApplicationEvent {

    final private ReviewDelivery reviewDelivery;
    final private Mail mail;

    public ReviewDeliveryCreatedEvent(ReviewDelivery reviewDelivery, String recipientEmail) {
        super(reviewDelivery);
        this.reviewDelivery = reviewDelivery;

        // Configuración del correo electrónico
        Map<String, Object> properties = new HashMap<>();
        properties.put("Nombre de Repartidor", reviewDelivery.getRepartidor().getFirstName() + " " + reviewDelivery.getRepartidor().getLastName());
        properties.put("Puntuación", reviewDelivery.getRatingScore());
        properties.put("Comentario", reviewDelivery.getComment());

        Mail mail = Mail.builder()
                .from("fernando.munoz.p@utec.edu.pe")
                .to(recipientEmail)
                .htmlTemplate(new Mail.HtmlTemplate("ReviewDeliveryCreatedTemplate", properties))
                .subject("Nueva Reseña de Repartidor Creada")
                .build();

        this.mail = mail;
    }
}
