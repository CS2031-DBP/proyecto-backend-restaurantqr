package com.example.proydbp.events.email_event;

import com.example.proydbp.events.model.Mail;
import com.example.proydbp.reviewMesero.domain.ReviewMesero;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ReviewMeseroCreadoEvent extends ApplicationEvent {

    private final Mail mail;
    private final ReviewMesero reviewMesero;

    public ReviewMeseroCreadoEvent(ReviewMesero reviewMesero, String recipientEmail) {
        super(reviewMesero);
        this.reviewMesero = reviewMesero;

        Map<String, Object> properties = new HashMap<>();
        properties.put("id", reviewMesero.getId());
        properties.put("Puntuación", reviewMesero.getRatingScore());
        properties.put("Comentario", reviewMesero.getComment());
        properties.put("Fecha", reviewMesero.getFecha());

        this.mail = Mail.builder()
                .from("fernando.munoz.p@utec.edu.pe")
                .to(recipientEmail)
                .htmlTemplate(new Mail.HtmlTemplate("ReviewMeseroCreadoTemplate", properties))
                .subject("Nueva Reseña de Mesero Creada")
                .build();
    }
}