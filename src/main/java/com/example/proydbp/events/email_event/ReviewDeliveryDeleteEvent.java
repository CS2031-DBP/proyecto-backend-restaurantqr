package com.example.proydbp.events.email_event;

import com.example.proydbp.events.model.Mail;
import com.example.proydbp.reviewDelivery.domain.ReviewDelivery;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ReviewDeliveryDeleteEvent extends ApplicationEvent {

    private final Mail mail;
    private final ReviewDelivery reviewDelivery;

    public ReviewDeliveryDeleteEvent(ReviewDelivery reviewDelivery, String recipientEmail) {
        super(reviewDelivery);
        this.reviewDelivery = reviewDelivery;

        Map<String, Object> properties = new HashMap<>();
        properties.put("id", reviewDelivery.getId());
        properties.put("Puntuación", reviewDelivery.getRatingScore());
        properties.put("Comentario", reviewDelivery.getComment());

        this.mail = Mail.builder()
                .from("fernando.munoz.p@utec.edu.pe")
                .to(recipientEmail)
                .htmlTemplate(new Mail.HtmlTemplate("ReviewDeliveryDeleteTemplate", properties))
                .subject("Reseña de repartidor eliminada")
                .build();
    }
}
