package com.example.proydbp.events.email_event;

import com.example.proydbp.events.model.Mail;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ReviewDeliveryDeletedEvent extends ApplicationEvent {

    final private Long reviewId;
    final private Mail mail;

    public ReviewDeliveryDeletedEvent(Long reviewId, String recipientEmail) {
        super(reviewId);
        this.reviewId = reviewId;

        // Configuración del correo electrónico
        Map<String, Object> properties = new HashMap<>();
        properties.put("ID de Reseña", reviewId);

        Mail mail = Mail.builder()
                .from("fernando.munoz.p@utec.edu.pe")
                .to(recipientEmail)
                .htmlTemplate(new Mail.HtmlTemplate("ReviewDeliveryDeletedTemplate", properties))
                .subject("Reseña de Repartidor Eliminada")
                .build();

        this.mail = mail;
    }
}
